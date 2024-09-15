package cn.cutepikachu.common.util;

import cn.cutepikachu.common.exception.BusinessException;
import cn.cutepikachu.common.response.ResponseCode;

/**
 * 抛异常工具
 *
 * @author <a href="https://github.com/cutepikachu-cn">笨蛋皮卡丘</a>
 * @version 1.0
 * @since 2024-0-28 17:55:55
 */
public class ThrowUtils {

    /**
     * 条件成立则抛异常
     *
     * @param condition        抛出条件
     * @param runtimeException 异常对象
     */
    public static void throwIf(boolean condition, RuntimeException runtimeException) {
        if (condition) {
            throw runtimeException;
        }
    }

    /**
     * 条件成立则抛异常
     *
     * @param condition 抛出条件
     * @param errorCode 异常码
     */
    public static void throwIf(boolean condition, ResponseCode errorCode) {
        throwIf(condition, new BusinessException(errorCode));
    }


    /**
     * 条件成立则抛异常
     *
     * @param condition 抛出条件
     * @param errorCode 异常码
     * @param message   异常信息
     */
    public static void throwIf(boolean condition, ResponseCode errorCode, String message) {
        throwIf(condition, new BusinessException(errorCode, message));
    }

    /**
     * 条件成立则抛异常
     *
     * @param condition 抛出条件
     * @param errorCode 异常码
     * @param message   异常信息
     */
    public static void throwIf(boolean condition, Integer errorCode, String message) {
        throwIf(condition, new BusinessException(errorCode, message));
    }

}
