package cn.cutepikachu.xtimer.model.entity;

import cn.cutepikachu.common.model.BaseEntity;
import cn.cutepikachu.xtimer.model.dto.NotifyHTTPParam;
import cn.cutepikachu.xtimer.model.vo.TimerVO;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

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
public class Timer extends BaseEntity<Timer, TimerVO> implements Serializable {

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
     * 0 新建，1 激活，2 未激活
     */
    @TableField("`status`")
    private Integer status;

    /**
     * cron 表达式
     */
    @TableField("`cron`")
    private String cron;

    /**
     * 回调上下文
     */
    @TableField("`notify_http_param`")
    private String notifyHttpParam;

    /**
     * 创建时间
     */
    @TableField(value = "`create_time`", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField(value = "`update_time`", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    /**
     * 是否删除
     */
    @TableField("`is_delete`")
    @TableLogic
    private Boolean isDelete;

    public static final String TIMER_ID = "timer_id";

    public static final String APP = "app";

    public static final String NAME = "name";

    public static final String STATUS = "status";

    public static final String CRON = "cron";

    public static final String NOTIFY_HTTP_PARAM = "notify_http_param";

    public static final String CREATE_TIME = "create_time";

    public static final String UPDATE_TIME = "update_time";

    public static final String IS_DELETE = "is_delete";

    @Override
    public TimerVO toVO(Class<TimerVO> timerVOClass) {
        TimerVO vo = super.toVO(timerVOClass);
        NotifyHTTPParam notifyHTTPParam = JSONUtil.toBean(this.getNotifyHttpParam(), NotifyHTTPParam.class);
        vo.setNotifyHttpParam(notifyHTTPParam);
        return vo;
    }

}
