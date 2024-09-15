package cn.cutepikachu.xtimer.pool;

import cn.cutepikachu.xtimer.config.SchedulerConfiguration;
import cn.cutepikachu.xtimer.config.TriggerConfiguration;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 定时任务异步线程池配置
 *
 * @author <a href="https://github.com/cutepikachu-cn">笨蛋皮卡丘</a>
 * @version 1.0
 * @since 2024-08-12 21:49-59
 */
@Slf4j
@EnableAsync
@Configuration
public class AsyncPool {

    @Resource
    SchedulerConfiguration schedulerConfiguration;

    @Resource
    TriggerConfiguration triggerConfiguration;

    /**
     * 任务调度线程池（分片）
     */
    @Bean(name = "schedulerPool")
    public Executor schedulerPoolExecutor() {
        log.info("start schedulerPoolExecutor");

        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        // 配置核心线程数
        executor.setCorePoolSize(schedulerConfiguration.getCorePoolSize());
        // 配置最大线程数
        executor.setMaxPoolSize(schedulerConfiguration.getMaxPoolSize());
        // 配置队列大小
        executor.setQueueCapacity(schedulerConfiguration.getQueueCapacity());
        // 配置线程池线程名称前缀
        executor.setThreadNamePrefix(schedulerConfiguration.getNamePrefix());
        // 拒绝策略：CALLER_RUNS，当线程池满后，直接在调用者线程中执行
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        // 线程池初始化
        executor.initialize();

        return executor;
    }

    /**
     * 任务触发执行线程池
     */
    @Bean(name = "triggerPool")
    public Executor triggerPoolExecutor() {
        log.info("start triggerPoolExecutor");

        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        // 配置核心线程数
        executor.setCorePoolSize(triggerConfiguration.getCorePoolSize());
        // 配置最大线程数
        executor.setMaxPoolSize(triggerConfiguration.getMaxPoolSize());
        // 配置队列大小
        executor.setQueueCapacity(triggerConfiguration.getQueueCapacity());
        // 配置线程池线程名称前缀
        executor.setThreadNamePrefix(triggerConfiguration.getNamePrefix());

        // 拒绝策略：CALLER_RUNS，当线程池满后，直接在调用者线程中执行
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        // 线程池初始化
        executor.initialize();

        return executor;
    }

}
