package cn.cutepikachu.common.auth.model.enums;

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
public enum RoleEnum implements BaseEnum<Long> {

    SYSTEM("system", 0L),
    USER("user", 1L),
    USER_ADMIN("user_admin", 2L),
    BLOG_ADMIN("blog_admin", 3L);

    private final String text;

    private final Long value;

}
