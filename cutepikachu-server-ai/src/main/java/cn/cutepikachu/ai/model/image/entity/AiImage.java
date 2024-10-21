package cn.cutepikachu.ai.model.image.entity;

import cn.cutepikachu.ai.model.enums.AiImageStatus;
import cn.cutepikachu.ai.model.enums.AiPlatform;
import cn.cutepikachu.common.model.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.ibatis.type.EnumTypeHandler;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Map;

/**
 * AI 文生图表
 *
 * @author <a href="https://github.com/cutepikachu-cn">笨蛋皮卡丘</a>
 * @version 0.0.1-SNAPSHOT
 * @since 2024-10-12 19:44:03
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "`ai_image`", autoResultMap = true)
public class AiImage extends BaseEntity implements Serializable {

    @Serial
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId("`id`")
    private Long id;

    /**
     * 用户 id
     */
    @TableField("`user_id`")
    private Long userId;

    /**
     * 平台
     */
    @TableField(value = "`platform`", typeHandler = EnumTypeHandler.class)
    private AiPlatform platform;

    /**
     * 模型
     */
    @TableField("`model`")
    private String model;

    /**
     * Prompt
     */
    @TableField("`prompt`")
    private String prompt;

    /**
     * 高度
     */
    @TableField("`width`")
    private Integer width;

    /**
     * 宽度
     */
    @TableField("`height`")
    private Integer height;

    /**
     * 选项
     */
    @TableField(value = "`options`", typeHandler = JacksonTypeHandler.class)
    private Map<String, Object> options;

    /**
     * 状态
     */
    @TableField("`status`")
    private AiImageStatus status;

    /**
     * 完成时间
     */
    @TableField("`finish_time`")
    private LocalDateTime finishTime;

    /**
     * 错误信息
     */
    @TableField("`error_message`")
    private String errorMessage;

    /**
     * 图片地址
     */
    @TableField("`image_url`")
    private String imageUrl;

    public static final String ID = "id";

    public static final String USER_ID = "user_id";

    public static final String PLATFORM = "platform";

    public static final String MODEL = "model";

    public static final String OPTIONS = "options";

    public static final String STATUS = "status";

    public static final String FINISH_TIME = "finish_time";

    public static final String ERROR_MESSAGE = "error_message";

    public static final String IMAGE_URL = "image_url";

}
