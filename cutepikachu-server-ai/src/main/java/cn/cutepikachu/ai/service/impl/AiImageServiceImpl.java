package cn.cutepikachu.ai.service.impl;

import cn.cutepikachu.ai.factory.AiImageModelFactory;
import cn.cutepikachu.ai.mapper.AiImageMapper;
import cn.cutepikachu.ai.model.convert.AiImageConvert;
import cn.cutepikachu.ai.model.enums.AiImageStatus;
import cn.cutepikachu.ai.model.enums.AiPlatform;
import cn.cutepikachu.ai.model.image.dto.AiImageDrawDTO;
import cn.cutepikachu.ai.model.image.entity.AiImage;
import cn.cutepikachu.ai.model.image.vo.AiImageVO;
import cn.cutepikachu.ai.service.IAiImageService;
import cn.cutepikachu.common.constant.DistributedBizTag;
import cn.cutepikachu.common.model.biz.entity.FileInfo;
import cn.cutepikachu.common.model.biz.enums.FileBizTag;
import cn.cutepikachu.common.model.user.entity.UserInfo;
import cn.cutepikachu.common.response.BaseResponse;
import cn.cutepikachu.common.response.ErrorCode;
import cn.cutepikachu.common.util.SpringUtils;
import cn.cutepikachu.inner.biz.FileInnerService;
import cn.cutepikachu.inner.biz.dto.FileSaveDTO;
import cn.cutepikachu.inner.leaf.DistributedIdInnerService;
import cn.hutool.core.codec.Base64;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import com.alibaba.cloud.ai.tongyi.image.TongYiImagesOptions;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.image.*;
import org.springframework.ai.zhipuai.ZhiPuAiImageOptions;
import org.springframework.scheduling.annotation.Async;
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
public class AiImageServiceImpl extends ServiceImpl<AiImageMapper, AiImage> implements IAiImageService {

    @Resource
    private AiImageModelFactory aiImageModelFactory;

    @Resource
    private DistributedIdInnerService distributedIdInnerService;

    @Resource
    private FileInnerService fileInnerService;

    private static final AiImageConvert AI_IMAGE_CONVERT = AiImageConvert.INSTANCE;

    @Override
    public AiImageVO drawImage(AiImageDrawDTO aiImageDrawDTO, UserInfo user) {
        // 构建绘图参数
        AiPlatform platform = aiImageDrawDTO.getPlatform();
        ImageOptions imageOptions = buildImageOptions(aiImageDrawDTO, platform);

        // 保存绘图记录
        AiImage aiImage = new AiImage();
        aiImage.setUserId(user.getUserId())
                .setModel(aiImageDrawDTO.getModel())
                .setPlatform(aiImageDrawDTO.getPlatform())
                .setPrompt(aiImageDrawDTO.getPrompt())
                .setHeight(aiImageDrawDTO.getHeight())
                .setWidth(aiImageDrawDTO.getWidth())
                .setOptions(aiImageDrawDTO.getOptions())
                .setStatus(AiImageStatus.IN_PROGRESS);
        // 获取分布式 ID
        BaseResponse<Long> resp = distributedIdInnerService.getDistributedID(DistributedBizTag.AI_IMAGE);
        resp.check();
        aiImage.setId(resp.getData());
        this.save(aiImage);

        // 异步绘图
        // 获取注册为 Bean 的代理对象，否则无法执行 AOP 异步调用
        AiImageServiceImpl aiImageService = SpringUtils.getBean(getClass());
        aiImageService.drawImageAsync(platform, imageOptions, aiImage);

        // 返回绘图记录
        return AI_IMAGE_CONVERT.convert(aiImage);
    }

    @Async
    public void drawImageAsync(AiPlatform aiPlatform, ImageOptions imageOptions, AiImage aiImage) {
        try {
            ImageModel imageModel = aiImageModelFactory.getImageModel(aiPlatform);
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

            this.lambdaUpdate()
                    .eq(AiImage::getId, aiImage.getId())
                    .set(AiImage::getStatus, AiImageStatus.SUCCESS)
                    .set(AiImage::getFinishTime, LocalDateTime.now())
                    .set(AiImage::getImageUrl, imageUrl)
                    .update();

        } catch (Exception e) {
            log.error("绘图失败 {}", aiImage, e);
            this.lambdaUpdate()
                    .eq(AiImage::getId, aiImage.getId())
                    .set(AiImage::getStatus, AiImageStatus.FAIL)
                    .set(AiImage::getErrorMessage, e.getMessage())
                    .update();
        }
    }

    private ImageOptions buildImageOptions(AiImageDrawDTO aiImageDrawDTO, AiPlatform aiPlatform) {
        // 参数校验
        if (aiPlatform == null) {
            throw bizException(ErrorCode.BAD_REQUEST, "不支持的平台");
        }

        String model = aiImageDrawDTO.getModel();
        if (StrUtil.isBlank(model)) {
            throw bizException(ErrorCode.BAD_REQUEST, "模型不能为空");
        }

        Integer height = aiImageDrawDTO.getHeight();
        if (height == null || height.compareTo(0) <= 0) {
            throw bizException(ErrorCode.BAD_REQUEST, "无效的高度");
        }

        Integer width = aiImageDrawDTO.getWidth();
        if (width == null || width.compareTo(0) <= 0) {
            throw bizException(ErrorCode.BAD_REQUEST, "无效的宽度");
        }

        // 构建参数
        switch (aiPlatform) {
            case TONGYI -> {
                return TongYiImagesOptions.builder()
                        .withModel(model)
                        .withWidth(width)
                        .withHeight(height)
                        .withN(1)
                        .build();
            }
            case ZHIPU -> {
                return ZhiPuAiImageOptions.builder()
                        .withModel(model)
                        .build();
            }
            default -> {
                return null;
            }
        }
    }

    @Override
    public AiImageVO getAiImage(Long id, UserInfo user) {
        AiImage aiImage = this.lambdaQuery()
                .eq(AiImage::getId, id)
                .eq(AiImage::getUserId, user.getUserId())
                .one();
        if (aiImage == null) {
            throw bizException(ErrorCode.NOT_FOUND, "未找到记录");
        }
        return AI_IMAGE_CONVERT.convert(aiImage);
    }

}
