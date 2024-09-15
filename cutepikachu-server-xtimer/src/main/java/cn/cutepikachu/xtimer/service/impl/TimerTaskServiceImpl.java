package cn.cutepikachu.xtimer.service.impl;

import cn.cutepikachu.xtimer.mapper.TimerTaskMapper;
import cn.cutepikachu.xtimer.model.entity.TimerTask;
import cn.cutepikachu.xtimer.service.ITimerTaskService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * 任务执行信息 服务实现类
 *
 * @author <a href="https://github.com/cutepikachu-cn">笨蛋皮卡丘</a>
 * @version 0.0.1-SNAPSHOT
 * @since 2024-08-12 22:24:49
 */
@Service
public class TimerTaskServiceImpl extends ServiceImpl<TimerTaskMapper, TimerTask> implements ITimerTaskService {

}
