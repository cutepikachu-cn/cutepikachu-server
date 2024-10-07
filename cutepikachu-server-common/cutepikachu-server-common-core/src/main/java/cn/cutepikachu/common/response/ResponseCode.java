package cn.cutepikachu.common.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;
import java.util.Arrays;

/**
 * 响应码枚举类
 *
 * @author <a href="https://github.com/cutepikachu-cn">笨蛋皮卡丘</a>
 * @version 1.0
 * @since 2024-0-28 17:55:55
 */
@AllArgsConstructor
@Getter
public enum ResponseCode implements Serializable {

    SUCCESS(0, "成功"),

    // 客户端异常
    BAD_REQUEST(40000, "请求参数错误"),
    UNAUTHORIZED(40100, "未登录"),
    FORBIDDEN(40300, "无权限"),
    NOT_FOUND(40400, "请求数据不存在"),
    METHOD_NOT_ALLOWED(40500, "请求方法错误"),
    TOO_MANY_REQUESTS(42900, "请求过于频繁"),

    // 服务端异常
    INTERNAL_SERVER_ERROR(50000, "系统内部异常"),
    NOT_IMPLEMENTED(50100, "功能未实现"),

    // 自定义异常
    UNKNOWN(99999, "未知错误");

    private static final long serialVersionUID = 1L;

    /**
     * 状态码
     */
    private final Integer code;

    /**
     * 信息
     */
    private final String message;

    /**
     * 根据状态码获取枚举
     *
     * @param code 状态码
     * @return 枚举
     */
    public static ResponseCode getByCode(Integer code) {
        return Arrays.stream(ResponseCode.values())
                .filter(responseCode -> responseCode.getCode().equals(code))
                .findFirst()
                .orElse(null);
    }

}
