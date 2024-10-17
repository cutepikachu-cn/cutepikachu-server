package cn.cutepikachu.common.exception;

import cn.cutepikachu.common.response.ErrorCode;

import java.io.Serial;

/**
 * 业务异常
 *
 * @author <a href="https://github.com/cutepikachu-cn">笨蛋皮卡丘</a>
 * @version 1.0
 * @since 2024-10-16 21:27-24
 */
public class BizException extends BaseException {

    @Serial
    private static final long serialVersionUID = 1L;

    private static final int DEFAULT_ERROR_CODE = ErrorCode.UNKNOWN.getCode();

    public BizException(String errorMessage) {
        super(DEFAULT_ERROR_CODE, errorMessage);
    }

    public BizException(int errorCode, String errorMessage) {
        super(errorCode, errorMessage);
    }

    public BizException(String errorMessage, Throwable e) {
        super(DEFAULT_ERROR_CODE, errorMessage, e);
    }

    public BizException(int errorCode, String errorMessage, Throwable e) {
        super(errorCode, errorMessage, e);
    }

}
