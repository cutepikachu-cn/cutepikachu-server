package cn.cutepikachu.xtimer.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

/**
 * 触发器配置
 *
 * @author <a href="https://github.com/cutepikachu-cn">笨蛋皮卡丘</a>
 * @version 1.0
 * @since 2024-08-12 21:41-21
 */
@RefreshScope
@Configuration
@ConfigurationProperties(prefix = "cutepikachu.xtimer.trigger")
@Data
public class TriggerConfiguration {

    @Value("${cutepikachu.xtimer.trigger.zrangeGapSeconds}")
    private int zrangeGapSeconds;

    @Value("${cutepikachu.xtimer.trigger.workersNum}")
    private int workersNum;

    @Value("${cutepikachu.xtimer.trigger.pool.corePoolSize}")
    private int corePoolSize;

    @Value("${cutepikachu.xtimer.trigger.pool.maxPoolSize}")
    private int maxPoolSize;

    @Value("${cutepikachu.xtimer.trigger.pool.queueCapacity}")
    private int queueCapacity;

    @Value("${cutepikachu.xtimer.trigger.pool.namePrefix}")
    private String namePrefix;

}
