package cn.cutepikachu.common.exception;

import cn.cutepikachu.common.response.ResponseCode;
import lombok.Getter;

/**
 * 自定义业务异常类
 *
 * @author <a href="https://github.com/cutepikachu-cn">笨蛋皮卡丘</a>
 * @version 1.0
 * @since 2024-0-28 17:55:55
 */
@Getter
public class BusinessException extends RuntimeException {

    /**
     * 异常码
     */
    private final Integer code;

    public BusinessException(ResponseCode errorCode) {
        super(errorCode.getMessage());
        this.code = errorCode.getCode();
    }

    public BusinessException(ResponseCode errorCode, String message) {
        super(message);
        this.code = errorCode.getCode();
    }

    public BusinessException(Integer code, String message) {
        super(message);
        this.code = code;
    }

}
