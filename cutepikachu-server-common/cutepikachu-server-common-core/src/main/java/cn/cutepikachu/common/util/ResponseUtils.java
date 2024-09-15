package cn.cutepikachu.common.util;

import cn.cutepikachu.common.exception.BusinessException;
import cn.cutepikachu.common.response.ResponseCode;
import cn.cutepikachu.common.response.ResponseEntity;

/**
 * 响应工具
 *
 * @author <a href="https://github.com/cutepikachu-cn">笨蛋皮卡丘</a>
 * @version 1.0
 * @since 2024-0-28 17:55:55
 */
public class ResponseUtils {

    /**
     * 如果响应码不是成功，则抛出异常
     *
     * @param responseEntity 响应对象
     */
    public static void throwIfNotSuccess(ResponseEntity<?> responseEntity) {
        if (responseEntity.isSuccess()) {
            return;
        }
        ResponseCode responseCode = ResponseCode.getByCode(responseEntity.getCode());
        ThrowUtils.throwIf(responseCode != null, new BusinessException(responseCode, responseEntity.getMessage()));
    }

    /**
     * 成功
     *
     * @return 响应对象
     */
    public static ResponseEntity<?> success() {
        return new ResponseEntity<>(ResponseCode.SUCCESS);
    }

    /**
     * 成功，带数据
     *
     * @param data 数据
     * @param <T>  数据类型
     * @return 响应对象
     */
    public static <T> ResponseEntity<T> success(T data) {
        return new ResponseEntity<>(ResponseCode.SUCCESS, data);
    }

    /**
     * 成功，带信息
     *
     * @return 响应对象
     */
    public static ResponseEntity<?> success(String message) {
        return new ResponseEntity<>(ResponseCode.SUCCESS, message);
    }

    /**
     * 成功，带数据和信息
     *
     * @param message 信息
     * @param data    数据
     * @param <T>     数据类型
     * @return 响应对象
     */
    public static <T> ResponseEntity<T> success(String message, T data) {
        return new ResponseEntity<>(ResponseCode.SUCCESS, data, message);
    }

    /**
     * 失败
     *
     * @param responseCode 响应码
     * @return 响应对象
     */
    public static ResponseEntity<?> error(ResponseCode responseCode) {
        return new ResponseEntity<>(responseCode);
    }

    /**
     * 失败，带信息
     *
     * @param responseCode 响应码
     * @param message      信息
     * @return 响应对象
     */
    public static ResponseEntity<?> error(ResponseCode responseCode, String message) {
        return new ResponseEntity<>(responseCode, message);
    }

    /**
     * 失败，带自定响应码和信息
     *
     * @param responseCode 响应码
     * @param message      信息
     * @return 响应对象
     */
    public static ResponseEntity<?> error(Integer responseCode, String message) {
        return new ResponseEntity<>(responseCode, message);
    }

}
