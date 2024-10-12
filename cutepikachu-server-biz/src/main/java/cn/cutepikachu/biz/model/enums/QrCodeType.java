package cn.cutepikachu.biz.model.enums;

import cn.cutepikachu.common.model.BaseEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 二维码类型枚举
 *
 * @author <a href="https://github.com/cutepikachu-cn">笨蛋皮卡丘</a>
 * @version 1.0
 * @since 2024-10-12 10:24-10
 */
@Getter
@AllArgsConstructor
public enum QrCodeType implements BaseEnum<String> {

    IMAGE("image", "image"),
    BASE64("base64", "base64");

    private final String text;

    private final String value;

}
