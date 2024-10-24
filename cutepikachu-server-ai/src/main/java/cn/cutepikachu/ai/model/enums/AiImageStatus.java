package cn.cutepikachu.ai.model.enums;

import cn.cutepikachu.common.model.BaseEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * AI 绘图状态枚举
 *
 * @author <a href="https://github.com/cutepikachu-cn">笨蛋皮卡丘</a>
 * @version 1.0
 * @since 2024-10-12 21:15-33
 */
@Getter
@AllArgsConstructor
public enum AiImageStatus implements BaseEnum<String> {

    IN_PROGRESS("生成中"),
    SUCCESS("成功"),
    FAIL("失败");

    private final String desc;

}
