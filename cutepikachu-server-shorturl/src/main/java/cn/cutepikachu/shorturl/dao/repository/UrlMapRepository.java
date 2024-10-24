package cn.cutepikachu.shorturl.dao.repository;

import cn.cutepikachu.shorturl.dao.mapper.UrlMapMapper;
import cn.cutepikachu.shorturl.model.entity.UrlMap;
import com.baomidou.mybatisplus.extension.repository.CrudRepository;
import org.springframework.stereotype.Component;

/**
 * @author <a href="https://github.com/cutepikachu-cn">笨蛋皮卡丘</a>
 * @version 1.0
 * @since 2024-10-24 21:14-40
 */
@Component
public class UrlMapRepository extends CrudRepository<UrlMapMapper, UrlMap> {
}
