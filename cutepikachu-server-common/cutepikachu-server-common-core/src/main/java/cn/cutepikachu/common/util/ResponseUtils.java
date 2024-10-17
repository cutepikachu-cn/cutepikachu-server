package cn.cutepikachu.common.util;

import cn.cutepikachu.common.response.BaseResponse;
import cn.cutepikachu.common.response.ErrorCode;

import static cn.cutepikachu.common.response.ErrorCode.SUCCESS;

/**
 * 响应工具
 *
 * @author <a href="https://github.com/cutepikachu-cn">笨蛋皮卡丘</a>
 * @version 1.0
 * @since 2024-0-28 17:55:55
 */
public class ResponseUtils {

    /**
     * 成功
     *
     * @return 响应对象
     */
    public static BaseResponse<?> success() {
        return new BaseResponse<>(true, SUCCESS.getCode(), SUCCESS.getMessage());
    }

    /**
     * 成功，带数据
     *
     * @param data 数据
     * @param <T>  数据类型
     * @return 响应对象
     */
    public static <T> BaseResponse<T> success(T data) {
        return new BaseResponse<>(true, SUCCESS.getCode(), data);
    }

    /**
     * 成功，带信息
     *
     * @return 响应对象
     */
    public static BaseResponse<?> success(String message) {
        return new BaseResponse<>(true, SUCCESS.getCode(), message);
    }

    /**
     * 成功，带数据和信息
     *
     * @param message 信息
     * @param data    数据
     * @param <T>     数据类型
     * @return 响应对象
     */
    public static <T> BaseResponse<T> success(String message, T data) {
        return new BaseResponse<>(true, SUCCESS.getCode(), data, message);
    }

    /**
     * 失败
     *
     * @param errorCode 响应码
     * @param message   信息
     * @return 响应对象
     */
    public static BaseResponse<?> error(int errorCode, String message) {
        return new BaseResponse<>(false, errorCode, message);
    }

    /**
     * 失败
     *
     * @param errorCode 响应码
     * @param message   信息
     * @return 响应对象
     */
    public static BaseResponse<?> error(ErrorCode errorCode, String message) {
        return new BaseResponse<>(false, errorCode.getCode(), message);
    }

    /**
     * 失败
     *
     * @param errorCode 响应码
     * @return 响应对象
     */
    public static BaseResponse<?> error(ErrorCode errorCode) {
        return new BaseResponse<>(false, errorCode.getCode(), errorCode.getMessage());
    }

}
