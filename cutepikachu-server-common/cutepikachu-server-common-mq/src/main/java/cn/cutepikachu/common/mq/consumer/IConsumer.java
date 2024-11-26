package cn.cutepikachu.common.mq.consumer;

import cn.cutepikachu.common.mq.model.IMessage;
import com.rabbitmq.client.Channel;

/**
 * 消息消费者接口
 *
 * @author <a href="https://github.com/cutepikachu-cn">笨蛋皮卡丘</a>
 * @version 1.0
 * @since 2024-11-24 23:51-37
 */
public interface IConsumer<T, MSG extends IMessage<T>> {

    /**
     * 处理消息
     *
     * @param message 消息
     * @param channel 通道
     * @param tag     标签
     */
    void handle(MSG message, Channel channel, long tag);

    /**
     * 处理死信消息
     *
     * @param message 消息
     */
    void handleDeadLetter(MSG message);

}
