package cn.cutepikachu.common.model;

import java.io.Serializable;

/**
 * 基础 Enum 接口
 *
 * @author <a href="https://github.com/cutepikachu-cn">笨蛋皮卡丘</a>
 * @version 1.0
 * @since 2024-0-28 17:55:55
 */
public interface BaseEnum<T extends Serializable> {

    /**
     * @return 获取枚举值描述
     */
    T getDesc();

}
