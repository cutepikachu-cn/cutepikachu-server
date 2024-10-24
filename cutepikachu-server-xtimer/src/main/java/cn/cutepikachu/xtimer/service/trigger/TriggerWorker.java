package cn.cutepikachu.xtimer.service.trigger;

import cn.cutepikachu.xtimer.config.TriggerConfiguration;
import cn.cutepikachu.xtimer.dao.repository.TimerTaskRepository;
import cn.cutepikachu.xtimer.util.TaskCacheUtils;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.CountDownLatch;

/**
 * @author <a href="https://github.com/cutepikachu-cn">笨蛋皮卡丘</a>
 * @version 1.0
 * @since 2024-08-13 18:05-58
 */
@Component
@Slf4j
public class TriggerWorker {

    @Resource
    private TriggerConfiguration triggerConfiguration;

    @Resource
    private TriggerPoolTask triggerPoolTask;

    @Resource
    private TaskCacheUtils taskCacheUtils;

    @Resource
    private TimerTaskRepository taskRepository;

    public void work(String timeBucketKey) {
        // 进行为时一分钟的 zRange
        Date startTime = getStartTime(timeBucketKey);
        Date endTime = new Date(startTime.getTime() + 60000);

        // 创建 Java 计时器任务
        CountDownLatch latch = new CountDownLatch(1);
        java.util.Timer timer = new java.util.Timer("Timer");
        TriggerTimerTask task = new TriggerTimerTask(triggerConfiguration, triggerPoolTask, taskCacheUtils, taskRepository, latch, startTime, endTime, timeBucketKey);
        // 开始调度分片中达到执行时间的任务
        // 每 zrangeGapSeconds 秒扫描一次分片中的任务，取出达到执行时间的任务执行
        timer.scheduleAtFixedRate(task, 0L, triggerConfiguration.getZrangeGapSeconds() * 1000L);
        try {
            latch.await();
        } catch (InterruptedException e) {
            log.error("执行 TriggerTimerTask 异常中断，task: {}", task);
        } finally {
            timer.cancel();
        }
    }

    /**
     * 获取 zRange 键 时间_桶索引 获取该分片任务的开始时间
     *
     * @param timeBucketKey zRange 键 时间_桶索引
     * @return 该分片任务的开始时间
     */
    private Date getStartTime(String timeBucketKey) {
        String[] timeBucket = timeBucketKey.split("_");
        if (timeBucket.length != 2) {
            log.error("TriggerWorker getStartTime 错误");
            return null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date startMinute = null;
        try {
            startMinute = sdf.parse(timeBucket[0]);
        } catch (ParseException e) {
            log.error("TriggerWorker getStartTime 错误");
        }
        return startMinute;
    }

}
