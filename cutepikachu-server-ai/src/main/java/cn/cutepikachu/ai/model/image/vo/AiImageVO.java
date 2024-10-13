package cn.cutepikachu.ai.model.image.vo;

import cn.cutepikachu.ai.model.image.entity.AiImage;
import cn.cutepikachu.common.model.BaseVO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Map;

/**
 * AI 文生图表 VO
 *
 * @author <a href="https://github.com/cutepikachu-cn">笨蛋皮卡丘</a>
 * @version 0.0.1-SNAPSHOT
 * @since 2024-10-12 19:44:03
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AiImageVO extends BaseVO<AiImage, AiImageVO> implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    private Long id;

    /**
     * 用户 id
     */
    private Long userId;

    /**
     * 平台
     */
    private String platform;

    /**
     * 模型
     */
    private String model;

    /**
     * Prompt
     */
    private String prompt;

    /**
     * 高度
     */
    private Integer height;

    /**
     * 宽度
     */
    private Integer width;

    /**
     * 选项
     */
    private Map<String, Object> options;

    /**
     * 状态
     */
    private String status;

    /**
     * 完成时间
     */
    private LocalDateTime finishTime;

    /**
     * 错误信息
     */
    private String errorMessage;

    /**
     * 图片地址
     */
    private String imageUrl;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

}
