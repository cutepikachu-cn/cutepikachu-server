package cn.cutepikachu.ai.service.impl;

import cn.cutepikachu.ai.dao.repository.AiImageRepository;
import cn.cutepikachu.ai.factory.AiImageModelFactory;
import cn.cutepikachu.ai.model.convert.AiImageConvert;
import cn.cutepikachu.ai.model.enums.AiImageStatus;
import cn.cutepikachu.ai.model.enums.AiPlatform;
import cn.cutepikachu.ai.model.image.dto.AiImageDrawDTO;
import cn.cutepikachu.ai.model.image.entity.AiImage;
import cn.cutepikachu.ai.model.image.vo.AiImageVO;
import cn.cutepikachu.ai.mq.model.AiImageMessage;
import cn.cutepikachu.ai.mq.provider.AiImageProvider;
import cn.cutepikachu.ai.service.IAiImageService;
import cn.cutepikachu.common.constant.DistributedBizTag;
import cn.cutepikachu.common.model.biz.entity.FileInfo;
import cn.cutepikachu.common.model.biz.enums.FileBizTag;
import cn.cutepikachu.common.model.user.entity.UserInfo;
import cn.cutepikachu.common.response.BaseResponse;
import cn.cutepikachu.common.response.ErrorCode;
import cn.cutepikachu.inner.biz.FileInnerService;
import cn.cutepikachu.inner.biz.dto.FileSaveDTO;
import cn.cutepikachu.inner.leaf.DistributedIdInnerService;
import cn.hutool.core.codec.Base64;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.image.*;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.net.URLConnection;
import java.time.LocalDateTime;

import static cn.cutepikachu.common.exception.ExceptionFactory.bizException;

/**
 * AI 文生图表 服务实现类
 *
 * @author <a href="https://github.com/cutepikachu-cn">笨蛋皮卡丘</a>
 * @version 0.0.1-SNAPSHOT
 * @since 2024-10-12 19:44:03
 */
@Service
@Slf4j
public class AiImageServiceImpl implements IAiImageService {

    @Resource
    private AiImageRepository aiImageRepository;

    @Resource
    private AiImageModelFactory aiImageModelFactory;

    @Resource
    private DistributedIdInnerService distributedIdInnerService;

    @Resource
    private FileInnerService fileInnerService;

    @Resource
    private AiImageProvider aiImageProvider;

    private static final AiImageConvert AI_IMAGE_CONVERT = AiImageConvert.INSTANCE;

    @Override
    public AiImageVO drawImage(AiImageDrawDTO aiImageDrawDTO, UserInfo user) {
        // 参数校验
        checkImageOptions(aiImageDrawDTO);

        // 保存绘图记录
        AiImage aiImage = AI_IMAGE_CONVERT.convert(aiImageDrawDTO);
        aiImage.setUserId(user.getUserId())
                .setStatus(AiImageStatus.IN_PROGRESS);

        // 获取分布式 ID
        BaseResponse<Long> resp = distributedIdInnerService.getDistributedID(DistributedBizTag.AI_IMAGE.getKey());
        resp.check();
        aiImage.setId(resp.getData());
        aiImageRepository.save(aiImage);

        // 异步绘图
        // 获取注册为 Bean 的代理对象，否则无法执行 AOP 异步调用
        // AiImageServiceImpl aiImageService = SpringUtils.getBean(getClass());
        // aiImageService.drawImageAsync(imageOptions, aiImage);

        // 发送绘图任务信息
        AiImageMessage aiImageMessage = AiImageMessage.builder()
                .content(aiImage)
                .build();
        aiImageProvider.send(aiImageMessage);

        // 返回绘图记录
        return AI_IMAGE_CONVERT.convert(aiImage);
    }

    @Deprecated
    // @Async
    public void drawImageAsync(ImageOptions imageOptions, AiImage aiImage) {
        try {
            ImageModel imageModel = aiImageModelFactory.getImageModel(aiImage.getPlatform());
            ImagePrompt imagePrompt = new ImagePrompt(aiImage.getPrompt(), imageOptions);
            ImageResponse imageResponse = imageModel.call(imagePrompt);
            ImageGeneration result = imageResponse.getResult();
            Image output = result.getOutput();

            // 获取图片内容
            String b64Json = output.getB64Json();
            byte[] imageContent = StrUtil.isNotBlank(b64Json)
                    ? Base64.decode(b64Json)
                    : HttpUtil.downloadBytes(imageResponse.getResult().getOutput().getUrl());

            // 获取文件类型
            String contentType = URLConnection.guessContentTypeFromStream(new ByteArrayInputStream(imageContent));

            // 保存生成的图片到 OSS
            FileSaveDTO fileSaveDTO = new FileSaveDTO();
            fileSaveDTO.setBytes(imageContent)
                    .setFileBizTag(FileBizTag.IMAGE_AI)
                    .setContentType(contentType)
                    .setFileName(aiImage.getId().toString());
            BaseResponse<FileInfo> resp = fileInnerService.saveFile(fileSaveDTO);
            resp.check();
            FileInfo fileInfo = resp.getData();

            // 更新绘图记录信息
            String imageUrl = String.format("%s/%s/%s", fileInfo.getEndpoint(), fileInfo.getBucket(), fileInfo.getPath());

            aiImageRepository.lambdaUpdate()
                    .eq(AiImage::getId, aiImage.getId())
                    .set(AiImage::getStatus, AiImageStatus.SUCCESS)
                    .set(AiImage::getFinishTime, LocalDateTime.now())
                    .set(AiImage::getImageUrl, imageUrl)
                    .update();

        } catch (Exception e) {
            log.error("绘图失败 {}", aiImage, e);
            aiImageRepository.lambdaUpdate()
                    .eq(AiImage::getId, aiImage.getId())
                    .set(AiImage::getStatus, AiImageStatus.FAIL)
                    .set(AiImage::getErrorMessage, e.getMessage())
                    .update();
        }
    }

    /**
     * 参数校验
     */
    private void checkImageOptions(AiImageDrawDTO aiImageDrawDTO) {
        AiPlatform aiPlatform = aiImageDrawDTO.getPlatform();
        if (aiPlatform == null) {
            throw bizException(ErrorCode.BAD_REQUEST, "不支持的平台");
        }

        String model = aiImageDrawDTO.getModel();
        if (StrUtil.isBlank(model)) {
            throw bizException(ErrorCode.BAD_REQUEST, "模型不能为空");
        }

        String prompt = aiImageDrawDTO.getPrompt();
        if (StrUtil.isBlank(prompt)) {
            throw bizException(ErrorCode.BAD_REQUEST, "提示不能为空");
        }

        Integer height = aiImageDrawDTO.getHeight();
        if (height == null || height.compareTo(0) <= 0) {
            throw bizException(ErrorCode.BAD_REQUEST, "无效的高度");
        }

        Integer width = aiImageDrawDTO.getWidth();
        if (width == null || width.compareTo(0) <= 0) {
            throw bizException(ErrorCode.BAD_REQUEST, "无效的宽度");
        }
    }

    @Override
    public AiImageVO getAiImage(Long id, UserInfo user) {
        AiImage aiImage = aiImageRepository.lambdaQuery()
                .eq(AiImage::getId, id)
                .eq(AiImage::getUserId, user.getUserId())
                .one();
        if (aiImage == null) {
            throw bizException(ErrorCode.NOT_FOUND, "未找到记录");
        }
        return AI_IMAGE_CONVERT.convert(aiImage);
    }

}
