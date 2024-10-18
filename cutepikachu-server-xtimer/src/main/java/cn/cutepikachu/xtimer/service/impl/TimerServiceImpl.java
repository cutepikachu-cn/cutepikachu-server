package cn.cutepikachu.xtimer.service.impl;

import cn.cutepikachu.common.constant.DistributedBizTag;
import cn.cutepikachu.common.response.BaseResponse;
import cn.cutepikachu.common.response.ErrorCode;
import cn.cutepikachu.inner.leaf.DistributedIdInnerService;
import cn.cutepikachu.xtimer.manager.MigratorManager;
import cn.cutepikachu.xtimer.mapper.TimerMapper;
import cn.cutepikachu.xtimer.model.convert.TimerConvert;
import cn.cutepikachu.xtimer.model.dto.TimerCreateDTO;
import cn.cutepikachu.xtimer.model.dto.TimerUpdateDTO;
import cn.cutepikachu.xtimer.model.entity.Timer;
import cn.cutepikachu.xtimer.model.enums.TimerStatus;
import cn.cutepikachu.xtimer.model.vo.TimerVO;
import cn.cutepikachu.xtimer.service.ITimerService;
import cn.cutepikachu.xtimer.util.TimerUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import org.quartz.CronExpression;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static cn.cutepikachu.common.exception.ExceptionFactory.bizException;
import static cn.cutepikachu.common.exception.ExceptionFactory.sysException;

/**
 * 定时任务信息表 服务实现类
 *
 * @author <a href="https://github.com/cutepikachu-cn">笨蛋皮卡丘</a>
 * @version 0.0.1-SNAPSHOT
 * @since 2024-08-12 22:24:49
 */
@Service
public class TimerServiceImpl extends ServiceImpl<TimerMapper, Timer> implements ITimerService {

    @Resource
    private DistributedIdInnerService distributedIdInnerService;

    @Resource
    private RedissonClient redissonClient;

    @Resource
    private MigratorManager migratorManager;

    private static final TimerConvert TIMER_CONVERT = TimerConvert.INSTANCE;

    /**
     * 默认获取锁间隔时间
     */
    private static final int defaultGapSeconds = 3;

    private void verify(Timer timer) {
        // 校验 cron 表达式是否合法
        if (timer.getCron() == null) {
            throw bizException(ErrorCode.BAD_REQUEST, "corn 表达式不能为空");
        }

        boolean isValidCron = CronExpression.isValidExpression(timer.getCron());
        if (!isValidCron) {
            throw bizException(ErrorCode.BAD_REQUEST, "corn 表达式不合法");
        }
    }

    @Override
    public TimerVO createTimer(TimerCreateDTO timerCreateDTO) {
        // 获取创建定时任务的锁
        String lockToken = TimerUtils.getToken();
        String lockKey = TimerUtils.getLockKey(timerCreateDTO.getApp());
        RLock lock = redissonClient.getLock(lockKey);
        try {
            // 不手动解锁，只有超时解锁；超时时间控制频率
            boolean isLocked = lock.tryLock(0, defaultGapSeconds, TimeUnit.SECONDS);
            if (!isLocked) {
                throw bizException(ErrorCode.TOO_MANY_REQUESTS, "操作过于频繁");
            }

            Timer timer = TIMER_CONVERT.convert(timerCreateDTO);
            // 校验参数
            verify(timer);

            // 获取任务分布式 ID
            BaseResponse<Long> resp = distributedIdInnerService.getDistributedID(DistributedBizTag.TIMER);
            resp.check();

            timer.setStatus(0)
                    .setTimerId(resp.getData());

            boolean isSave = this.save(timer);
            if (!isSave) {
                throw sysException(ErrorCode.INTERNAL_SERVER_ERROR, "创建失败");
            }

            return TIMER_CONVERT.convert(timer);
        } catch (InterruptedException e) {
            throw sysException(ErrorCode.INTERNAL_SERVER_ERROR, "创建失败");
        }
    }

    @Override
    public void deleteTimer(String app, Long timerId) {
        // 获取删除定时任务的锁
        String lockToken = TimerUtils.getToken();
        String lockKey = TimerUtils.getLockKey(app);
        RLock lock = redissonClient.getLock(lockKey);
        try {
            // 不手动解锁，只有超时解锁；超时时间控制频率
            boolean isLocked = lock.tryLock(0, defaultGapSeconds, TimeUnit.SECONDS);
            if (!isLocked) {
                throw bizException(ErrorCode.TOO_MANY_REQUESTS, "操作过于频繁");
            }
            boolean isRemove = this.removeById(timerId);
            if (!isRemove) {
                throw sysException(ErrorCode.INTERNAL_SERVER_ERROR, "删除失败");
            }
        } catch (InterruptedException e) {
            throw sysException(ErrorCode.INTERNAL_SERVER_ERROR, "操作失败");
        }
    }

