package cn.cutepikachu.xtimer.service.scheduler;

import cn.cutepikachu.common.response.ErrorCode;
import cn.cutepikachu.xtimer.config.SchedulerConfiguration;
import cn.cutepikachu.xtimer.service.trigger.TriggerWorker;
import cn.cutepikachu.xtimer.util.TimerUtils;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import static cn.cutepikachu.common.exception.ExceptionFactory.sysException;

/**
 * 调度器分片任务
 *
 * @author <a href="https://github.com/cutepikachu-cn">笨蛋皮卡丘</a>
 * @version 1.0
 * @since 2024-08-13 20:49-54
 */
@Slf4j
@Component
public class SchedulerTask {

    @Resource
    private RedissonClient redissonClient;

    @Resource
    private SchedulerConfiguration schedulerConfiguration;

    @Resource
    private TriggerWorker triggerWorker;

    @Async("schedulerPool")
    public void asyncHandleSlice(Date date, int bucketId) {
        log.info("start executeAsync");

        String lockToken = TimerUtils.getToken();
        String timeBucketLockKey = TimerUtils.getTimeBucketLockKey(date, bucketId);

        // 获取分片对应的分布式锁，防止被重复执行
        // time_bucket_lock_yyyy-MM-dd HH:mm_bucketId
        // 只加锁不解锁，只有超时时解锁；超时时间控制频率
        // 锁住横纵向切分后的：单个桶（分钟 + bucketId）
        RLock lock = redissonClient.getLock(timeBucketLockKey);
        try {
            if (!lock.tryLock(0, schedulerConfiguration.getTryLockSeconds(), TimeUnit.SECONDS)) {
                log.info("asyncHandleSlice 获取分布式锁失败");
                return;
            }
        } catch (InterruptedException e) {
            log.info("asyncHandleSlice 获取分布式锁失败");
            throw sysException(ErrorCode.INTERNAL_WARN, e);
        }
        log.info("get scheduler lock success, key: {}", timeBucketLockKey);

        // trigger 触发器调度 bucketId 桶中在当前执行时间范围内的任务
        // zRange 分片键 yyyy-MM-dd HH:mm_bucketId
        String sliceKey = TimerUtils.getSliceMsgKey(date, bucketId);
        triggerWorker.work(sliceKey);

        // 延长分布式锁的时间，避免重复执行分片任务
        lock.lock(schedulerConfiguration.getSuccessExpireSeconds(), TimeUnit.SECONDS);

        log.info("end executeAsync");
    }

}
