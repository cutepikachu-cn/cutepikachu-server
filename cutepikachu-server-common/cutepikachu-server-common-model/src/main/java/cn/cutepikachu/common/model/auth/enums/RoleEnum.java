package cn.cutepikachu.common.model.auth.enums;

import cn.cutepikachu.common.model.BaseEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 角色枚举
 *
 * @author <a href="https://github.com/cutepikachu-cn">笨蛋皮卡丘</a>
 * @version 1.0
 * @since 2024-0-28 17:55:55
 */
@Getter
@AllArgsConstructor
public enum RoleEnum implements BaseEnum<String> {

    SYSTEM("系统管理员"),
    ADMIN("管理员"),
    USER("普通用户");

    private final String desc;

}
