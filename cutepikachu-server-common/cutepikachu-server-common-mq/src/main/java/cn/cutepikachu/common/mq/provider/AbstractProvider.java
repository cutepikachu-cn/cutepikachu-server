package cn.cutepikachu.common.mq.provider;

import cn.cutepikachu.common.mq.config.GenericMQConfiguration;
import cn.cutepikachu.common.mq.model.IMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.util.concurrent.TimeUnit;

/**
 * 消息提供者抽象类
 *
 * @author <a href="https://github.com/cutepikachu-cn">笨蛋皮卡丘</a>
 * @version 1.0
 * @since 2024-11-24 23:46-10
 */
@Slf4j
public abstract class AbstractProvider<T, MSG extends IMessage<T>> {

    private final RabbitTemplate rabbitTemplate;

    private final String exchange;

    private final String routingKey;

    public AbstractProvider(RabbitTemplate rabbitTemplate, GenericMQConfiguration configuration) {
        this.rabbitTemplate = rabbitTemplate;
        this.exchange = configuration.getExchange();
        this.routingKey = configuration.getRoutingKey();
    }

    /**
     * 发送消息
     *
     * @param message 消息
     */
    public void send(MSG message) {
        log.info("Sending message to exchange: {}, routingKey: {}, message: {}", exchange, routingKey, message);
        rabbitTemplate.convertAndSend(exchange, routingKey, message);
    }

    /**
     * 发送延迟消息
     *
     * @param message  消息
     * @param delay    延迟时间
     * @param timeUnit 时间单位
     */
    public void sendDelay(MSG message, long delay, TimeUnit timeUnit) {
        log.info("Sending delay message to exchange: {}, routingKey: {}, message: {}, delay: {}, timeUnit: {}", exchange, routingKey, message, delay, timeUnit);
        rabbitTemplate.convertAndSend(exchange, routingKey, message, msg -> {
            msg.getMessageProperties().setDelayLong(timeUnit.toMillis(delay));
            return msg;
        });
    }

}
