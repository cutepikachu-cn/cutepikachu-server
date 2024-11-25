package cn.cutepikachu.common.protection.ratelimit.manager;

import jakarta.annotation.Resource;
import org.redisson.api.*;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * 限流管理器
 * <p>
 * 基于 Redisson 实现（令牌桶算法）
 *
 * @author <a href="https://github.com/cutepikachu-cn">笨蛋皮卡丘</a>
 * @version 1.0
 * @since 2024-10-21 21:20-32
 */
@Component
public class RateLimiterManager {

    private static final String RATE_LIMITER_KEY = "rate_limiter:%s";

    @Resource
    private RedissonClient redissonClient;

    public RRateLimiter getRateLimiter(String key, long count, long time, TimeUnit unit) {

        String rateLimiterKey = String.format(RATE_LIMITER_KEY, key);

        // 统一转换为毫秒
        time = unit.toMillis(time);

        RRateLimiter rateLimiter = redissonClient.getRateLimiter(rateLimiterKey);
        RateLimiterConfig config = rateLimiter.getConfig();

        // 无配置
        if (config == null) {
            rateLimiter.trySetRate(RateType.OVERALL, count, time, RateIntervalUnit.MILLISECONDS);
            return rateLimiter;
        }

        // 配置相同
        if (Objects.equals(config.getRateType(), RateType.OVERALL)
                && Objects.equals(config.getRate(), count)
                && Objects.equals(config.getRateInterval(), time)) {
            return rateLimiter;
        }

        // 配置不同
        rateLimiter.setRate(RateType.OVERALL, count, time, RateIntervalUnit.MILLISECONDS);
        return rateLimiter;
    }

}
