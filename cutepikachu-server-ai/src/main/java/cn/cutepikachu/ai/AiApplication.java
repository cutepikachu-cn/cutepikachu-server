package cn.cutepikachu.ai;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author <a href="https://github.com/cutepikachu-cn">笨蛋皮卡丘</a>
 * @version 1.0
 * @since 2024-09-13 16:18-43
 */
// @EnableAsync
@EnableFeignClients(basePackages = {"cn.cutepikachu.**.inner"})
@EnableDiscoveryClient
@SpringBootApplication(scanBasePackages = {"cn.cutepikachu.**"})
public class AiApplication {

    public static void main(String[] args) {
        SpringApplication.run(AiApplication.class, args);
    }

}
