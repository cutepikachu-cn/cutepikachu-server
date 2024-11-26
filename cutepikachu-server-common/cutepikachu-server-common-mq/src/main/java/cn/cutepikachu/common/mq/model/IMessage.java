package cn.cutepikachu.common.mq.model;

import java.io.Serializable;

/**
 * 消息接口
 *
 * @author <a href="https://github.com/cutepikachu-cn">笨蛋皮卡丘</a>
 * @version 1.0
 * @since 2024-11-24 23:43-18
 */
public interface IMessage<T> extends Serializable {

    /**
     * 获取消息内容
     *
     * @return 消息内容
     */
    T getContent();

}
