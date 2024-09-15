package cn.cutepikachu.biz.model.enums;

import cn.cutepikachu.common.model.BaseEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author <a href="https://github.com/cutepikachu-cn">笨蛋皮卡丘</a>
 * @version 1.0
 * @since 2024-09-13 19:19-56
 */
@Getter
@AllArgsConstructor
public enum FileBizTag implements BaseEnum<String> {
    
    USER_AVATAR("用户头像", "user_avatar");

    private final String text;

    private final String value;

}
