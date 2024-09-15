package cn.cutepikachu.xtimer.service.scheduler;

import cn.cutepikachu.xtimer.config.SchedulerConfiguration;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * 定时任务调度器
 *
 * @author <a href="https://github.com/cutepikachu-cn">笨蛋皮卡丘</a>
 * @version 1.0
 * @since 2024-08-13 18:04-55
 */
@Slf4j
@Component
public class SchedulerWorker {

    @Resource
    private SchedulerTask schedulerTask;

    @Resource
    private SchedulerConfiguration schedulerConfiguration;

    /**
     * 每 1s 进行一次轮询
     */
    @Scheduled(fixedRate = 1000)
    public void scheduledTask() {
        log.info("任务执行时间：{}", LocalDateTime.now());
        handleSlices();
    }

    /**
     * 处理所有桶
     */
    private void handleSlices() {
        for (int i = 0; i < schedulerConfiguration.getBucketsNum(); i++) {
            handleSlice(i);
        }
    }

    private void handleSlice(int bucketId) {
        Date now = new Date();
        Date nowPreMin = new Date(now.getTime() - schedulerConfiguration.getSuccessExpireSeconds());

        // 兜底重试：多执行一次前一分钟的批次
        // 若已经成功，则本次不会获取锁成功：前一次执行成功后会延长锁过期时间，抢不到锁
        // 若前一次执行失败，则本次会获取锁成功，进行重试
        try {
            schedulerTask.asyncHandleSlice(nowPreMin, bucketId);
        } catch (Exception e) {
            log.error("[handle slice] submit nowPreMin task failed, err: ", e);
        }

        // 执行当前时间的批次
        try {
            schedulerTask.asyncHandleSlice(now, bucketId);
        } catch (Exception e) {
            log.error("[handle slice] submit now task failed, err: ", e);
        }

    }

}
