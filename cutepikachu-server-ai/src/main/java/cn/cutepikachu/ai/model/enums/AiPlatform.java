package cn.cutepikachu.ai.model.enums;

import cn.cutepikachu.common.model.BaseEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * AI 平台枚举
 *
 * @author <a href="https://github.com/cutepikachu-cn">笨蛋皮卡丘</a>
 * @version 1.0
 * @since 2024-10-12 19:49-16
 */
@Getter
@AllArgsConstructor
public enum AiPlatform implements BaseEnum<String> {

    TONGYI("通义"),
    ZHIPU("智谱");

    private final String desc;

}
