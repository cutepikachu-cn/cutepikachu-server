package cn.cutepikachu.xtimer.service.migrator;

import cn.cutepikachu.common.response.ErrorCode;
import cn.cutepikachu.xtimer.config.MigratorConfiguration;
import cn.cutepikachu.xtimer.manager.MigratorManager;
import cn.cutepikachu.xtimer.model.entity.Timer;
import cn.cutepikachu.xtimer.model.enums.TimerStatus;
import cn.cutepikachu.xtimer.service.ITimerService;
import cn.cutepikachu.xtimer.util.TimerUtils;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static cn.cutepikachu.common.exception.ExceptionFactory.sysException;

/**
 * 任务迁移器（定时生成任务分片）
 *
 * @author <a href="https://github.com/cutepikachu-cn">笨蛋皮卡丘</a>
 * @version 1.0
 * @since 2024-08-13 17:19-51
 */
@Component
@Slf4j
public class MigratorWorker {

    @Resource
    private ITimerService timerService;

    @Resource
    private MigratorConfiguration migratorConfiguration;

    @Resource
    private MigratorManager migratorManager;

    @Resource
    private RedissonClient redissonClient;

    // 测试用 10 秒执行一次
    @Scheduled(fixedRate = 10, timeUnit = TimeUnit.SECONDS) // 60 * 60 * 1000 一小时执行一次
    public void work() {
        log.info("start migrator：{}", LocalDateTime.now());
        Date startHour = getStartHour(new Date());
        String lockToken = TimerUtils.getToken();
        String locKey = TimerUtils.getMigratorLockKey(startHour);
        RLock lock = redissonClient.getLock(locKey);

        try {
            int migrateTryLockSeconds = migratorConfiguration.getMigrateTryLockSeconds();
            if (!lock.tryLock(0, 2L * migrateTryLockSeconds, TimeUnit.SECONDS)) {
                log.warn("migrator get lock failed！{}", locKey);
                return;
            }

            // 迁移
            migrate();

            // 迁移成功，更新锁过期时间
            int migrateSuccessExpireSeconds = migratorConfiguration.getMigrateSuccessExpireSeconds();
            lock.lock(2L * migrateSuccessExpireSeconds, TimeUnit.SECONDS);

        } catch (InterruptedException e) {
            throw sysException(ErrorCode.INTERNAL_ERROR, e);
        }
    }

    private Date getStartHour(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH");
        try {
            return sdf.parse(sdf.format(date));
        } catch (ParseException e) {
            throw sysException(ErrorCode.INTERNAL_ERROR, e);
        }
    }

    private void migrate() {
        // 获取已激活的定时任务
        List<Timer> timers = timerService.lambdaQuery()
                .eq(Timer::getStatus, TimerStatus.ENABLE.getValue())
                .list();

        if (CollectionUtils.isEmpty(timers)) {
            log.info("migrate timers is empty");
            return;
        }

        // 迁移每个定时任务
        for (Timer timer : timers) {
            migratorManager.migrateTimer(timer);
        }
    }

}
