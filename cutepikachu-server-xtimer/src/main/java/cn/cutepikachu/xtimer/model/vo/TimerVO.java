package cn.cutepikachu.xtimer.model.vo;

import cn.cutepikachu.common.model.BaseVO;
import cn.cutepikachu.xtimer.model.dto.NotifyHTTPParam;
import cn.cutepikachu.xtimer.model.entity.Timer;
import cn.hutool.json.JSONUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * 定时任务信息表 VO
 *
 * @author <a href="https://github.com/cutepikachu-cn">笨蛋皮卡丘</a>
 * @version 0.0.1-SNAPSHOT
 * @since 2024-08-12 22:15:16
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TimerVO extends BaseVO<Timer, TimerVO> implements Serializable {

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

    @Override
    public Timer toEntity(Class<Timer> timerClass) {
        Timer entity = super.toEntity(timerClass);
        String notifyHttpParam = JSONUtil.toJsonStr(this.getNotifyHttpParam());
        entity.setNotifyHttpParam(notifyHttpParam);
        return entity;
    }

}
