package cn.cutepikachu.leaf.model;

import lombok.Data;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 双 buffer
 *
 * @author <a href="https://github.com/Meituan-Dianping/Leaf"> leaf 美团分布式 ID 生成</a>
 * @version 1.0.1
 */
@Data
public class SegmentBuffer {

    private String key;

    // 双buffer
    private Segment[] segments;

    // 当前的使用的segment的index
    private volatile int currentPos;

    // 下一个segment是否处于可切换状态
    private volatile boolean nextReady;

    // 是否初始化完成
    private volatile boolean initOk;

    // 线程是否在运行中
    private final AtomicBoolean threadRunning;

    private final ReadWriteLock lock;

    private volatile int step;

    private volatile int minStep;

    private volatile long updateTimestamp;

    public SegmentBuffer() {
        segments = new Segment[]{new Segment(this), new Segment(this)};
        currentPos = 0;
        nextReady = false;
        initOk = false;
        threadRunning = new AtomicBoolean(false);
        lock = new ReentrantReadWriteLock();
    }

    public Segment getCurrent() {
        return segments[currentPos];
    }

    public int nextPos() {
        return (currentPos + 1) % 2;
    }

    public void switchPos() {
        currentPos = nextPos();
    }

    public Lock rLock() {
        return lock.readLock();
    }

    public Lock wLock() {
        return lock.writeLock();
    }

}
