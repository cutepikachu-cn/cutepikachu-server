package cn.cutepikachu.common.mq.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;

/**
 * 消息队列配置接口
 *
 * @author <a href="https://github.com/cutepikachu-cn">笨蛋皮卡丘</a>
 * @version 1.0
 * @since 2024-11-25 10:06-16
 */
public interface GenericMQConfiguration {

    String getExchange();

    String getRoutingKey();

    String getQueue();

    String getDeadExchange();

    String getDeadRoutingKey();

    String getDeadQueue();

    @Bean
    default Queue smsQueue() {
        return QueueBuilder.durable(getQueue())
                .withArgument("x-dead-letter-exchange", getDeadExchange())
                .withArgument("x-dead-letter-routing-key", getDeadRoutingKey())
                .build();
    }

    @Bean
    default Exchange smsExchange() {
        return new DirectExchange(getExchange(), true, false);
    }

    @Bean
    default Binding smsBinding() {
        return BindingBuilder.bind(smsQueue())
                .to(smsExchange())
                .with(getRoutingKey()).noargs();
    }

    @Bean
    default Queue deadSmsQueue() {
        return new Queue(getDeadQueue(), true, false, false);
    }

    @Bean
    default Exchange deadSmsExchange() {
        return new DirectExchange(getDeadExchange(), true, false);
    }

    @Bean
    default Binding deadSmsBinding() {
        return BindingBuilder.bind(deadSmsQueue())
                .to(deadSmsExchange())
                .with(getDeadRoutingKey()).noargs();
    }

}
