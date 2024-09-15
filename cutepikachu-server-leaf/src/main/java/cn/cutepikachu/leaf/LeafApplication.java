package cn.cutepikachu.leaf;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author <a href="https://github.com/cutepikachu-cn">笨蛋皮卡丘</a>
 * @version 1.0
 * @since 2024-08-03 17:50-36
 */
@EnableFeignClients(basePackages = {"cn.cutepikachu.**.inner"})
@EnableDiscoveryClient
@SpringBootApplication(scanBasePackages = {"cn.cutepikachu.**"})
public class LeafApplication {

    public static void main(String[] args) {
        SpringApplication.run(LeafApplication.class, args);
    }

}
