package cn.cutepikachu.leaf.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @author <a href="https://github.com/Meituan-Dianping/Leaf"> leaf 美团分布式 ID 生成</a>
 * @version 1.0.1
 */
@Data
@TableName("`leaf_alloc`")
public class LeafAlloc {

    @TableId("`biz_tag`")
    private String bizTag;

    @TableField("`max_id`")
    private long maxId;

    @TableField("`step`")
    private int step;

    @TableField("`update_time`")
    private String updateTime;

    @TableField("`random_step`")
    private int randomStep;

}
