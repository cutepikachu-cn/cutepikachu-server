package cn.cutepikachu.xtimer.dao.repository;

import cn.cutepikachu.xtimer.dao.mapper.TimerTaskMapper;
import cn.cutepikachu.xtimer.model.entity.TimerTask;
import com.baomidou.mybatisplus.extension.repository.CrudRepository;
import org.springframework.stereotype.Component;

/**
 * @author <a href="https://github.com/cutepikachu-cn">笨蛋皮卡丘</a>
 * @version 1.0
 * @since 2024-10-24 16:43-15
 */
@Component
public class TimerTaskRepository extends CrudRepository<TimerTaskMapper, TimerTask> {
}
