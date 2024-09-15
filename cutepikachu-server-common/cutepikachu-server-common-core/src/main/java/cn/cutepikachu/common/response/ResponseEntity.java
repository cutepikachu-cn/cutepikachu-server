package cn.cutepikachu.common.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * 基础响应包装类
 *
 * @author <a href="https://github.com/cutepikachu-cn">笨蛋皮卡丘</a>
 * @version 1.0
 * @since 2024-0-28 17:55:55
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseEntity<T> implements Serializable {

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
     * 响应，响应码
     *
     * @param responseCode 响应码
     */
    public ResponseEntity(ResponseCode responseCode) {
        this(responseCode == ResponseCode.SUCCESS, responseCode.getCode(), null, responseCode.getMessage());
    }

    /**
     * 响应，带数据
     *
     * @param responseCode 响应码
     * @param data         数据
     */
    public ResponseEntity(ResponseCode responseCode, T data) {
        this(responseCode == ResponseCode.SUCCESS, responseCode.getCode(), data, responseCode.getMessage());
    }

    /**
     * 响应，带信息
     *
     * @param responseCode 响应码
     * @param message      信息
     */
    public ResponseEntity(ResponseCode responseCode, String message) {
        this(responseCode == ResponseCode.SUCCESS, responseCode.getCode(), null, message);
    }

    /**
     * 响应，带信息和数据
     *
     * @param responseCode 响应码
     * @param data         数据
     * @param message      信息
     */
    public ResponseEntity(ResponseCode responseCode, T data, String message) {
        this(responseCode == ResponseCode.SUCCESS, responseCode.getCode(), data, message);
    }

    /**
     * 响应，带自定响应码和信息
     *
     * @param responseCode 响应码
     * @param message      信息
     */
    public ResponseEntity(int responseCode, String message) {
        this(responseCode == 0, responseCode, null, message);
    }

}
