package cn.cutepikachu.xtimer.model.vo;

import cn.cutepikachu.common.model.BaseVO;
import cn.cutepikachu.xtimer.model.entity.TimerTask;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * 任务执行信息 VO
 *
 * @author <a href="https://github.com/cutepikachu-cn">笨蛋皮卡丘</a>
 * @version 0.0.1-SNAPSHOT
 * @since 2024-08-12 22:15:16
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TimerTaskVO extends BaseVO<TimerTask, TimerTaskVO> implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 任务执行 id
     */
    private Long taskId;

    /**
     * 定时任务 id
     */
    private Long timerId;

    /**
     * 业务方标识
     */
    private String app;

    /**
     * 0 待执行，1 成功，2 失败
     */
    private Integer status;

    /**
     * 执行结果输出
     */
    private String output;

    /**
     * 运行开始时间
     */
    private Long runTime;

    /**
     * 执行耗时（误差时间）
     */
    private Long costTime;

}
