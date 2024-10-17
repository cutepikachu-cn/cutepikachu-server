package cn.cutepikachu.common.response;

import cn.cutepikachu.common.exception.BizException;
import cn.cutepikachu.common.exception.SysException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * 通用响应包装类
 *
 * @author <a href="https://github.com/cutepikachu-cn">笨蛋皮卡丘</a>
 * @version 1.0
 * @since 2024-0-28 17:55:55
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BaseResponse<T> implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 操作是否成功
     */
    private boolean success;

    /**
     * 响应码
     */
    private int code;

    /**
     * 响应数据
     */
    private T data;

    /**
     * 响应信息
     */
    private String message;

    /**
     * 检查是否成功
     * 不成功则抛出对应异常
     */
    public void check() {
        if (this.isSuccess()) {
            return;
        }
        ErrorCode errorCode = ErrorCode.getByCode(this.getCode());
        if (errorCode.isBiz()) {
            throw new BizException(this.getCode(), this.getMessage());
        }
        throw new SysException(this.getCode(), this.getMessage());
    }

    public BaseResponse(boolean success, int code, T data) {
        this(success, code, data, null);
    }

    public BaseResponse(boolean success, int code, String message) {
        this(success, code, null, message);
    }

}
