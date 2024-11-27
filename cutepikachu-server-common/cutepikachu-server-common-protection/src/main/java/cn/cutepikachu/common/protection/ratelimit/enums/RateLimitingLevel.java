package cn.cutepikachu.common.protection.ratelimit.enums;

import cn.cutepikachu.common.protection.ratelimit.handler.RateLimiterHandler;
import cn.cutepikachu.common.protection.ratelimit.handler.impl.*;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

/**
 * 限流级别枚举
 *
 * @author <a href="https://github.com/cutepikachu-cn">笨蛋皮卡丘</a>
 * @version 1.0
 * @since 2024-10-21 21:29-29
 */
@Getter
@AllArgsConstructor
public enum RateLimitingLevel {

    GLOBAL("全局", new GlobalRateLimiterHandler()),
    USER("用户", new UserRateLimiterHandler()),
    CLIENT_IP("客户端 IP", new ClientIpRateLimiterHandler()),
    SERVER_NODE("服务器节点", new ServerNodeRateLimiterHandler()),
    SESSION("会话", new SessionRateLimiterHandler());

    private final String desc;

    private final RateLimiterHandler handler;

    public static RateLimiterHandler getHandler(RateLimitingLevel level) {
        Objects.requireNonNull(level, "level must not be null");
        for (RateLimitingLevel value : values()) {
            if (value == level) {
                return value.handler;
            }
        }
        return null;
    }

}
