package cn.cutepikachu.xtimer.controller;

import cn.cutepikachu.common.response.BaseResponse;
import cn.cutepikachu.common.util.ResponseUtils;
import cn.cutepikachu.xtimer.model.dto.TimerCreateDTO;
import cn.cutepikachu.xtimer.model.vo.TimerVO;
import cn.cutepikachu.xtimer.service.ITimerService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

/**
 * 定时任务 对外接口
 *
 * @author <a href="https://github.com/cutepikachu-cn">笨蛋皮卡丘</a>
 * @version 1.0
 * @since 2024-08-15 21:05-19
 */
@RestController
public class TimerController {

    @Resource
    private ITimerService timerService;

    @PostMapping("/test")
    public BaseResponse<?> test() {
        return ResponseUtils.success("Hello, XTimer!");
    }

    @PostMapping("/create")
    public BaseResponse<TimerVO> create(@RequestBody TimerCreateDTO timerCreateDTO) {
        TimerVO timerVO = timerService.createTimer(timerCreateDTO);
        return ResponseUtils.success(timerVO);
    }

    @GetMapping("/enable")
    public BaseResponse<Boolean> enable(@RequestParam String app,
                                        @RequestParam Long timerId) {
        timerService.enableTimer(app, timerId);
        return ResponseUtils.success(Boolean.TRUE);
    }

    @GetMapping("/unable")
    public BaseResponse<Boolean> unable(@RequestParam String app,
                                        @RequestParam Long timerId) {
        timerService.unableTimer(app, timerId);
        return ResponseUtils.success(Boolean.TRUE);
    }

}
