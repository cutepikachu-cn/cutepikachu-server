package cn.cutepikachu.common.model.biz.enums;

import cn.cutepikachu.common.model.BaseEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Map;

/**
 * @author <a href="https://github.com/cutepikachu-cn">笨蛋皮卡丘</a>
 * @version 1.0
 * @since 2024-09-13 19:19-56
 */
@Getter
@AllArgsConstructor
public enum FileBizTag implements BaseEnum<String> {

    IMAGE_OTHER("其它图片", "other", "image", Map.of(
            "jpg", "FFD8FF",
            "jpeg", "FFD8FF",
            "png", "89504E47"
    ));

    private final String text;

    private final String value;

    private final String bucket;

    private final Map<String, String> fileMagicNumberMap;

}
