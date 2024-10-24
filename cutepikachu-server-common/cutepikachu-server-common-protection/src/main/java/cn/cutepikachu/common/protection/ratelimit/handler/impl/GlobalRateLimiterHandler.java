package cn.cutepikachu.common.protection.ratelimit.handler.impl;

import cn.cutepikachu.common.protection.ratelimit.annotation.RateLimiter;
import cn.cutepikachu.common.protection.ratelimit.handler.RateLimiterHandler;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import org.aspectj.lang.JoinPoint;
import org.springframework.stereotype.Component;

/**
 * 全局限流处理器
 * <p>
 * Key: md5(方法名 + 方法参数)
 *
 * @author <a href="https://github.com/cutepikachu-cn">笨蛋皮卡丘</a>
 * @version 1.0
 * @since 2024-10-21 21:14-51
 */
@Component
public class GlobalRateLimiterHandler implements RateLimiterHandler {

    @Override
    public String getKey(JoinPoint joinPoint, RateLimiter rateLimiter) {
        String methodName = joinPoint.getSignature().toString();
        String args = StrUtil.join(",", joinPoint.getArgs());
        String key = String.format("%s:%s", methodName, args);
        return SecureUtil.md5(key);
    }

}
