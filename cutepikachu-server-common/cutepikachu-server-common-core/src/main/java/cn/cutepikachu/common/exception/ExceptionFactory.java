package cn.cutepikachu.common.exception;

import cn.cutepikachu.common.response.ErrorCode;

/**
 * 异常工厂
 *
 * @author <a href="https://github.com/cutepikachu-cn">笨蛋皮卡丘</a>
 * @version 1.0
 * @since 2024-10-17 09:38-13
 */
public class ExceptionFactory {

    // === BizException ===

    public static BizException bizException(String errorMessage) {
        return new BizException(errorMessage);
    }

    public static BizException bizException(int errorCode, String errorMessage) {
        return new BizException(errorCode, errorMessage);
    }

    public static BizException bizException(ErrorCode errorCode, String errorMessage) {
        return new BizException(errorCode.getCode(), errorMessage);
    }

    public static BizException bizException(ErrorCode errorCode) {
        return new BizException(errorCode.getCode(), errorCode.getMessage());
    }

    // === SysException ===

    public static SysException sysException(String errorMessage) {
        return new SysException(errorMessage);
    }

    public static SysException sysException(int errorCode, String errorMessage) {
        return new SysException(errorCode, errorMessage);
    }

    public static SysException sysException(ErrorCode errorCode, String errorMessage) {
        return new SysException(errorCode.getCode(), errorMessage);
    }

    public static SysException sysException(ErrorCode errorCode) {
        return new SysException(errorCode.getCode(), errorCode.getMessage());
    }

    public static SysException sysException(String errorMessage, Throwable e) {
        return new SysException(errorMessage, e);
    }

    public static SysException sysException(int errorCode, String errorMessage, Throwable e) {
        return new SysException(errorCode, errorMessage, e);
    }

    public static SysException sysException(ErrorCode errorCode, String errorMessage, Throwable e) {
        return new SysException(errorCode.getCode(), errorMessage, e);
    }

    public static SysException sysException(ErrorCode errorCode, Throwable e) {
        return new SysException(errorCode.getCode(), errorCode.getMessage(), e);
    }

}
