package cn.cutepikachu.shorturl.model.entity;

import cn.cutepikachu.common.model.BaseEntity;
import cn.cutepikachu.shorturl.model.vo.UrlMapVO;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 短链接映射表
 *
 * @author <a href="https://github.com/cutepikachu-cn">笨蛋皮卡丘</a>
 * @version 0.0.1-SNAPSHOT
 * @since 2024-09-02 19:12:07
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "`url_map`", autoResultMap = true)
public class UrlMap extends BaseEntity<UrlMap, UrlMapVO> implements Serializable {

    @Serial
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    /**
     * 链接 id
     */
    @TableId(value = "`url_id`")
    private Long urlId;

    /**
     * 长链接
     */
    @TableField("`long_url`")
    private String longUrl;

    /**
     * 短链接
     */
    @TableField("`short_url`")
    private String shortUrl;

    /**
     * 创建时间
     */
    @TableField(value = "`create_time`", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField(value = "`update_time`", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    public static final String URL_ID = "url_id";

    public static final String LONG_URL = "long_url";

    public static final String SHORT_URL = "short_url";

    public static final String CREATE_TIME = "create_time";

    public static final String UPDATE_TIME = "update_time";

}
