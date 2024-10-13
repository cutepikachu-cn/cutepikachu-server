package cn.cutepikachu.ai.factory;

import cn.cutepikachu.ai.model.enums.AiPlatform;
import cn.cutepikachu.common.util.SpringUtils;
import com.alibaba.cloud.ai.tongyi.image.TongYiImagesModel;
import org.springframework.ai.image.ImageModel;
import org.springframework.ai.zhipuai.ZhiPuAiImageModel;
import org.springframework.stereotype.Component;

/**
 * AI 图像模型工厂
 *
 * @author <a href="https://github.com/cutepikachu-cn">笨蛋皮卡丘</a>
 * @version 1.0
 * @since 2024-10-12 20:15-20
 */
@Component
public class AiImageModelFactoryImpl implements AiImageModelFactory {

    @Override
    public ImageModel getImageModel(AiPlatform aiPlatform) {
        switch (aiPlatform) {
            case TONGTYI -> {
                return SpringUtils.getBean(TongYiImagesModel.class);
            }
            case ZHIPU -> {
                return SpringUtils.getBean(ZhiPuAiImageModel.class);
            }
            default -> throw new IllegalArgumentException("不支持的平台");
        }
    }

}
