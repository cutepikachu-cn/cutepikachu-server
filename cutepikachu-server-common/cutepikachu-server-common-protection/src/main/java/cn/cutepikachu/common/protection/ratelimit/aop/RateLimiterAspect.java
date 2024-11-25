package cn.cutepikachu.common.protection.ratelimit.aop;

import cn.cutepikachu.common.protection.ratelimit.annotation.RateLimiter;
import cn.cutepikachu.common.protection.ratelimit.enums.RateLimitingLevel;
import cn.cutepikachu.common.protection.ratelimit.handler.RateLimiterHandler;
import cn.cutepikachu.common.protection.ratelimit.manager.RateLimiterManager;
import cn.cutepikachu.common.response.ErrorCode;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.redisson.api.RRateLimiter;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

import static cn.cutepikachu.common.exception.ExceptionFactory.bizException;
import static cn.cutepikachu.common.exception.ExceptionFactory.sysException;

/**
 * 限流切面
 *
 * @author <a href="https://github.com/cutepikachu-cn">笨蛋皮卡丘</a>
 * @version 1.0
 * @since 2024-10-21 20:44-56
 */
@Aspect
@Component
@Slf4j
public class RateLimiterAspect {

    @Resource
    private RateLimiterManager rateLimiterManager;

    @Before("@annotation(rateLimiterAnnotation)")
    public void beforeJoinPoint(JoinPoint joinPoint, RateLimiter rateLimiterAnnotation) {
        // 获取限流级别及对应的处理器
        RateLimitingLevel level = rateLimiterAnnotation.level();
        RateLimiterHandler rateLimiterHandler = RateLimitingLevel.getHandler(level);
        if (rateLimiterHandler == null) {
            throw sysException(ErrorCode.INTERNAL_ERROR, "RateLimiterHandler is null");
        }

        // 获取限流器
        String key = rateLimiterHandler.getKey(joinPoint, rateLimiterAnnotation);
        long count = rateLimiterAnnotation.count();
        long time = rateLimiterAnnotation.time();
        TimeUnit unit = rateLimiterAnnotation.unit();
        RRateLimiter rateLimiter = rateLimiterManager.getRateLimiter(key, count, time, unit);

        // 尝试获取令牌
        boolean success = rateLimiter.tryAcquire();
        if (!success) {
            log.info("[beforeJoinPoint][方法({}) 参数({}) 请求过于频繁]", joinPoint.getSignature().toString(), joinPoint.getArgs());
            throw bizException(ErrorCode.TOO_MANY_REQUESTS);
        }

    }

}
