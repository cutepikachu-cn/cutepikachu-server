package cn.cutepikachu.xtimer.service;

import cn.cutepikachu.xtimer.model.dto.TimerCreateDTO;
import cn.cutepikachu.xtimer.model.dto.TimerUpdateDTO;
import cn.cutepikachu.xtimer.model.entity.Timer;
import cn.cutepikachu.xtimer.model.vo.TimerVO;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 定时任务信息表 服务类
 *
 * @author <a href="https://github.com/cutepikachu-cn">笨蛋皮卡丘</a>
 * @version 0.0.1-SNAPSHOT
 * @since 2024-08-12 22:24:49
 */
public interface ITimerService extends IService<Timer> {

    @Transactional(rollbackFor = Exception.class)
    TimerVO createTimer(TimerCreateDTO timerCreateDTO);

    @Transactional(rollbackFor = Exception.class)
    void deleteTimer(String app, Long timerId);

    @Transactional(rollbackFor = Exception.class)
    void updateTimer(TimerUpdateDTO timerDTO);

    TimerVO getTimer(String app, Long timerId);

    @Transactional(rollbackFor = Exception.class)
    void enableTimer(String app, Long timerId);

    @Transactional(rollbackFor = Exception.class)
    void unableTimer(String app, Long timerId);

    List<TimerVO> getAppTimers(String app);

}
