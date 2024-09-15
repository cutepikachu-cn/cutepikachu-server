package cn.cutepikachu.xtimer.model.dto;

import cn.cutepikachu.common.request.PageDTO;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 定时任务信息表 查询 DTO
 *
 * @author <a href="https://github.com/cutepikachu-cn">笨蛋皮卡丘</a>
 * @version 0.0.1-SNAPSHOT
 * @since 2024-08-12 22:15:16
 */
@Data
public class TimerQueryDTO extends PageDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 定时任务 id
     */
    private Long timerId;

    /**
     * 业务方标识
     */
    private String app;

    /**
     * 任务名称
     */
    private String name;

    /**
     * 0 新建，1 激活，2 未激活
     */
    private Integer status;

    /**
     * cron 表达式
     */
    private String cron;

    /**
     * 回调上下文
     */
    private NotifyHTTPParam notifyHttpParam;

}
