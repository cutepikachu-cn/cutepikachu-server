package cn.cutepikachu.biz.model.dto;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 二维码生成 DTO
 *
 * @author <a href="https://github.com/cutepikachu-cn">笨蛋皮卡丘</a>
 * @version 1.0
 * @since 2024-10-11 20:34-05
 */
@Data
public class QrCodeGenerateDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 二维码内容
     */
    private String content;

    /**
     * 二维码类型
     */
    private String type;

    /**
     * 二维码格式
     */
    private String format;

}
