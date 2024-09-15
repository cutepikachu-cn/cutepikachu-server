package cn.cutepikachu.xtimer.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

/**
 * 调度器配置
 *
 * @author <a href="https://github.com/cutepikachu-cn">笨蛋皮卡丘</a>
 * @version 1.0
 * @since 2024-08-12 21:41-21
 */
@RefreshScope
@Configuration
@ConfigurationProperties(prefix = "cutepikachu.xtimer.scheduler")
@Data
public class SchedulerConfiguration {

    /**
     * 任务存储 zRange 纵向分治桶数量（并发数）
     */
    @Value("${cutepikachu.xtimer.scheduler.bucketsNum}")
    private int bucketsNum;

    @Value("${cutepikachu.xtimer.scheduler.tryLockSeconds}")
    private int tryLockSeconds;

    @Value("${cutepikachu.xtimer.scheduler.tryLockGapMilliSeconds}")
    private int tryLockGapMilliSeconds;

    @Value("${cutepikachu.xtimer.scheduler.successExpireSeconds}")
    private int successExpireSeconds;

    /**
     * 调度器核心线程数
     */
    @Value("${cutepikachu.xtimer.scheduler.pool.corePoolSize}")
    private int corePoolSize;

    /**
     * 调度器最大线程数
     */
    @Value("${cutepikachu.xtimer.scheduler.pool.maxPoolSize}")
    private int maxPoolSize;

    /**
     * 调度器队列大小
     */
    @Value("${cutepikachu.xtimer.scheduler.pool.queueCapacity}")
    private int queueCapacity;

    /**
     * 调度器线程名称前缀
     */
    @Value("${cutepikachu.xtimer.scheduler.pool.namePrefix}")
    private String namePrefix;

}
