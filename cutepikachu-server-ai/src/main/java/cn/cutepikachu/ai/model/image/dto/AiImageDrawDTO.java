package cn.cutepikachu.ai.model.image.dto;

import cn.cutepikachu.ai.model.enums.AiPlatform;
import lombok.Data;

import java.util.Map;

/**
 * AI 文生图表 DTO
 *
 * @author <a href="https://github.com/cutepikachu-cn">笨蛋皮卡丘</a>
 * @version 1.0
 * @since 2024-10-12 18:21-30
 */
@Data
public class AiImageDrawDTO {

    /**
     * AI 平台
     */
    private AiPlatform platform;

    /**
     * 提示词
     */
    private String prompt;

    /**
     * 模型
     */
    private String model;

    /**
     * 图片高度
     */
    private Integer height;

    /**
     * 图片宽度
     */
    private Integer width;

    /**
     * 绘制参数
     */
    private Map<String, Object> options;

}
