package cn.cutepikachu.common.model;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 基础 VO 类
 *
 * @author <a href="https://github.com/cutepikachu-cn">笨蛋皮卡丘</a>
 * @version 1.0
 * @since 2024-0-28 17:55:55
 */
@Data
public abstract class BaseVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

}
