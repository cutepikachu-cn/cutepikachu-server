package cn.cutepikachu.xtimer.model.entity;

import cn.cutepikachu.common.model.BaseEntity;
import cn.cutepikachu.xtimer.model.dto.NotifyHTTPParam;
import cn.cutepikachu.xtimer.model.enums.TimerStatus;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * 定时任务信息表
 *
 * @author <a href="https://github.com/cutepikachu-cn">笨蛋皮卡丘</a>
 * @version 0.0.1-SNAPSHOT
 * @since 2024-08-12 22:15:16
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "`timer`", autoResultMap = true)
public class Timer extends BaseEntity implements Serializable {

    @Serial
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    /**
     * 定时任务 id
     */
    @TableId(value = "`timer_id`")
    private Long timerId;

    /**
     * 业务方标识
     */
    @TableField("`app`")
    private String app;

    /**
     * 任务名称
     */
    @TableField("`name`")
    private String name;

    /**
     * NEW 新建，ENABLE 激活，UNABLE 未激活
     */
    @TableField("`status`")
    private TimerStatus status;

    /**
     * cron 表达式
     */
    @TableField("`cron`")
    private String cron;

    /**
     * 回调上下文
     */
    @TableField(value = "`notify_http_param`", typeHandler = JacksonTypeHandler.class)
    private NotifyHTTPParam notifyHttpParam;

    public static final String TIMER_ID = "timer_id";

    public static final String APP = "app";

    public static final String NAME = "name";

    public static final String STATUS = "status";

    public static final String CRON = "cron";

    public static final String NOTIFY_HTTP_PARAM = "notify_http_param";

}
