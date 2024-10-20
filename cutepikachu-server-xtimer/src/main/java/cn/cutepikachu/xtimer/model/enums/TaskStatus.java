package cn.cutepikachu.xtimer.model.enums;

import cn.cutepikachu.common.model.BaseEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 任务状态枚举
 *
 * @author <a href="https://github.com/cutepikachu-cn">笨蛋皮卡丘</a>
 * @version 1.0
 * @since 2024-08-12 23:40-39
 */
@Getter
@AllArgsConstructor
public enum TaskStatus implements BaseEnum<String> {

    NOT_RUN("待执行"),
    RUNNING("正在执行"),
    SUCCESS("执行成功"),
    FAIL("执行失败");

    private final String desc;

}
