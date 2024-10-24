package cn.cutepikachu.ai.dao.mapper;

import cn.cutepikachu.ai.model.image.entity.AiImage;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * AI 文生图表 Mapper 接口
 *
 * @author <a href="https://github.com/cutepikachu-cn">笨蛋皮卡丘</a>
 * @version 0.0.1-SNAPSHOT
 * @since 2024-10-12 19:44:03
 */
@Mapper
public interface AiImageMapper extends BaseMapper<AiImage> {

}
