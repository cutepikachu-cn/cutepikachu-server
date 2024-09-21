package cn.cutepikachu.common.snowflake;

import cn.cutepikachu.common.snowflake.exception.ClockGoBackException;
import lombok.Getter;
import lombok.Setter;

import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 雪花算法 ID 生成器
 *
 * @author <a href="https://github.com/cutepikachu-cn">笨蛋皮卡丘</a>
 * @version 1.0
 * @since 2024-09-18 23:14-14
 */
public class SnowflakeIdGenerator {

    // 起始的时间戳: 2020-01-01 00:00:00
    public static final long EPOCH = 1577808000000L;
    // 机器ID位数
    public static final long WORKER_ID_BITS = 10L;
    // 序列号位数
    public static final long SEQUENCE_BITS = 12L;

    // 最大机器ID
    public static final long MAX_WORKER_ID = ~(-1L << WORKER_ID_BITS);
    // 最大序列号
    public static final long MAX_SEQUENCE = ~(-1L << SEQUENCE_BITS);

    public static final long WORKER_ID_SHIFT = SEQUENCE_BITS;
    public static final long TIMESTAMP_LEFT_SHIFT = SEQUENCE_BITS + WORKER_ID_BITS;

    @Getter
    @Setter
    private long workerId;
    private long sequence = 0L;
    private long lastTimestamp = -1L;

    private static final Random RANDOM = new Random();
    private static final ConcurrentHashMap<String, Lock> KEY_LOCKS = new ConcurrentHashMap<>();

    public SnowflakeIdGenerator(long workerId) {
        this.workerId = workerId;
    }

    public long nextId(String key) {
        Lock lock = KEY_LOCKS.computeIfAbsent(key, k -> new ReentrantLock());
        lock.lock();
        try {
            return generateId();
        } finally {
            lock.unlock();
        }
    }

    private long generateId() {
        // 获取当前时间戳
        long timestamp = currentTime();

        // 如果当前时间小于上次生成 ID 的时间戳，则发生了时钟回拨
        if (timestamp < lastTimestamp) {
            handleClockBackwards(timestamp);
        }

        // 如果当前时间与上次生成ID的时间相同（即在同一毫秒内）
        if (lastTimestamp == timestamp) {
            // 增加序列号（用来区分同一毫秒内生成的不同ID）
            sequence = (sequence + 1) & MAX_SEQUENCE;
            // 如果序列号达到最大值，表示同一毫秒内ID已生成满
            if (sequence == 0) {
                // 随机初始化下一个序列号，等待到下一毫秒
                sequence = RANDOM.nextInt(100);
                timestamp = untilNextMillis(lastTimestamp);
            }
        } else {
            // 如果不是同一毫秒，则重置序列号，随机一个初始值
            sequence = RANDOM.nextInt(100);
        }

        // 更新上次生成ID的时间戳
        lastTimestamp = timestamp;

        // 组合 SnowflakeID
        return ((timestamp - EPOCH) << TIMESTAMP_LEFT_SHIFT)
                | (workerId << WORKER_ID_SHIFT)
                | sequence;
    }

    private void handleClockBackwards(long timestamp) {
        // 获取时钟回拨的时间差
        long offset = lastTimestamp - timestamp;

        // 如果时间差小于等于 5 毫秒，允许等待
        if (offset <= 5) {
            try {
                // 等待 offset 毫秒后重试
                wait(offset << 1);
                // 再次获取当前时间
                timestamp = currentTime();
                // 如果等待后，时间戳仍然小于上次生成 ID 的时间戳，则返回时钟回拨异常
                if (timestamp < lastTimestamp) {
                    throw new ClockGoBackException("Clock moved backwards");
                }
            } catch (InterruptedException e) {
                // 如果等待被中断，则直接抛出异常
                throw new ClockGoBackException("Clock moved backwards, Wait retry interrupted");
            }
        } else {
            // 如果时钟回拨超过 5 毫秒，则直接抛出异常
            throw new ClockGoBackException("Clock moved backwards");
        }
    }

    private long untilNextMillis(long lastTimestamp) {
        long timestamp = currentTime();
        while (timestamp <= lastTimestamp) {
            timestamp = currentTime();
        }
        return timestamp;
    }

    private long currentTime() {
        return System.currentTimeMillis();
    }

}
