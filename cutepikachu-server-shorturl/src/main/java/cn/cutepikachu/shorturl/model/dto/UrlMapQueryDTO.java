package cn.cutepikachu.shorturl.model.dto;

import cn.cutepikachu.common.request.PageDTO;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 查询 DTO
 *
 * @author <a href="https://github.com/cutepikachu-cn">笨蛋皮卡丘</a>
 * @version 0.0.1-SNAPSHOT
 * @since 2024-09-02 19:12:07
 */
@Data
public class UrlMapQueryDTO extends PageDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 链接 id
     */
    private Long urlId;

    /**
     * 长链接
     */
    private String longUrl;

    /**
     * 短链接
     */
    private String shortUrl;

}
