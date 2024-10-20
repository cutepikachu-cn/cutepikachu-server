package cn.cutepikachu.xtimer.model.enums;

import cn.cutepikachu.common.model.BaseEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 定时任务状态枚举
 *
 * @author <a href="https://github.com/cutepikachu-cn">笨蛋皮卡丘</a>
 * @version 1.0
 * @since 2024-08-12 23:37-04
 */
@Getter
@AllArgsConstructor
public enum TimerStatus implements BaseEnum<String> {

    NEW("新建"),
    ENABLE("激活"),
    UNABLE("未激活");

    private final String desc;

}
