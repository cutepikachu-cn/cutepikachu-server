package cn.cutepikachu.common.model.auth.enums;

import cn.cutepikachu.common.model.BaseEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author <a href="https://github.com/cutepikachu-cn">笨蛋皮卡丘</a>
 * @version 1.0
 * @since 2024-10-20 22:28-02
 */
@Getter
@AllArgsConstructor
public enum AuthStatus implements BaseEnum<String> {

    ENABLE("启用"),
    DISABLE("禁用");

    private final String desc;

}
