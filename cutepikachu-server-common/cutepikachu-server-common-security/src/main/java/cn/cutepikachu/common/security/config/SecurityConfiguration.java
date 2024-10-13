package cn.cutepikachu.common.security.config;

import cn.cutepikachu.common.model.auth.enums.RoleEnum;
import cn.cutepikachu.common.security.util.PasswordUtil;
import cn.hutool.crypto.digest.DigestAlgorithm;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Map;

/**
 * 认证配置
 *
 * @author <a href="https://github.com/cutepikachu-cn">笨蛋皮卡丘</a>
 * @version 1.0
 * @since 2024-08-03 11:04-03
 */
@Data
@RefreshScope
@Configuration
@ConfigurationProperties(prefix = "cutepikachu.security")
public class SecurityConfiguration {

    /**
     * 加密盐值
     */
    private String salt;

    /**
     * 加密算法
     */
    private DigestAlgorithm algorithm;

    @Bean
    public PasswordUtil passwordUtil() {
        return new PasswordUtil(this);
    }

    /**
     * 路由配置
     */
    private Map<String, List<Router>> routers;

    /**
     * 路由配置
     */
    @Data
    static class Router {

        /**
         * 匹配路径
         */
        private String[] match;

        /**
         * 不匹配路径
         */
        private String[] notMatch;

        /**
         * 检查
         */
        private String check;

        /**
         * 角色
         */
        private RoleEnum role;

    }

}
