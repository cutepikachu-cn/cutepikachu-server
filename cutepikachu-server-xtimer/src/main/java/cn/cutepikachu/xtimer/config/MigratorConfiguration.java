package cn.cutepikachu.xtimer.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

/**
 * 迁移器配置
 *
 * @author <a href="https://github.com/cutepikachu-cn">笨蛋皮卡丘</a>
 * @version 1.0
 * @since 2024-08-12 21:41-21
 */
@RefreshScope
@Configuration
@ConfigurationProperties(prefix = "cutepikachu.xtimer.migrator")
@Data
public class MigratorConfiguration {

    @Value("${cutepikachu.xtimer.migrator.workersNum}")
    private int workersNum;

    /**
     * 迁移步长，单位秒
     */
    @Value("${cutepikachu.xtimer.migrator.migrateStepSeconds}")
    private int migrateStepSeconds;

    /**
     * 迁移成功后锁过期时间，单位秒
     */
    @Value("${cutepikachu.xtimer.migrator.migrateSuccessExpireSeconds}")
    private int migrateSuccessExpireSeconds;

    /**
     * 尝试获取锁时间，单位秒
     */
    @Value("${cutepikachu.xtimer.migrator.migrateTryLockSeconds}")
    private int migrateTryLockSeconds;

    @Value("${cutepikachu.xtimer.migrator.timerDetailCacheSeconds}")
    private int timerDetailCacheSeconds;

}
