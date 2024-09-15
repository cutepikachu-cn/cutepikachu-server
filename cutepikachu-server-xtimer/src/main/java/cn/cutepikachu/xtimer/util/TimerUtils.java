package cn.cutepikachu.xtimer.util;

import org.quartz.CronExpression;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static cn.cutepikachu.xtimer.constant.TimerConstant.*;

/**
 * @author <a href="https://github.com/cutepikachu-cn">笨蛋皮卡丘</a>
 * @version 1.0
 * @since 2024-08-12 23:15-38
 */
public class TimerUtils {

    public static String getLockKey(String app) {
        return TIMER_LOCK + app;
    }

    public static String getEnableLockKey(String app) {
        return ENABLE_TIMER_LOCK + app;
    }

    public static String getMigratorLockKey(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH");
        String dateStr = sdf.format(date);
        return MIGRATOR_LOCK + dateStr;
    }

    public static String getTimeBucketLockKey(Date time, int bucketId) {
        StringBuilder sb = new StringBuilder();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String timeStr = sdf.format(time);
        return sb.append(TIME_BUCKET_LOCK).append(timeStr).append("_").append(bucketId).toString();
    }

    public static String getSliceMsgKey(Date time, int bucketId) {
        StringBuilder sb = new StringBuilder();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String timeStr = sdf.format(time);
        return sb.append(timeStr).append("_").append(bucketId).toString();
    }

    public static String getToken() {
        long timestamp = System.currentTimeMillis();
        String thread = Thread.currentThread().getName();
        return thread + timestamp;
    }

    public static Date getForwardTwoMigrateStepEnd(Date start, int diffSeconds) {
        return new Date(start.getTime() + 2L * diffSeconds * 1000);
    }

    public static List<Long> getCronNextBetween(CronExpression cronExpression, Date now, Date end) {
        List<Long> times = new ArrayList<>();
        if (end.before(now)) {
            return times;
        }

        for (Date start = now; start.before(end); ) {
            Date next = cronExpression.getNextValidTimeAfter(start);
            times.add(next.getTime());
            start = next;
        }
        return times;
    }

    public static String unionTimerIDUnix(long timerId, long unix) {
        return timerId + "_" + unix;
    }

    public static List<Long> splitTimerIDUnix(String timerIDUnix) {
        List<Long> longSet = new ArrayList<>();
        String[] strList = timerIDUnix.split("_");
        if (strList.length != 2) {
            return longSet;
        }
        longSet.add(Long.parseLong(strList[0]));
        longSet.add(Long.parseLong(strList[1]));
        return longSet;
    }

}
