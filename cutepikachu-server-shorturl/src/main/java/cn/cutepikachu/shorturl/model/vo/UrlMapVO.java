package cn.cutepikachu.shorturl.model.vo;

import cn.cutepikachu.common.model.BaseVO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * 短链接映射表 VO
 *
 * @author <a href="https://github.com/cutepikachu-cn">笨蛋皮卡丘</a>
 * @version 0.0.1-SNAPSHOT
 * @since 2024-09-02 19:12:07
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UrlMapVO extends BaseVO implements Serializable {

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
