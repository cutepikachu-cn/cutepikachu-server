package cn.cutepikachu.common.snowflake.service;

import cn.cutepikachu.common.snowflake.SnowflakeIdGenerator;
import com.alibaba.cloud.nacos.NacosDiscoveryProperties;
import com.alibaba.cloud.nacos.NacosServiceManager;
import com.alibaba.cloud.nacos.discovery.NacosServiceDiscovery;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.listener.AbstractEventListener;
import com.alibaba.nacos.api.naming.listener.Event;
import com.alibaba.nacos.api.naming.listener.EventListener;
import com.alibaba.nacos.api.naming.listener.NamingEvent;
import com.alibaba.nacos.api.naming.pojo.Instance;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import static cn.cutepikachu.common.snowflake.SnowflakeIdGenerator.MAX_WORKER_ID;

/**
 * 雪花算法 ID 生成器服务
 *
 * @author <a href="https://github.com/cutepikachu-cn">笨蛋皮卡丘</a>
 * @version 1.0
 * @since 2024-09-18 23:32-37
 */
@Slf4j
@Service
public class SnowflakeIdGenerateService {

    @Resource
    private NacosServiceDiscovery nacosServiceDiscovery;

    @Resource
    private NacosDiscoveryProperties nacosDiscoveryProperties;

    @Resource
    private NacosServiceManager nacosServiceManager;

    private SnowflakeIdGenerator snowflakeIdGenerator;

    @PostConstruct
    private void init() {
        AtomicLong initCostTime = new AtomicLong(System.currentTimeMillis());
        String serviceName = nacosDiscoveryProperties.getService();
        String ip = nacosDiscoveryProperties.getIp();
        NamingService namingService = nacosServiceManager.getNamingService();
        try {

            // 订阅服务
            EventListener eventListener = new AbstractEventListener() {
                @Override
                public void onEvent(Event event) {
                    if (!(event instanceof NamingEvent)) {
                        return;
                    }
                    List<Instance> instances = ((NamingEvent) event).getInstances();
                    log.info(instances.toString());
                    instances.stream()
                            .filter(instance -> ip.equals(instance.getIp()))
                            .findFirst()
                            // 获取当前实例 ID 作为 workerId
                            .ifPresent(instance -> {
                                long workerId = Math.abs(instance.getInstanceId().hashCode()) % MAX_WORKER_ID;
                                snowflakeIdGenerator = new SnowflakeIdGenerator(workerId);
                                initCostTime.set(System.currentTimeMillis() - initCostTime.get());
                                log.info("SnowflakeIdGenerator init success, workerId: {}, cost time: {}ms", workerId, initCostTime);
                                // 取消订阅
                                try {
                                    namingService.unsubscribe(serviceName, this);
                                } catch (NacosException e) {
                                    throw new RuntimeException(e);
                                }
                            });
                }
            };

            namingService.subscribe(serviceName, eventListener);

        } catch (NacosException e) {
            throw new RuntimeException(e);
        }
    }

    public long nextId(String key) {
        return snowflakeIdGenerator.nextId(key);
    }

}
