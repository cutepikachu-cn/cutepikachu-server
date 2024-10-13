package cn.cutepikachu.biz.config;

import cn.cutepikachu.biz.service.OssService;
import cn.cutepikachu.biz.service.factory.OssServiceFactory;
import cn.cutepikachu.biz.service.impl.MinioService;
import jakarta.annotation.Resource;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Minio 配置类
 *
 * @author <a href="https://github.com/cutepikachu-cn">笨蛋皮卡丘</a>
 * @version 1.0
 * @since 2024-09-13 21:09-19
 */
@RefreshScope
@Data
@Configuration
@ConfigurationProperties(prefix = "cutepikachu.biz.oss.minio")
public class MinioConfiguration {

    private String endpoint;

    private String accessKey;

    private String secretKey;

    @Resource
    private OssServiceFactory ossServiceFactory;

    @Bean
    public OssService minioService() {
        return new MinioService(this, ossServiceFactory);
    }

}
