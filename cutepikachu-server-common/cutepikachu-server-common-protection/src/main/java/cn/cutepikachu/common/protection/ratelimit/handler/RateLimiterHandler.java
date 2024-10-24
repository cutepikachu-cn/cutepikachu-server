package cn.cutepikachu.common.protection.ratelimit.handler;

import cn.cutepikachu.common.protection.ratelimit.annotation.RateLimiter;
import org.aspectj.lang.JoinPoint;

/**
 * 限流处理器接口
 *
 * @author <a href="https://github.com/cutepikachu-cn">笨蛋皮卡丘</a>
 * @version 1.0
 * @since 2024-10-21 21:00-27
 */
public interface RateLimiterHandler {

    /**
     * 获取限流 Key
     *
     * @param joinPoint   AOP 切面
     * @param rateLimiter 限流注解
     * @return Key
     */
    String getKey(JoinPoint joinPoint, RateLimiter rateLimiter);

}
