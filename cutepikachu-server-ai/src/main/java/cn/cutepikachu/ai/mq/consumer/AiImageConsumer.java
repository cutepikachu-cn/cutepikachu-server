package cn.cutepikachu.ai.mq.consumer;

import cn.cutepikachu.ai.dao.repository.AiImageRepository;
import cn.cutepikachu.ai.factory.AiImageModelFactory;
import cn.cutepikachu.ai.model.enums.AiImageStatus;
import cn.cutepikachu.ai.model.enums.AiPlatform;
import cn.cutepikachu.ai.model.image.entity.AiImage;
import cn.cutepikachu.ai.mq.config.AiImageMQConfiguration;
import cn.cutepikachu.ai.mq.model.AiImageMessage;
import cn.cutepikachu.common.model.biz.entity.FileInfo;
import cn.cutepikachu.common.model.biz.enums.FileBizTag;
import cn.cutepikachu.common.mq.consumer.IConsumer;
import cn.cutepikachu.common.response.BaseResponse;
import cn.cutepikachu.common.response.ErrorCode;
import cn.cutepikachu.inner.biz.FileInnerService;
import cn.cutepikachu.inner.biz.dto.FileSaveDTO;
import cn.hutool.core.codec.Base64;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import com.alibaba.cloud.ai.tongyi.image.TongYiImagesOptions;
import com.rabbitmq.client.Channel;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.image.*;
import org.springframework.ai.zhipuai.ZhiPuAiImageOptions;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.net.URLConnection;
import java.time.LocalDateTime;

import static cn.cutepikachu.common.exception.ExceptionFactory.bizException;

/**
 * @author <a href="https://github.com/cutepikachu-cn">笨蛋皮卡丘</a>
 * @version 1.0
 * @since 2024-11-26 10:06-07
 */
@Slf4j
@Component
public class AiImageConsumer implements IConsumer<AiImage, AiImageMessage> {

    @Resource
    private AiImageModelFactory aiImageModelFactory;

    @Resource
    private AiImageRepository aiImageRepository;

    @Resource
    private FileInnerService fileInnerService;

    @RabbitListener(queues = AiImageMQConfiguration.QUEUE, ackMode = "AUTO")
    @Override
    public void handle(AiImageMessage message, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long tag) {
        try {
            AiImage aiImage = message.getContent();
            ImageModel imageModel = aiImageModelFactory.getImageModel(aiImage.getPlatform());
            ImagePrompt imagePrompt = buildImagePrompt(aiImage);
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

            channel.basicAck(tag, false);
        } catch (Exception e) {
            log.error("Message processing failed, sending to dead letter queue: {}", e.getMessage());
            try {
                channel.basicNack(tag, false, false);
            } catch (Exception ex) {
                if (log.isErrorEnabled()) {
                    log.error("Failed to nack the message: {}", ex.getMessage());
                }
            }
        }
    }

    private ImagePrompt buildImagePrompt(AiImage aiImage) {
        AiPlatform platform = aiImage.getPlatform();
        String model = aiImage.getModel();
        Integer width = aiImage.getWidth();
        Integer height = aiImage.getHeight();

        ImageOptions imageOptions;

        // 构建参数
        switch (platform) {
            case TONGYI -> imageOptions = TongYiImagesOptions.builder()
                    .withModel(model)
                    .withWidth(width)
                    .withHeight(height)
                    .withN(1)
                    .build();
            case ZHIPU -> imageOptions = ZhiPuAiImageOptions.builder()
                    .withModel(model)
                    .build();
            default -> throw bizException(ErrorCode.BAD_REQUEST, "无效的平台");
        }

        String prompt = aiImage.getPrompt();
        return new ImagePrompt(prompt, imageOptions);
    }

    @RabbitListener(queues = AiImageMQConfiguration.DEAD_QUEUE, ackMode = "AUTO")
    @Override
    public void handleDeadLetter(AiImageMessage message) {
        AiImage aiImage = message.getContent();
        aiImageRepository.lambdaUpdate()
                .eq(AiImage::getId, aiImage.getId())
                .set(AiImage::getStatus, AiImageStatus.FAIL)
                .set(AiImage::getErrorMessage, "绘图失败")
                .update();
    }

}
