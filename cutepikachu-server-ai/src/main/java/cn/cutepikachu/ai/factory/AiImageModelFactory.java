package cn.cutepikachu.ai.factory;

import cn.cutepikachu.ai.model.enums.AiPlatform;
import org.springframework.ai.image.ImageModel;

/**
 * AI 图像模型工厂
 *
 * @author <a href="https://github.com/cutepikachu-cn">笨蛋皮卡丘</a>
 * @version 1.0
 * @since 2024-10-12 19:54-37
 */
public interface AiImageModelFactory {

    ImageModel getImageModel(AiPlatform aiPlatform);

}
