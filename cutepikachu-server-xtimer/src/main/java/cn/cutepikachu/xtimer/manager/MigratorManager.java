package cn.cutepikachu.xtimer.manager;

import cn.cutepikachu.common.response.ErrorCode;
import cn.cutepikachu.xtimer.config.MigratorConfiguration;
import cn.cutepikachu.xtimer.dao.repository.TimerTaskRepository;
import cn.cutepikachu.xtimer.model.entity.Timer;
import cn.cutepikachu.xtimer.model.entity.TimerTask;
import cn.cutepikachu.xtimer.model.enums.TaskStatus;
import cn.cutepikachu.xtimer.model.enums.TimerStatus;
import cn.cutepikachu.xtimer.util.TaskCacheUtils;
import cn.cutepikachu.xtimer.util.TimerUtils;
import cn.hutool.core.collection.CollUtil;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.quartz.CronExpression;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import static cn.cutepikachu.common.exception.ExceptionFactory.sysException;

/**
 * 迁移管理器
 *
 * @author <a href="https://github.com/cutepikachu-cn">笨蛋皮卡丘</a>
 * @version 1.0
 * @since 2024-08-12 22:02-30
 */
@Component
@Slf4j
public class MigratorManager {

    @Resource
    private TimerTaskRepository taskRepository;

    @Resource
    private MigratorConfiguration migratorConfiguration;

    @Resource
    private TaskCacheUtils taskCacheUtils;

    /**
     * 将未来一段时间（migrateStepMinutes）内的定时任务迁移至数据库及 Redis
     *
     * @param timer 定时任务信息
     */
    public void migrateTimer(Timer timer) {
        // 校验状态定时任务
        // 是否已激活
        if (!Objects.equals(timer.getStatus(), TimerStatus.ENABLE)) {
            throw sysException(ErrorCode.INTERNAL_WARN, "Timer 非 Enable 状态，迁移失败，timerId:" + timer.getTimerId());
        }

        // 批量获取定时任务执行时机
        CronExpression cronExpression;
        try {
            cronExpression = new CronExpression(timer.getCron());
        } catch (ParseException e) {
            throw sysException(ErrorCode.INTERNAL_ERROR, "解析 cron 表达式失败：" + timer.getCron());
        }
        // 获取 [当前时间, 当前时间 + 2 * migrateStepSeconds ] 之间的时间范围
        Date now = new Date();
        Date end = TimerUtils.getForwardTwoMigrateStepEnd(now, migratorConfiguration.getMigrateStepSeconds());
        // 获取定时任务在 [now, end] 时间范围内的任务执行时间
        List<Long> executeTimes = TimerUtils.getCronNextBetween(cronExpression, now, end);
        if (CollUtil.isEmpty(executeTimes)) {
            log.warn("获取执行时机 executeTimes 为空");
            return;
        }

        // 将 [now, end] 时间内将要执行的任务信息加入数据库
        List<TimerTask> taskList = getTasksToExecuted(timer, executeTimes);
        // 基于 timer_id + run_time 唯一键，保证任务不被重复插入
        taskRepository.saveBatch(taskList, taskList.size());

        // 将 [now, end] 时间内将要执行的任务信息加入 Redis
        boolean cacheRes = taskCacheUtils.saveTasksToCache(taskList);
        if (!cacheRes) {
            log.error("Zset 存储 taskList 失败");
            throw sysException(ErrorCode.INTERNAL_ERROR, "ZSet 存储 taskList 失败，timerId: " + timer.getTimerId());
        }
    }

    /**
     * 获取将要执行的任务列表
     *
     * @param timer        定时任务信息
     * @param executeTimes 任务执行时机
     * @return 任务列表
     */
    private List<TimerTask> getTasksToExecuted(Timer timer, List<Long> executeTimes) {
        if (timer == null || CollUtil.isEmpty(executeTimes)) {
            return null;
        }
        List<TimerTask> taskList = new ArrayList<>();
        for (Long runTime : executeTimes) {
            TimerTask task = new TimerTask();
            task.setApp(timer.getApp())
                    .setTimerId(timer.getTimerId())
                    .setRunTime(runTime)
                    .setStatus(TaskStatus.NOT_RUN);
            taskList.add(task);
        }
        return taskList;
    }

}
