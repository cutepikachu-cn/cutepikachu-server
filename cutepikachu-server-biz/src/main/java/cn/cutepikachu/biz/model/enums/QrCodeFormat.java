package cn.cutepikachu.biz.model.enums;

import cn.cutepikachu.common.model.BaseEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 二维码格式枚举
 *
 * @author <a href="https://github.com/cutepikachu-cn">笨蛋皮卡丘</a>
 * @version 1.0
 * @since 2024-10-12 10:01-07
 */
@Getter
@AllArgsConstructor
public enum QrCodeFormat implements BaseEnum<String> {

    JPG("jpg", "jpg", "attachment; filename=\"qrcode.jpg\""),
    PNG("png", "png", "attachment; filename=\"qrcode.png\""),
    BASE64("base64", "base64", null);

    private final String text;

    private final String value;

    private final String contentDisposition;

}
