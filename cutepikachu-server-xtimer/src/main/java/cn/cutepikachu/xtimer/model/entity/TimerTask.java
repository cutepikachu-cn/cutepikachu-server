package cn.cutepikachu.xtimer.model.entity;

import cn.cutepikachu.common.model.BaseEntity;
import cn.cutepikachu.xtimer.model.vo.TimerTaskVO;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * 任务执行信息
 *
 * @author <a href="https://github.com/cutepikachu-cn">笨蛋皮卡丘</a>
 * @version 0.0.1-SNAPSHOT
 * @since 2024-08-12 22:15:16
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "`timer_task`", autoResultMap = true)
public class TimerTask extends BaseEntity<TimerTask, TimerTaskVO> implements Serializable {

    @Serial
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    /**
     * 任务执行 id
     */
    @TableId(value = "`task_id`")
    private Long taskId;

    /**
     * 定时任务 id
     */
    @TableField("`timer_id`")
    private Long timerId;

    /**
     * 业务方标识
     */
    @TableField("`app`")
    private String app;

    /**
     * 0 待执行，1 成功，2 失败
     */
    @TableField("`status`")
    private Integer status;

    /**
     * 执行结果输出
     */
    @TableField("`output`")
    private String output;

    /**
     * 运行开始时间
     */
    @TableField("`run_time`")
    private Long runTime;

    /**
     * 执行耗时（误差时间）
     */
    @TableField("`cost_time`")
    private Long costTime;

    public static final String TASK_ID = "task_id";

    public static final String TIMER_ID = "timer_id";

    public static final String APP = "app";

    public static final String STATUS = "status";

    public static final String OUTPUT = "output";

    public static final String RUN_TIME = "run_time";

    public static final String COST_TIME = "cost_time";

}
