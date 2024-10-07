package cn.cutepikachu.common.redis.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;

import java.lang.reflect.Field;

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
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) throws NoSuchFieldException, IllegalAccessException {
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

    private static RedisSerializer<?> buildJsonRedisSerializer() throws NoSuchFieldException, IllegalAccessException {
        RedisSerializer<Object> json = RedisSerializer.json();
        // 解决 LocalDateTime 的序列化
        Class<?> aClass = json.getClass();
        Field field = aClass.getField("mapper");
        field.setAccessible(true);
        ObjectMapper objectMapper = (ObjectMapper) field.get(json);
        objectMapper.registerModules(new JavaTimeModule());
        return json;
    }
}
