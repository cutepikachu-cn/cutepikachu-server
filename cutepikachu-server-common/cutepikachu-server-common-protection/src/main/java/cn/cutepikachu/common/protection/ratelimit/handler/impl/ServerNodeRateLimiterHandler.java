package cn.cutepikachu.common.protection.ratelimit.handler.impl;

import cn.cutepikachu.common.protection.ratelimit.annotation.RateLimiter;
import cn.cutepikachu.common.protection.ratelimit.handler.RateLimiterHandler;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.system.SystemUtil;
import org.aspectj.lang.JoinPoint;

/**
 * 服务器节点级限流处理器
 * <p>
 * Key: md5(方法名 + 方法参数 + 服务器节点)
 *
 * @author <a href="https://github.com/cutepikachu-cn">笨蛋皮卡丘</a>
 * @version 1.0
 * @since 2024-10-21 21:38-52
 */
public class ServerNodeRateLimiterHandler implements RateLimiterHandler {

    @Override
    public String getKey(JoinPoint joinPoint, RateLimiter rateLimiter) {
        String methodName = joinPoint.getSignature().toString();
        String argsStr = StrUtil.join(",", joinPoint.getArgs());
        String serverNode = String.format("%s@%d", SystemUtil.getHostInfo().getAddress(), SystemUtil.getCurrentPID());
        String key = String.format("%s:%s:%s", methodName, argsStr, serverNode);
        return SecureUtil.md5(key);
    }

}
