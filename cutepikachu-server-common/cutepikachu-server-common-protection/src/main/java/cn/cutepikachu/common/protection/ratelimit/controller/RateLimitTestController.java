package cn.cutepikachu.common.protection.ratelimit.controller;

import cn.cutepikachu.common.protection.ratelimit.annotation.RateLimiter;
import cn.cutepikachu.common.protection.ratelimit.enums.RateLimitingLevel;
import cn.cutepikachu.common.response.BaseResponse;
import cn.cutepikachu.common.util.ResponseUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author <a href="https://github.com/cutepikachu-cn">笨蛋皮卡丘</a>
 * @version 1.0
 * @since 2024-11-22 09:43-14
 */
@RestController("/rateLimit")
public class RateLimitTestController {

    @RateLimiter(time = 5, unit = TimeUnit.SECONDS, count = 1, level = RateLimitingLevel.CLIENT_IP)
    @PostMapping("/test")
    public BaseResponse<Map<String, String>> test() {
        return ResponseUtils.success("ok", Map.of("key", "value"));
    }

}
