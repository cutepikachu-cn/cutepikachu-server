package cn.cutepikachu.common.util;

import static cn.cutepikachu.common.constant.CommonConstant.DEFAULT_COPY_OPTIONS;

/**
 * @author <a href="https://github.com/cutepikachu-cn">笨蛋皮卡丘</a>
 * @version 1.0
 * @since 2024-08-03 13:50-41
 */
public class BeanUtils {

    public static void copyProperties(Object source, Object target) {
        cn.hutool.core.bean.BeanUtil.copyProperties(source, target, DEFAULT_COPY_OPTIONS);
    }

    public static <T> T copyProperties(Object source, Class<T> targetClass) {
        return cn.hutool.core.bean.BeanUtil.copyProperties(source, targetClass);
    }

}
