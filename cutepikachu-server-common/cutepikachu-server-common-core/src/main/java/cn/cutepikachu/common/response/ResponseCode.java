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
    PARAMS_ERROR(41500, "请求参数错误"),
    NOT_LOGIN_ERROR(40100, "未登录"),
    NOT_AUTH_ERROR(40300, "无权限"),
    NOT_FOUND_ERROR(40400, "请求数据不存在"),
    METHOD_NOT_ALLOW(40500, "请求方法错误"),
    TOO_MANY_REQUESTS(42900, "请求过于频繁"),

    // 服务端异常
    SYSTEM_ERROR(50000, "系统内部异常"),
    OPERATION_ERROR(50001, "操作失败");

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
