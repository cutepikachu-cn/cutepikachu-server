package cn.cutepikachu.biz.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

/**
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

}
