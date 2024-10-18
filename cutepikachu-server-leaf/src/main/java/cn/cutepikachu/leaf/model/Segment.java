package cn.cutepikachu.leaf.model;

import lombok.Data;

import java.util.concurrent.atomic.AtomicLong;

/**
 * @author <a href="https://github.com/Meituan-Dianping/Leaf"> leaf 美团分布式 ID 生成</a>
 * @version 1.0.1
 */
@Data
public class Segment {

    private AtomicLong value = new AtomicLong(0);

    private volatile long max;

    private volatile int step;

    private volatile int randomStep;

    private SegmentBuffer buffer;

    public Segment(SegmentBuffer buffer) {
        this.buffer = buffer;
    }

    public long getIdle() {
        return this.getMax() - getValue().get();
    }

    @Override
    public String toString() {
        return "Segment{" +
                "value=" + value +
                ", max=" + max +
                ", step=" + step +
                ", randomStep=" + randomStep +
                '}';
    }
}
