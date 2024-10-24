package cn.cutepikachu.common.protection.ratelimit.annotation;

import cn.cutepikachu.common.protection.ratelimit.enums.RateLimitingLevel;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

/**
 * 限流注解
 *
 * @author <a href="https://github.com/cutepikachu-cn">笨蛋皮卡丘</a>
 * @version 1.0
 * @since 2024-10-21 20:43-07
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface RateLimiter {

    /**
     * 限流的时间，默认为 1 秒
     */
    long time() default 1;

    /**
     * 限流的时间单位，默认为分钟
     */
    TimeUnit unit() default TimeUnit.MINUTES;

    /**
     * 限流次数，默认为 100 次
     */
    long count() default 100;

    /**
     * 限流级别，默认为全局
     *
     * @see RateLimitingLevel
     */
    RateLimitingLevel level() default RateLimitingLevel.GLOBAL;

}
