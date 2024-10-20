package cn.cutepikachu.xtimer.service.trigger;

import cn.cutepikachu.xtimer.config.TriggerConfiguration;
import cn.cutepikachu.xtimer.model.entity.TimerTask;
import cn.cutepikachu.xtimer.model.enums.TaskStatus;
import cn.cutepikachu.xtimer.service.ITimerTaskService;
import cn.cutepikachu.xtimer.util.TaskCacheUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * 触发器任务
 *
 * @author <a href="https://github.com/cutepikachu-cn">笨蛋皮卡丘</a>
 * @version 1.0
 * @since 2024-08-13 18:06-06
 */
@Slf4j
public class TriggerTimerTask extends java.util.TimerTask {

    TriggerConfiguration triggerConfiguration;

    TriggerPoolTask triggerPoolTask;

    TaskCacheUtils taskCacheUtils;

    ITimerTaskService taskService;

    private final CountDownLatch latch;

    private Long count;

    /**
     * 分片开始时间
     */
    private final Date startTime;

    /**
     * 分片结束时间
     */
    private final Date endTime;

    private final String timeBucketKey;

    public TriggerTimerTask(TriggerConfiguration triggerConfiguration, TriggerPoolTask triggerPoolTask, TaskCacheUtils taskCacheUtils, ITimerTaskService taskService, CountDownLatch latch, Date startTime, Date endTime, String timeBucketKey) {
        this.triggerConfiguration = triggerConfiguration;
        this.triggerPoolTask = triggerPoolTask;
        this.taskCacheUtils = taskCacheUtils;
        this.taskService = taskService;
        this.latch = latch;
        this.count = 0L;
        this.startTime = startTime;
        this.endTime = endTime;
        this.timeBucketKey = timeBucketKey;
    }

    @Override
    public void run() {
        // 当前时间
        Date curStart = new Date(startTime.getTime() + count * triggerConfiguration.getZrangeGapSeconds() * 1000L);
        if (curStart.compareTo(endTime) > 0) {
            // 当前时间大于结束时间，结束 zRange 查询
            latch.countDown();
            return;
        }
        // 处理当前时间到 zrangeGapSeconds 秒内范围内的任务
        try {
            handleTasks(curStart, new Date(curStart.getTime() + triggerConfiguration.getZrangeGapSeconds() * 1000L));
        } catch (Exception e) {
            log.error("handleBatch Error. timeBucketKey: {}, tStartTime: {}, e:", timeBucketKey, startTime, e);
        }
        count++;
    }

    /**
     * 处理任务
     *
     * @param start 开始时间
     * @param end   结束时间
     */
    private void handleTasks(Date start, Date end) {
        // 取出达到执行时间的任务
        List<TimerTask> tasks = getTasksByTime(start, end);
        if (CollectionUtils.isEmpty(tasks)) {
            return;
        }
        // 执行每个任务
        for (TimerTask task : tasks) {
            try {
                if (task == null) {
                    continue;
                }
                // 执行任务
                triggerPoolTask.runExecutor(task);
            } catch (Exception e) {
                log.error("executor run task error, task{}", task);
            }
        }
    }

    /**
     * 获取指定时间范围内的任务
     *
     * @param start 开始时间
     * @param end   结束时间
     * @return 任务列表
     */
    private List<TimerTask> getTasksByTime(Date start, Date end) {
        List<TimerTask> tasks = null;
        // 先从缓存查询
        try {
            tasks = taskCacheUtils.getTasksFromCache(timeBucketKey, start.getTime(), end.getTime());
        } catch (Exception e) {
            log.warn("getTasksFromCache error: ", e);
            // 缓存未命中，拆线呢数据库
            try {
                tasks = taskService.lambdaQuery()
                        .between(TimerTask::getRunTime, start.getTime(), end.getTime() - 1)
                        .eq(TimerTask::getStatus, TaskStatus.NOT_RUN)
                        .list();
            } catch (Exception ei) {
                log.error("getTasksFromDatabase error: ", ei);
            }
        }
        return tasks;
    }

}
