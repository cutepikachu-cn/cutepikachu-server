package cn.cutepikachu.ai.model.convert;

import cn.cutepikachu.ai.model.image.entity.AiImage;
import cn.cutepikachu.ai.model.image.vo.AiImageVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @author <a href="https://github.com/cutepikachu-cn">笨蛋皮卡丘</a>
 * @version 1.0
 * @since 2024-10-18 17:26-22
 */
@Mapper
public interface AiImageConvert {

    AiImageConvert INSTANCE = Mappers.getMapper(AiImageConvert.class);

    AiImageVO convert(AiImage aiImage);

}
