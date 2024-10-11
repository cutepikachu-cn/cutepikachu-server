package cn.cutepikachu.common.redis.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;

/**
 * Redis 配置
 *
 * @author <a href="https://github.com/cutepikachu-cn">笨蛋皮卡丘</a>
 * @version 1.0
 * @since 2024-08-12 16:57-11
 */
@Configuration
public class RedisConfiguration {

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) {
        // 创建 RedisTemplate 对象
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        // 设置 RedisConnection 工厂
        template.setConnectionFactory(factory);

        // 使用 String 序列化方式，序列化 key
        template.setKeySerializer(RedisSerializer.string());
        // 使用 String 序列化方式，序列化 hash 的 key
        template.setHashKeySerializer(RedisSerializer.string());

        // 使用 JSON 序列化方式（Jackson ），序列化 value
        template.setValueSerializer(buildJsonRedisSerializer());
        template.setHashValueSerializer(buildJsonRedisSerializer());

        return template;
    }

    private static RedisSerializer<?> buildJsonRedisSerializer() {
        ObjectMapper objectMapper = new ObjectMapper();
        // 解决 LocalDateTime 的序列化
        objectMapper.registerModules(new JavaTimeModule());
        objectMapper.findAndRegisterModules();
        return new GenericJackson2JsonRedisSerializer(objectMapper);
    }
}
