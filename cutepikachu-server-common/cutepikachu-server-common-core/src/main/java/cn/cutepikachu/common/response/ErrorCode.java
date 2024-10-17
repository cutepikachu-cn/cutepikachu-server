package cn.cutepikachu.common.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;
import java.util.Arrays;

import static cn.cutepikachu.common.response.ErrorCode.Type.*;

/**
 * 响应码枚举类
 *
 * @author <a href="https://github.com/cutepikachu-cn">笨蛋皮卡丘</a>
 * @version 1.0
 * @since 2024-0-28 17:55:55
 */
@AllArgsConstructor
@Getter
public enum ErrorCode implements Serializable {

    SUCCESS(0, "成功", BIZ),

    // 客户端异常
    BAD_REQUEST(400_00, "请求参数错误", BIZ),
    UNAUTHORIZED(401_00, "未登录", BIZ),
    FORBIDDEN(403_00, "无权限", BIZ),
    NOT_FOUND(404_00, "请求数据不存在", BIZ),
    METHOD_NOT_ALLOWED(405_00, "请求方法错误", BIZ),
    TOO_MANY_REQUESTS(429_00, "请求过于频繁", BIZ),

    // 服务端异常
    INTERNAL_SERVER_ERROR(500_00, "系统内部异常", SYS),
    NOT_IMPLEMENTED(501_00, "功能未实现", SYS),

    // 内部异常
    INTERNAL_ERROR(900_00, "内部错误", SYS),
    INTERNAL_WARN(900_01, "内部警告", SYS),

    // 自定义异常
    UNKNOWN(99999, "未知错误", OTHER);

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
     * 类型
     */
    private final Type type;

    public enum Type {
        BIZ, SYS, OTHER
    }

    public boolean isBiz() {
        return BIZ.equals(type);
    }

    public boolean isSys() {
        return SYS.equals(type);
    }

    /**
     * 根据状态码获取枚举
     *
     * @param code 状态码
     * @return 枚举
     */
    public static ErrorCode getByCode(Integer code) {
        return Arrays.stream(ErrorCode.values())
                .filter(errorCode -> errorCode.getCode().equals(code))
                .findFirst()
                .orElse(UNKNOWN);
    }

}
