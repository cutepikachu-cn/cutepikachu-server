package cn.cutepikachu.shorturl.model.dto;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 短链接映射表 创建 DTO
 *
 * @author <a href="https://github.com/cutepikachu-cn">笨蛋皮卡丘</a>
 * @version 0.0.1-SNAPSHOT
 * @since 2024-09-02 19:12:07
 */
@Data
public class UrlMapCreateDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 长链接
     */
    private String longUrl;

}
