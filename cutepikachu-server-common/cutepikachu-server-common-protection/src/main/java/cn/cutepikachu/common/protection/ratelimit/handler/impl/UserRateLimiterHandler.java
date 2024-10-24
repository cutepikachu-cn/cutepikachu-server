package cn.cutepikachu.common.protection.ratelimit.handler.impl;

import cn.cutepikachu.common.protection.ratelimit.annotation.RateLimiter;
import cn.cutepikachu.common.protection.ratelimit.handler.RateLimiterHandler;
import cn.cutepikachu.common.util.SecurityUtils;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import org.aspectj.lang.JoinPoint;

/**
 * 用户级别限流处理器
 * <p>
 * Key: md5(方法名 + 方法参数 + 用户 ID)
 *
 * @author <a href="https://github.com/cutepikachu-cn">笨蛋皮卡丘</a>
 * @version 1.0
 * @since 2024-10-21 21:38-12
 */
public class UserRateLimiterHandler implements RateLimiterHandler {

    @Override
    public String getKey(JoinPoint joinPoint, RateLimiter rateLimiter) {
        String methodName = joinPoint.getSignature().toString();
        String argsStr = StrUtil.join(",", joinPoint.getArgs());
        Long userId = SecurityUtils.getLoginUserId();
        String key = String.format("%s:%s:%s", methodName, argsStr, userId);
        return SecureUtil.md5(key);
    }

}
