package cn.cutepikachu.leaf.segment.model;

import lombok.Data;

/**
 * @author <a href="https://github.com/Meituan-Dianping/Leaf"> leaf 美团分布式 ID 生成</a>
 * @version 1.0.1
 */
@Data
public class LeafAlloc {

    private String key;

    private long maxId;

    private int step;

    private String updateTime;

    private int randomStep;

}