    @Override
    public void updateTimer(TimerUpdateDTO timerUpdateDTO) {
        String app = timerUpdateDTO.getApp();
        Long timerId = timerUpdateDTO.getTimerId();
        Timer timer = lambdaQuery().eq(Timer::getApp, app)
                .eq(Timer::getTimerId, timerId)
                .one();
        if (timer == null) {
            throw bizException(ErrorCode.BAD_REQUEST, "定时任务不存在");
        }

        TIMER_CONVERT.copy(timerUpdateDTO, timer);
        // 校验参数
        verify(timer);

        boolean isUpdate = this.updateById(timer);
        if (!isUpdate) {
            throw sysException(ErrorCode.INTERNAL_SERVER_ERROR, "更新失败");
        }

    }

    @Override
    public TimerVO getTimer(String app, Long timerId) {
        Timer timer = getById(timerId);
        if (timer == null) {
            throw bizException(ErrorCode.NOT_FOUND, "定时任务不存在");
        }
        return TIMER_CONVERT.convert(timer);
    }

    @Override
    public void enableTimer(String app, Long timerId) {
        String lockToken = TimerUtils.getToken();
        String lockKey = TimerUtils.getEnableLockKey(app);
        RLock lock = redissonClient.getLock(lockKey);
        try {
            boolean isLock = lock.tryLock(0, defaultGapSeconds, TimeUnit.SECONDS);
            if (!isLock) {
                throw bizException(ErrorCode.TOO_MANY_REQUESTS, "操作过于频繁");
            }

            // 获取定时任务
            Timer timer = getById(timerId);
            if (timer == null) {
                throw bizException(ErrorCode.NOT_FOUND, "定时任务不存在");
            }

            // 是否已激活
            if (Objects.equals(timer.getStatus(), TimerStatus.ENABLE.getValue())) {
                throw bizException(ErrorCode.BAD_REQUEST, "定时任务已激活");
            }

            // 激活
            timer.setStatus(TimerStatus.ENABLE.getValue());
            boolean isUpdate = this.updateById(timer);
            if (!isUpdate) {
                throw sysException(ErrorCode.INTERNAL_SERVER_ERROR, "激活失败");
            }

            // 进行定时任务迁移工作
            migratorManager.migrateTimer(timer);
        } catch (InterruptedException e) {
            throw sysException(ErrorCode.INTERNAL_SERVER_ERROR, "激活失败");
        }
    }

    @Override
    public void unableTimer(String app, Long timerId) {
        String lockToken = TimerUtils.getToken();
        String lockKey = TimerUtils.getEnableLockKey(app);
        RLock lock = redissonClient.getLock(lockKey);
        try {
            boolean isLock = lock.tryLock(0, defaultGapSeconds, TimeUnit.SECONDS);
            if (!isLock) {
                throw bizException(ErrorCode.TOO_MANY_REQUESTS, "操作过于频繁");
            }

            // 获取定时任务
            Timer timer = getById(timerId);
            if (timer == null) {
                throw bizException(ErrorCode.NOT_FOUND, "定时任务不存在");
            }

            // 是否已去激活
            if (Objects.equals(timer.getStatus(), TimerStatus.UNABLE.getValue())) {
                throw bizException(ErrorCode.BAD_REQUEST, "定时任务已去激活");
            }

            // 去激活
            timer.setStatus(TimerStatus.UNABLE.getValue());
            boolean isUpdate = this.updateById(timer);
            if (!isUpdate) {
                throw sysException(ErrorCode.INTERNAL_SERVER_ERROR, "去激活失败");
            }
        } catch (InterruptedException e) {
            throw sysException(ErrorCode.INTERNAL_SERVER_ERROR, "去激活失败");
        }
    }

    @Override
    public List<TimerVO> getAppTimers(String app) {
        List<TimerVO> timerVOList = lambdaQuery()
                .eq(Timer::getApp, app)
                .list()
                .stream()
                .map(TIMER_CONVERT::convert)
                .toList();
        return timerVOList;
    }
}
