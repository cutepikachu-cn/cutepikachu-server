package cn.cutepikachu.ai.mq.config;

import cn.cutepikachu.common.mq.config.GenericMQConfiguration;
import org.springframework.context.annotation.Configuration;

/**
 * @author <a href="https://github.com/cutepikachu-cn">笨蛋皮卡丘</a>
 * @version 1.0
 * @since 2024-11-26 09:57-08
 */
@Configuration
public class AiImageMQConfiguration implements GenericMQConfiguration {

    public static final String EXCHANGE = "ai-image-exchange";

    public static final String QUEUE = "ai-image-queue";

    public static final String ROUTING_KEY = "ai-image-routing-key";

    public static final String DEAD_EXCHANGE = "ai-image-dead-exchange";

    public static final String DEAD_QUEUE = "ai-image-dead-queue";

    public static final String DEAD_ROUTING_KEY = "ai-image-dead-routing-key";

    @Override
    public String getExchange() {
        return EXCHANGE;
    }

    @Override
    public String getRoutingKey() {
        return ROUTING_KEY;
    }

    @Override
    public String getQueue() {
        return QUEUE;
    }

    @Override
    public String getDeadExchange() {
        return DEAD_EXCHANGE;
    }

    @Override
    public String getDeadRoutingKey() {
        return DEAD_ROUTING_KEY;
    }

    @Override
    public String getDeadQueue() {
        return DEAD_QUEUE;
    }

    @Override
    public boolean getDelayed() {
        return false;
    }

}
