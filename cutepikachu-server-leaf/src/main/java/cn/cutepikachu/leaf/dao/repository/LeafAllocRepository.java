package cn.cutepikachu.leaf.dao.repository;

import cn.cutepikachu.leaf.dao.mapper.LeafAllocMapper;
import cn.cutepikachu.leaf.model.LeafAlloc;
import com.baomidou.mybatisplus.extension.repository.CrudRepository;
import org.springframework.stereotype.Component;

/**
 * @author <a href="https://github.com/cutepikachu-cn">笨蛋皮卡丘</a>
 * @version 1.0
 * @since 2024-10-24 21:16-13
 */
@Component
public class LeafAllocRepository extends CrudRepository<LeafAllocMapper, LeafAlloc> {
}
