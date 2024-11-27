package cn.cutepikachu.common.protection.ratelimit.handler.impl;

import cn.cutepikachu.common.protection.ratelimit.annotation.RateLimiter;
import cn.cutepikachu.common.protection.ratelimit.handler.RateLimiterHandler;
import cn.cutepikachu.common.response.ErrorCode;
import cn.cutepikachu.common.util.ServletUtils;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.JoinPoint;

import static cn.cutepikachu.common.exception.ExceptionFactory.bizException;

/**
 * 基于会话的限流处理器
 * <p>
 * Key: md5(方法名 + 方法参数 + 会话ID)
 *
 * @author <a href="https://github.com/cutepikachu-cn">笨蛋皮卡丘</a>
 * @version 1.0
 * @since 2024-11-27 17:06-50
 */
public class SessionRateLimiterHandler implements RateLimiterHandler {

    @Override
    public String getKey(JoinPoint joinPoint, RateLimiter rateLimiter) {
        HttpServletRequest request = ServletUtils.getRequest();
        if (request == null) {
            throw bizException(ErrorCode.BAD_REQUEST, "无法获取请求");
        }
        String methodName = joinPoint.getSignature().toString();
        String sessionId = request.getSession().getId();
        String argsStr = StrUtil.join(",", joinPoint.getArgs());
        String key = String.format("%s:%s:%s", methodName, argsStr, sessionId);
        return SecureUtil.md5(key);
    }

}
