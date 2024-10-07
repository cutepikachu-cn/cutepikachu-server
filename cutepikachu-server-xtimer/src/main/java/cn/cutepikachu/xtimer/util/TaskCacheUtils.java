package cn.cutepikachu.xtimer.util;

import cn.cutepikachu.common.exception.BusinessException;
import cn.cutepikachu.common.response.ResponseCode;
import cn.cutepikachu.xtimer.config.SchedulerConfiguration;
import cn.cutepikachu.xtimer.model.entity.TimerTask;
import cn.hutool.core.collection.CollUtil;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * @author <a href="https://github.com/cutepikachu-cn">笨蛋皮卡丘</a>
 * @version 1.0
 * @since 2024-08-12 23:06-28
 */
@Component
@Slf4j
public class TaskCacheUtils {

    @Resource
    SchedulerConfiguration schedulerConfiguration;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    private String getZSetKey(TimerTask timerTask) {
        // 获取最大桶数
        int maxBucket = schedulerConfiguration.getBucketsNum();
        StringBuilder zSetKey = new StringBuilder();
        // 根据任务执行时间分配至横向时间分片
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String timeStr = sdf.format(new Date(timerTask.getRunTime()));
        // 根据 timerId % 桶数 分配至纵向桶分片
        long index = timerTask.getTimerId() % maxBucket;
        return zSetKey.append(timeStr).append("_").append(index).toString();
    }

    public boolean saveTasksToCache(List<TimerTask> taskList) {
        try {
            SessionCallback<Object> sessionCallback = new SessionCallback<>() {
                @Override
                public Object execute(RedisOperations redisOperations) throws DataAccessException {
                    redisOperations.multi();
                    for (TimerTask task : taskList) {
                        long unix = task.getRunTime();
                        String zSetKey = getZSetKey(task);
                        // 以 timerId + unix 作为任务标识
                        // 以执行时间戳作为分数排序
                        redisTemplate.opsForZSet().add(
                                zSetKey,
                                TimerUtils.unionTimerIDUnix(task.getTimerId(), unix),
                                unix);
                    }
                    return redisOperations.exec();
                }
            };
            redisTemplate.execute(sessionCallback);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public List<TimerTask> getTasksFromCache(String key, long start, long end) {
        List<TimerTask> tasks = new ArrayList<>();

        Set<Object> timerIDUnixSet = redisTemplate.opsForZSet().rangeByScore(key, start, end - 1);
        if (CollUtil.isEmpty(timerIDUnixSet)) {
            return tasks;
        }

        for (Object timerIDUnixObj : timerIDUnixSet) {
            TimerTask task = new TimerTask();
            String timerIDUnix = (String) timerIDUnixObj;
            List<Long> longSet = TimerUtils.splitTimerIDUnix(timerIDUnix);
            if (longSet.size() != 2) {
                log.error("splitTimerIDUnix 错误, timerIDUnix:{}", timerIDUnix);
                throw new BusinessException(ResponseCode.INTERNAL_SERVER_ERROR, "splitTimerIDUnix 错误, timerIDUnix: " + timerIDUnix);
            }
            task.setTimerId(longSet.get(0));
            task.setRunTime(longSet.get(1));
            tasks.add(task);
        }

        return tasks;
    }

}
