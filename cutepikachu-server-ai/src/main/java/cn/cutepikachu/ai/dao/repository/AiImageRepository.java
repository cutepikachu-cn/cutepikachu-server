package cn.cutepikachu.ai.dao.repository;

import cn.cutepikachu.ai.dao.mapper.AiImageMapper;
import cn.cutepikachu.ai.model.image.entity.AiImage;
import com.baomidou.mybatisplus.extension.repository.CrudRepository;
import org.springframework.stereotype.Component;

/**
 * @author <a href="https://github.com/cutepikachu-cn">笨蛋皮卡丘</a>
 * @version 1.0
 * @since 2024-10-24 21:26-09
 */
@Component
public class AiImageRepository extends CrudRepository<AiImageMapper, AiImage> {
}
