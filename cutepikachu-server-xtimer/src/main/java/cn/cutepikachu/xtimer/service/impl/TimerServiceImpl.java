package cn.cutepikachu.xtimer.service.impl;

import cn.cutepikachu.common.constant.DistributedBizTag;
import cn.cutepikachu.common.exception.BusinessException;
import cn.cutepikachu.common.response.ResponseCode;
import cn.cutepikachu.common.response.ResponseEntity;
import cn.cutepikachu.common.util.BeanUtils;
import cn.cutepikachu.common.util.ResponseUtils;
import cn.cutepikachu.common.util.ThrowUtils;
import cn.cutepikachu.inner.leaf.DistributedIDInnerService;
import cn.cutepikachu.xtimer.manager.MigratorManager;
import cn.cutepikachu.xtimer.mapper.TimerMapper;
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
    private DistributedIDInnerService distributedIDInnerService;

    @Resource
    private RedissonClient redissonClient;

    @Resource
    private MigratorManager migratorManager;

    /**
     * 默认获取锁间隔时间
     */
    private static final int defaultGapSeconds = 3;

    private void verify(Timer timer) {
        // 校验 cron 表达式是否合法
        ThrowUtils.throwIf(timer.getCron() == null, new BusinessException(ResponseCode.BAD_REQUEST, "corn 表达式不能为空"));
        boolean isValidCron = CronExpression.isValidExpression(timer.getCron());
        ThrowUtils.throwIf(!isValidCron, new BusinessException(ResponseCode.BAD_REQUEST, "corn 表达式不合法"));
    }

    @Override
    public TimerVO createTimer(TimerCreateDTO timerCreateDTO) {
        // 获取创建定时任务的锁
        String lockToken = TimerUtils.getToken();
        String lockKey = TimerUtils.getLockKey(timerCreateDTO.getApp());
        RLock lock = redissonClient.getLock(lockKey);
        try {
            // 不手动解锁，只有超时解锁；超时时间控制频率
            if (!lock.tryLock(0, defaultGapSeconds, TimeUnit.SECONDS)) {
                throw new BusinessException(ResponseCode.TOO_MANY_REQUESTS, "操作过于频繁");
            }

            Timer timer = timerCreateDTO.toEntity();
            // 校验参数
            verify(timer);

            // 获取任务分布式 ID
            ResponseEntity<Long> resp = distributedIDInnerService.getDistributedID(DistributedBizTag.TIMER);
            ResponseUtils.throwIfNotSuccess(resp);

            timer.setStatus(0)
                    .setTimerId(resp.getData());

            ThrowUtils.throwIf(!this.save(timer), new BusinessException(ResponseCode.INTERNAL_SERVER_ERROR, "创建失败"));

            return timer.toVO(TimerVO.class);
        } catch (InterruptedException e) {
            throw new BusinessException(ResponseCode.INTERNAL_SERVER_ERROR, "创建失败");
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
            if (!lock.tryLock(0, defaultGapSeconds, TimeUnit.SECONDS)) {
                throw new BusinessException(ResponseCode.TOO_MANY_REQUESTS, "操作过于频繁");
            }
            ThrowUtils.throwIf(!this.removeById(timerId), new BusinessException(ResponseCode.INTERNAL_SERVER_ERROR, "删除失败"));
        } catch (InterruptedException e) {
            throw new BusinessException(ResponseCode.INTERNAL_SERVER_ERROR, "操作失败");
        }
    }

    @Override
    public void updateTimer(TimerUpdateDTO timerUpdateDTO) {
        String app = timerUpdateDTO.getApp();
        Long timerId = timerUpdateDTO.getTimerId();
        Timer timer = lambdaQuery().eq(Timer::getApp, app)
                .eq(Timer::getTimerId, timerId)
                .one();
        ThrowUtils.throwIf(timer == null, new BusinessException(ResponseCode.BAD_REQUEST, "定时任务不存在"));

        BeanUtils.copyProperties(timerUpdateDTO, timer);
        // 校验参数
        verify(timer);

        ThrowUtils.throwIf(!this.updateById(timer), new BusinessException(ResponseCode.INTERNAL_SERVER_ERROR, "更新失败定时任务失败"));

    }

    @Override
    public TimerVO getTimer(String app, Long timerId) {
        Timer timer = getById(timerId);
        ThrowUtils.throwIf(timer == null, new BusinessException(ResponseCode.NOT_FOUND, "定时任务不存在"));
        return timer.toVO(TimerVO.class);
    }

    @Override
    public void enableTimer(String app, Long timerId) {
        String lockToken = TimerUtils.getToken();
        String lockKey = TimerUtils.getEnableLockKey(app);
        RLock lock = redissonClient.getLock(lockKey);
        try {
            if (!lock.tryLock(0, defaultGapSeconds, TimeUnit.SECONDS)) {
                throw new BusinessException(ResponseCode.TOO_MANY_REQUESTS, "操作过于频繁");
            }
            // 获取定时任务
            Timer timer = getById(timerId);
            ThrowUtils.throwIf(timer == null, new BusinessException(ResponseCode.NOT_FOUND, "定时任务不存在"));

            // 是否已激活
            if (Objects.equals(timer.getStatus(), TimerStatus.ENABLE.getValue())) {
                throw new BusinessException(ResponseCode.BAD_REQUEST, "定时任务已激活");
            }

            // 激活
            timer.setStatus(TimerStatus.ENABLE.getValue());
            ThrowUtils.throwIf(!this.updateById(timer), new BusinessException(ResponseCode.INTERNAL_SERVER_ERROR, "激活失败"));

            // 进行定时任务迁移工作
            migratorManager.migrateTimer(timer);
        } catch (InterruptedException e) {
            throw new BusinessException(ResponseCode.INTERNAL_SERVER_ERROR, "激活失败");
        }
    }

    @Override
    public void unableTimer(String app, Long timerId) {
        String lockToken = TimerUtils.getToken();
        String lockKey = TimerUtils.getEnableLockKey(app);
        RLock lock = redissonClient.getLock(lockKey);
        try {
            if (!lock.tryLock(0, defaultGapSeconds, TimeUnit.SECONDS)) {
                throw new BusinessException(ResponseCode.TOO_MANY_REQUESTS, "操作过于频繁");
            }
            // 获取定时任务
            Timer timer = getById(timerId);
            ThrowUtils.throwIf(timer == null, new BusinessException(ResponseCode.NOT_FOUND, "定时任务不存在"));

            // 是否已去激活
            if (Objects.equals(timer.getStatus(), TimerStatus.UNABLE.getValue())) {
                throw new BusinessException(ResponseCode.BAD_REQUEST, "定时任务已去激活");
            }

            // 去激活
            timer.setStatus(TimerStatus.UNABLE.getValue());
            ThrowUtils.throwIf(!this.updateById(timer), new BusinessException(ResponseCode.INTERNAL_SERVER_ERROR, "去激活失败"));
        } catch (InterruptedException e) {
            throw new BusinessException(ResponseCode.INTERNAL_SERVER_ERROR, "去激活失败");
        }
    }

    @Override
    public List<TimerVO> getAppTimers(String app) {
        List<TimerVO> timerVOList = lambdaQuery()
                .eq(Timer::getApp, app)
                .list()
                .stream()
                .map(timer -> timer.toVO(TimerVO.class))
                .toList();
        return timerVOList;
    }
}
