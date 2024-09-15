package cn.cutepikachu.xtimer.service.trigger;

import cn.cutepikachu.xtimer.model.entity.TimerTask;
import cn.cutepikachu.xtimer.service.executor.ExecutorWorker;
import cn.cutepikachu.xtimer.util.TimerUtils;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * @author <a href="https://github.com/cutepikachu-cn">笨蛋皮卡丘</a>
 * @version 1.0
 * @since 2024-08-13 18:06-11
 */
@Slf4j
@Component
public class TriggerPoolTask {

    @Resource
    private ExecutorWorker executorWorker;

    // 使用触发器线程池执行任务
    @Async("triggerPool")
    public void runExecutor(TimerTask task) {
        if (task == null) {
            return;
        }
        log.info("start runExecutor");

        executorWorker.work(TimerUtils.unionTimerIDUnix(task.getTimerId(), task.getRunTime()));

        log.info("end executeAsync");
    }

}
