package cn.cutepikachu.common.model.enums;

import cn.cutepikachu.common.model.BaseEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 排序规则枚举
 *
 * @author <a href="https://github.com/cutepikachu-cn">笨蛋皮卡丘</a>
 * @version 1.0
 * @since 2024-0-28 17:55:55
 */
@Getter
@AllArgsConstructor
public enum SortOrderEnum implements BaseEnum<String> {

    ASCENDING("升序"),
    DESCENDING("降序"),
    UNSORTED("不排序");

    private final String desc;

}
