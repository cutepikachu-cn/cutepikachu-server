package cn.cutepikachu.ai.mq.provider;

import cn.cutepikachu.ai.model.image.entity.AiImage;
import cn.cutepikachu.ai.mq.config.AiImageMQConfiguration;
import cn.cutepikachu.ai.mq.model.AiImageMessage;
import cn.cutepikachu.common.mq.provider.AbstractProvider;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

/**
 * @author <a href="https://github.com/cutepikachu-cn">笨蛋皮卡丘</a>
 * @version 1.0
 * @since 2024-11-26 09:54-32
 */
@Component
public class AiImageProvider extends AbstractProvider<AiImage, AiImageMessage> {

    public AiImageProvider(RabbitTemplate rabbitTemplate, AiImageMQConfiguration aiImageMQConfiguration) {
        super(rabbitTemplate, aiImageMQConfiguration);
    }

}
