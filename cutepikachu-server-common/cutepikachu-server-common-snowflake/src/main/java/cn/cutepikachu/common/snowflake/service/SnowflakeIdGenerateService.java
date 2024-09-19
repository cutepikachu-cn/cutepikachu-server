package cn.cutepikachu.common.snowflake.service;

import cn.cutepikachu.common.snowflake.SnowflakeIdGenerator;
import com.alibaba.cloud.nacos.NacosDiscoveryProperties;
import com.alibaba.cloud.nacos.discovery.NacosServiceDiscovery;
import com.alibaba.nacos.api.exception.NacosException;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static cn.cutepikachu.common.snowflake.SnowflakeIdGenerator.MAX_WORKER_ID;

/**
 * 雪花算法 ID 生成器服务
 *
 * @author <a href="https://github.com/cutepikachu-cn">笨蛋皮卡丘</a>
 * @version 1.0
 * @since 2024-09-18 23:32-37
 */
@Service
public class SnowflakeIdGenerateService implements InitializingBean {

    @Resource
    private NacosServiceDiscovery nacosServiceDiscovery;

    @Resource
    private NacosDiscoveryProperties nacosDiscoveryProperties;

    private SnowflakeIdGenerator snowflakeIdGenerator;

    @Override
    public void afterPropertiesSet() {
        init();
    }

    private void init() {
        try {
            String service = nacosDiscoveryProperties.getService();
            List<ServiceInstance> instances = nacosServiceDiscovery.getInstances(service);
            if (!instances.isEmpty()) {
                ServiceInstance instance = instances.get(0);
                long workerId = Math.abs(instance.getInstanceId().hashCode()) % MAX_WORKER_ID;
                snowflakeIdGenerator = new SnowflakeIdGenerator(workerId);
            } else {
                ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);
                scheduledExecutorService.schedule(this::init, 1, TimeUnit.SECONDS);
            }
        } catch (NacosException e) {
            throw new RuntimeException(e);
        }
    }

    public long nextId(String key) {
        return snowflakeIdGenerator.nextId(key);
    }

}
