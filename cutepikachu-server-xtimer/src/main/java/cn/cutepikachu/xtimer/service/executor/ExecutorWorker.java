package cn.cutepikachu.xtimer.service.executor;

import cn.cutepikachu.common.exception.BusinessException;
import cn.cutepikachu.common.response.ResponseCode;
import cn.cutepikachu.xtimer.model.dto.NotifyHTTPParam;
import cn.cutepikachu.xtimer.model.entity.Timer;
import cn.cutepikachu.xtimer.model.entity.TimerTask;
import cn.cutepikachu.xtimer.model.enums.TaskStatus;
import cn.cutepikachu.xtimer.model.enums.TimerStatus;
import cn.cutepikachu.xtimer.model.vo.TimerVO;
import cn.cutepikachu.xtimer.service.ITimerService;
import cn.cutepikachu.xtimer.service.ITimerTaskService;
import cn.cutepikachu.xtimer.util.TimerUtils;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * @author <a href="https://github.com/cutepikachu-cn">笨蛋皮卡丘</a>
 * @version 1.0
 * @since 2024-08-13 18:06-33
 */
@Component
@Slf4j
public class ExecutorWorker {

    @Resource
    private ITimerService timerService;

    @Resource
    private ITimerTaskService taskService;

    public void work(String timerIDUnixKey) {
        // 根据 timerId 和 runTime 获取任务
        List<Long> timerIDUnix = TimerUtils.splitTimerIDUnix(timerIDUnixKey);
        if (timerIDUnix.size() != 2) {
            log.error("splitTimerIDUnix 错误, timerIDUnix: {}", timerIDUnixKey);
            throw new BusinessException(ResponseCode.INTERNAL_SERVER_ERROR, "splitTimerIDUnix 错误, timerIDUnix: " + timerIDUnixKey);
        }
        Long timerId = timerIDUnix.get(0);
        Long unix = timerIDUnix.get(1);
        TimerTask task = taskService.lambdaQuery()
                .eq(TimerTask::getTimerId, timerId)
                .eq(TimerTask::getRunTime, unix)
                .one();
        // 判断任务是否已执行
        if (!Objects.equals(task.getStatus(), TaskStatus.NOT_RUN.getValue())) {
            log.warn("重复执行任务： timerId: {}, runTime: {}", timerId, unix);
            return;
        }

        // 执行任务并回调
        executeAndPostProcess(task, timerId);
    }

    private void executeAndPostProcess(TimerTask task, Long timerId) {
        // 定时任务是否还存在
        Timer timer = timerService.getById(timerId);
        if (timer == null) {
            log.error("执行回调错误，找不到对应的 Timer, timerId: {}", timerId);
            throw new BusinessException(ResponseCode.INTERNAL_SERVER_ERROR, "执行回调错误，找不到对应的 Timer, timerId: " + timerId);
        }

        // 任务是否未激活
        if (!Objects.equals(timer.getStatus(), TimerStatus.ENABLE.getValue())) {
            log.warn("Timer 处于去激活状态。 timerId: {}", timerId);
            return;
        }

        // 获取真实出发时间与设定执行时间 runTime 的误差时间
        long gapTime = new Date().getTime() - task.getRunTime();
        task.setCostTime(gapTime);

        // 执行 http 回调
        ResponseEntity<String> resp = null;
        try {
            resp = executeTimerCallBack(timer);
        } catch (Exception e) {
            log.error("执行回调失败，抛出异常: {}", String.valueOf(e));
        }

        // 更新任务状态
        if (resp == null) {
            task.setStatus(TaskStatus.FAIL.getValue());
        } else if (resp.getStatusCode().is2xxSuccessful()) {
            task.setStatus(TaskStatus.SUCCESS.getValue());
            task.setOutput(resp.toString());
        } else {
            task.setStatus(TaskStatus.FAIL.getValue());
            task.setOutput(resp.toString());
        }

        taskService.updateById(task);
    }

    private ResponseEntity<String> executeTimerCallBack(Timer timer) {
        TimerVO timerDTO = timer.toVO(TimerVO.class);
        NotifyHTTPParam httpParam = timerDTO.getNotifyHttpParam();
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> resp = null;
        if ("POST".equals(httpParam.getMethod())) {
            resp = restTemplate.postForEntity(httpParam.getUrl(), httpParam.getBody(), String.class);
        } else {
            log.error("不支持的 httpMethod");
        }
        if (resp == null || !resp.getStatusCode().is2xxSuccessful()) {
            log.error("http 回调失败：{}", resp);
        }
        return resp;
    }

}
