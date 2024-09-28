package cn.cutepikachu.common.redis.util;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Redis 配置
 *
 * @author <a href="https://github.com/cutepikachu-cn">笨蛋皮卡丘</a>
 * @version 1.0
 * @since 2024-08-12 16:57-11
 */
@Component
@Slf4j
public final class RedisUtils {

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    // 一般 Key 操作

    /**
     * 普通键存储
     *
     * @param key   键
     * @param value 值
     * @return true 成功 false 失败
     */
    public Boolean SET(String key, Object value) {
        try {
            redisTemplate.opsForValue().set(key, value);
            return Boolean.TRUE;
        } catch (Exception e) {
            log.error("Error setting value in Redis for key: {}, value: {}", key, value, e);
            return Boolean.FALSE;
        }
    }

    /**
     * 普通键获取
     *
     * @param key 键
     * @return 值
     */
    public Object GET(String key) {
        try {
            return redisTemplate.opsForValue().get(key);
        } catch (Exception e) {
            log.error("Error getting value from Redis for key: {}", key, e);
            return null;
        }
    }

    /**
     * 普通键存储，并设置过期时间
     *
     * @param key   键
     * @param value 值
     * @param time  时间，大于 0；如果 time 小于等于 0 将设置无限期
     * @param unit  时间单位
     * @return true 成功 false 失败
     */
    public Boolean SET(String key, Object value, long time, TimeUnit unit) {
        try {
            redisTemplate.opsForValue().set(key, value, time, unit);
            return Boolean.TRUE;
        } catch (Exception e) {
            log.error("Error setting value in Redis with expiration for key: {}, value: {}, time: {}, unit: {}", key, value, time, unit, e);
            return Boolean.FALSE;
        }
    }

    /**
     * 根据 key 获取过期时间
     *
     * @param key  键，不能为 null
     * @param unit 时间单位
     * @return 过期时间
     */
    public Long TTL(String key, TimeUnit unit) {
        try {
            return redisTemplate.getExpire(key, unit);
        } catch (Exception e) {
            log.error("Error getting expiration time for key: {}, unit: {}", key, unit, e);
            return 0L;
        }
    }

    /**
     * 查找与给定 pattern 匹配的所有键
     *
     * @param pattern 通配符
     * @return 匹配的键
     */
    public Set<String> KEYS(String pattern) {
        try {
            return redisTemplate.keys(pattern);
        } catch (Exception e) {
            log.error("Error getting keys with pattern: {}", pattern, e);
            return null;
        }
    }

    /**
     * 设置键过期时间
     *
     * @param key  键
     * @param time 时间，大于 0；如果 time 小于等于 0 将设置无限期
     * @param unit 时间单位
     * @return true 成功 false 失败
     */
    public Boolean EXPIRE(String key, long time, TimeUnit unit) {
        try {
            return redisTemplate.expire(key, time, unit);
        } catch (Exception e) {
            log.error("Error setting expiration time for key: {}, time: {}, unit: {}", key, time, unit, e);
            return Boolean.FALSE;
        }
    }

    /**
     * 判断键是否存在
     *
     * @param key 键
     * @return true 存在 false 不存在
     */
    public Boolean EXISTS(String key) {
        try {
            return redisTemplate.hasKey(key);
        } catch (Exception e) {
            log.error("Error checking if key exists: {}", key, e);
            return Boolean.FALSE;
        }
    }

    /**
     * 根据键删除
     *
     * @param key 一个或多个键
     * @return 删除的个数
     */
    public Long DEL(String... key) {
        try {
            return redisTemplate.delete(List.of(key));
        } catch (Exception e) {
            log.error("Error deleting key(s): {}", key, e);
            return 0L;
        }
    }

    /**
     * 普通键存储，不存在存储，存在返回
     *
     * @param key   键
     * @param value 值
     * @return true成功 false失败
     */
    public Boolean SETNX(String key, Object value) {
        try {
            return redisTemplate.opsForValue().setIfAbsent(key, value);
        } catch (Exception e) {
            log.error("Error setting value if absent in Redis for key: {}, value: {}", key, value, e);
            return Boolean.FALSE;
        }
    }

    /**
     * 普通键存储并设置过期时间，不存在存储，存在返回 false
     *
     * @param key   键
     * @param value 值
     * @param time  时间，大于 0；如果 time 小于等于 0 将设置无限期
     * @param unit  时间单位
     * @return true 成功 false 失败
     */
    public Boolean SETNX(String key, Object value, long time, TimeUnit unit) {
        try {
            return redisTemplate.opsForValue().setIfAbsent(key, value, time, unit);
        } catch (Exception e) {
            log.error("Error setting value if absent in Redis with expiration for key: {}, value: {}, time: {}, unit: {}", key, value, time, unit, e);
            return Boolean.FALSE;
        }
    }

    /**
     * 键值递增（整数）
     *
     * @param key   键
     * @param delta 增加值(大于0)
     * @return 递增后的值
     */
    public Long INCRBY(String key, long delta) {
        try {
            return redisTemplate.opsForValue().increment(key, delta);
        } catch (Exception e) {
            log.error("Error incrementing value in Redis for key: {}, delta: {}", key, delta, e);
            return 0L;
        }
    }

    /**
     * 键值递增（浮点数）
     *
     * @param key   键
     * @param delta 增加值(大于0)
     * @return 递增后的值
     */
    public Double INCRBY(String key, double delta) {
        try {
            return redisTemplate.opsForValue().increment(key, delta);
        } catch (Exception e) {
            log.error("Error incrementing value in Redis for key: {}, delta: {}", key, delta, e);
            return 0.0;
        }
    }

    /**
     * 键值递减（整数）
     *
     * @param key   键
     * @param delta 减少值(大于0)
     * @return 递减后的值
     */
    public Long DECRBY(String key, long delta) {
        try {
            return redisTemplate.opsForValue().increment(key, -delta);
        } catch (Exception e) {
            log.error("Error decrementing value in Redis for key: {}, delta: {}", key, delta, e);
            return 0L;
        }
    }

    /**
     * 键值递减（浮点数）
     *
     * @param key   键
     * @param delta 减少值(大于0)
     * @return 递减后的值
     */
    public Double DECRBY(String key, double delta) {
        try {
            return redisTemplate.opsForValue().increment(key, -delta);
        } catch (Exception e) {
            log.error("Error decrementing value in Redis for key: {}, delta: {}", key, delta, e);
            return 0.0;
        }
    }

    // Hash 操作

    /**
     * 获取表指定键的值
     *
     * @param key     键
     * @param hashKey 哈希键
     * @return 值
     */
    public Object HGET(String key, Object hashKey) {
        try {
            return redisTemplate.opsForHash().get(key, hashKey);
        } catch (Exception e) {
            log.error("Error getting value from Redis hash for key: {}, hashKey: {}", key, hashKey, e);
            return null;
        }
    }

    /**
     * 获取表中所有键值对
     *
     * @param key 键
     * @return 键值对 Map
     */
    public Map<Object, Object> HMGET(String key) {
        try {
            return redisTemplate.opsForHash().entries(key);
        } catch (Exception e) {
            log.error("Error getting all key-value pairs from Redis hash for key: {}", key, e);
            return null;
        }
    }

    /**
     * 设置表中多个键值对
     *
     * @param key 键
     * @param map 对应多个键值
     * @return true 成功 false 失败
     */
    public Boolean HSET(String key, Map<String, Object> map) {
        try {
            redisTemplate.opsForHash().putAll(key, map);
            return Boolean.TRUE;
        } catch (Exception e) {
            log.error("Error setting multiple key-value pairs in Redis hash for key: {}, map: {}", key, map, e);
            return Boolean.FALSE;
        }
    }

    /**
     * 设置表中多个键值对并设置过期时间
     *
     * @param key  键
     * @param map  键值对 Map
     * @param time 时间，大于 0；如果 time 小于等于 0 将设置无限期
     * @param unit 时间单位
     * @return true 成功 false 失败
     */
    public Boolean HSET(String key, Map<String, Object> map, long time, TimeUnit unit) {
        try {
            redisTemplate.opsForHash().putAll(key, map);
            EXPIRE(key, time, unit);
            return Boolean.TRUE;
        } catch (Exception e) {
            log.error("Error setting multiple key-value pairs in Redis hash with expiration for key: {}, map: {}, time: {}, unit: {}", key, map, time, unit, e);
            return Boolean.FALSE;
        }
    }

    /**
     * 向表中放入键值对
     *
     * @param key     键
     * @param hashKey hash 键
     * @param value   值
     * @return true 成功 false 失败
     */
    public Boolean HSET(String key, String hashKey, Object value) {
        try {
            redisTemplate.opsForHash().put(key, hashKey, value);
            return Boolean.TRUE;
        } catch (Exception e) {
            log.error("Error setting value in Redis hash for key: {}, hashKey: {}, value: {}", key, hashKey, value, e);
            return Boolean.FALSE;
        }
    }

    /**
     * 向表中放入键值对并设置过期时间
     *
     * @param key     键
     * @param hashKey hash 键
     * @param value   值
     * @param time    时间，大于 0；如果 time 小于等于 0 将设置无限期
     * @param unit    时间单位
     * @return true 成功 false 失败
     */
    public Boolean HSET(String key, String hashKey, Object value, long time, TimeUnit unit) {
        try {
            redisTemplate.opsForHash().put(key, hashKey, value);
            EXPIRE(key, time, unit);
            return Boolean.TRUE;
        } catch (Exception e) {
            log.error("Error setting value in Redis hash with expiration for key: {}, hashKey: {}, value: {}, time: {}, unit: {}", key, hashKey, value, time, unit, e);
            return Boolean.FALSE;
        }
    }

    /**
     * 删除表中的值
     *
     * @param key     键，不能为 null
     * @param hashKey hash 键，可又多个不能为 null
     */
    public Long HDEL(String key, Object... hashKey) {
        try {
            return redisTemplate.opsForHash().delete(key, hashKey);
        } catch (Exception e) {
            log.error("Error deleting value from Redis hash for key: {}, hashKey(s): {}", key, hashKey, e);
            return 0L;
        }
    }

    /**
     * 判断表中是否有该键
     *
     * @param key     键
     * @param hashKey hash 键
     * @return true 存在 false 不存在
     */
    public Boolean HEXISTS(String key, String hashKey) {
        try {
            return redisTemplate.opsForHash().hasKey(key, hashKey);
        } catch (Exception e) {
            log.error("Error checking if key exists in Redis hash for key: {}, hashKey: {}", key, hashKey, e);
            return Boolean.FALSE;
        }
    }

    /**
     * 值递增（整数）
     *
     * @param key     键
     * @param hashKey hash 键
     * @param delta   增加值(大于0)
     * @return 递增后的值
     */
    public Long HINCRBY(String key, String hashKey, long delta) {
        try {
            return redisTemplate.opsForHash().increment(key, hashKey, delta);
        } catch (Exception e) {
            log.error("Error incrementing value in Redis hash for key: {}, hashKey: {}, delta: {}", key, hashKey, delta, e);
            return 0L;
        }
    }

    /**
     * 值递增（浮点数）
     *
     * @param key     键
     * @param hashKey hash 键
     * @param delta   增加值(大于0)
     * @return 递增后的值
     */
    public Double HINCRBY(String key, String hashKey, double delta) {
        try {
            return redisTemplate.opsForHash().increment(key, hashKey, delta);
        } catch (Exception e) {
            log.error("Error incrementing value in Redis hash for key: {}, hashKey: {}, delta: {}", key, hashKey, delta, e);
            return 0.0;
        }
    }

    /**
     * 值递减（整数）
     *
     * @param key     键
     * @param hashKey hash 键
     * @param delta   减少值(大于0)
     * @return 递减后的值
     */
    public Long HDECRBY(String key, String hashKey, long delta) {
        try {
            return redisTemplate.opsForHash().increment(key, hashKey, -delta);
        } catch (Exception e) {
            log.error("Error decrementing value in Redis hash for key: {}, hashKey: {}, delta: {}", key, hashKey, delta, e);
            return 0L;
        }
    }

    /**
     * 值递减（浮点数）
     *
     * @param key     键
     * @param hashKey hash 键
     * @param delta   减少值(大于0)
     * @return 递减后的值
     */
    public Double HDECRBY(String key, String hashKey, double delta) {
        try {
            return redisTemplate.opsForHash().increment(key, hashKey, -delta);
        } catch (Exception e) {
            log.error("Error decrementing value in Redis hash for key: {}, hashKey: {}, delta: {}", key, hashKey, delta, e);
            return 0.0;
        }
    }

    // Set 操作

    /**
     * 获取集合中的所有成员
     *
     * @param key 键
     * @return 成员集合
     */
    public Set<Object> SMEMBERS(String key) {
        try {
            return redisTemplate.opsForSet().members(key);
        } catch (Exception e) {
            log.error("Error getting members from Redis set for key: {}", key, e);
            return new HashSet<>();
        }
    }

    /**
     * 判断成员是否在集合中
     *
     * @param key   键
     * @param value 成员值
     * @return true 存在 false 不存在
     */
    public Boolean SISMEMBER(String key, Object value) {
        try {
            return redisTemplate.opsForSet().isMember(key, value);
        } catch (Exception e) {
            log.error("Error checking if member exists in Redis set for key: {}, value: {}", key, value, e);
            return Boolean.FALSE;
        }
    }

    /**
     * 将成员存入集合
     *
     * @param key    键
     * @param values 成员值
     * @return 成功个数
     */
    public Long SADD(String key, Object... values) {
        try {
            return redisTemplate.opsForSet().add(key, values);
        } catch (Exception e) {
            log.error("Error adding members to Redis set for key: {}, members: {}", key, values, e);
            return 0L;
        }
    }

    /**
     * 将成员存入集合并设置过期时间
     *
     * @param key    键
     * @param time   时间，大于 0；如果 time 小于等于 0 将设置无限期
     * @param values 成员值
     * @param unit   时间单位
     * @return 成功个数
     */
    public Long SADD(String key, long time, TimeUnit unit, Object... values) {
        try {
            Long count = redisTemplate.opsForSet().add(key, values);
            EXPIRE(key, time, unit);
            return count;
        } catch (Exception e) {
            log.error("Error adding member(s) to Redis set with expiration for key: {}, member(s): {}, time: {}, unit: {}", key, values, time, unit, e);
            return 0L;
        }
    }

    /**
     * 获取集合成员数
     *
     * @param key 键
     * @return 成员数
     */
    public Long SCARD(String key) {
        try {
            return redisTemplate.opsForSet().size(key);
        } catch (Exception e) {
            log.error("Error getting member count from Redis set for key: {}", key, e);
            return 0L;
        }
    }

    /**
     * 移除集合中的成员
     *
     * @param key    键
     * @param values 成员值
     * @return 成功移除个数
     */
    public Long SREM(String key, Object... values) {
        try {
            return redisTemplate.opsForSet().remove(key, values);
        } catch (Exception e) {
            log.error("Error removing member(s) from Redis set for key: {}, member(s): {}", key, values, e);
            return 0L;
        }
    }

    // List 操作

    /**
     * 获取列表指定范围内的元素
     *
     * @param key   键
     * @param start 开始
     * @param end   结束
     * @return 元素列表
     */
    public List<Object> LRANGE(String key, long start, long end) {
        try {
            return redisTemplate.opsForList().range(key, start, end);
        } catch (Exception e) {
            log.error("Error getting range of elements from Redis list for key: {}, start: {}, end: {}", key, start, end, e);
            return new ArrayList<>();
        }
    }

    /**
     * 获取列表长度
     *
     * @param key 键
     * @return 长度
     */
    public Long LLEN(String key) {
        try {
            return redisTemplate.opsForList().size(key);
        } catch (Exception e) {
            log.error("Error getting length of Redis list for key: {}", key, e);
            return 0L;
        }
    }

    /**
     * 通过索引获取列表中的元素
     *
     * @param key   键
     * @param index 索引
     * @return 元素
     */
    public Object LINDEX(String key, long index) {
        try {
            return redisTemplate.opsForList().index(key, index);
        } catch (Exception e) {
            log.error("Error getting element from Redis list for key: {}, index: {}", key, index, e);
            return null;
        }
    }

    /**
     * 将元素插入列表头部
     *
     * @param key   键
     * @param value 值
     * @return true 成功 false 失败
     */
    public Boolean LPUSH(String key, Object value) {
        try {
            redisTemplate.opsForList().leftPush(key, value);
            return Boolean.TRUE;
        } catch (Exception e) {
            log.error("Error inserting element into Redis list head for key: {}, value: {}", key, value, e);
            return Boolean.FALSE;
        }
    }

    /**
     * 将元素插入列表尾部
     *
     * @param key   键
     * @param value 值
     * @return true 成功 false 失败
     */
    public Boolean RPUSH(String key, Object value) {
        try {
            redisTemplate.opsForList().rightPush(key, value);
            return Boolean.TRUE;
        } catch (Exception e) {
            log.error("Error inserting element into Redis list tail for key: {}, value: {}", key, value, e);
            return Boolean.FALSE;
        }
    }

    /**
     * 将元素插入列表头部并设置过期时间
     *
     * @param key   键
     * @param value 值
     * @param time  时间，大于 0；如果 time 小于等于 0 将设置无限期
     * @param unit  时间单位
     * @return true 成功 false 失败
     */
    public Boolean LPUSH(String key, Object value, long time, TimeUnit unit) {
        try {
            redisTemplate.opsForList().leftPush(key, value);
            EXPIRE(key, time, unit);
            return Boolean.TRUE;
        } catch (Exception e) {
            log.error("Error inserting element into Redis list head with expiration for key: {}, value: {}, time: {}, unit: {}", key, value, time, unit, e);
            return Boolean.FALSE;
        }
    }

    /**
     * 将元素插入列表尾部并设置过期时间
     *
     * @param key   键
     * @param value 值
     * @param time  时间，大于 0；如果 time 小于等于 0 将设置无限期
     * @param unit  时间单位
     * @return true 成功 false 失败
     */
    public Boolean RPUSH(String key, Object value, long time, TimeUnit unit) {
        try {
            redisTemplate.opsForList().rightPush(key, value);
            EXPIRE(key, time, unit);
            return Boolean.TRUE;
        } catch (Exception e) {
            log.error("Error inserting element into Redis list tail with expiration for key: {}, value: {}, time: {}, unit: {}", key, value, time, unit, e);
            return Boolean.FALSE;
        }
    }

    /**
     * 将多个元素插入列表头部
     *
     * @param key    键
     * @param values 元素列表
     * @return true 成功 false 失败
     */
    public Boolean LPUSH(String key, List<Object> values) {
        try {
            redisTemplate.opsForList().leftPushAll(key, values);
            return Boolean.TRUE;
        } catch (Exception e) {
            log.error("Error inserting multiple elements into Redis list head for key: {}, values: {}", key, values, e);
            return Boolean.FALSE;
        }
    }

    /**
     * 将多个元素插入列表尾部
     *
     * @param key    键
     * @param values 元素列表
     * @return true 成功 false 失败
     */
    public Boolean RPUSH(String key, List<Object> values) {
        try {
            redisTemplate.opsForList().rightPushAll(key, values);
            return Boolean.TRUE;
        } catch (Exception e) {
            log.error("Error inserting multiple elements into Redis list tail for key: {}, values: {}", key, values, e);
            return Boolean.FALSE;
        }
    }

    /**
     * 将多个元素插入列表头部并设置过期时间
     *
     * @param key    键
     * @param values 元素列表
     * @param time   时间，大于 0；如果 time 小于等于 0 将设置无限期
     * @param unit   时间单位
     * @return true 成功 false 失败
     */
    public Boolean LPUSH(String key, List<Object> values, long time, TimeUnit unit) {
        try {
            redisTemplate.opsForList().rightPushAll(key, values);
            EXPIRE(key, time, unit);
            return Boolean.TRUE;
        } catch (Exception e) {
            log.error("Error inserting multiple elements into Redis list head with expiration for key: {}, values: {}, time: {}, unit: {}", key, values, time, unit, e);
            return Boolean.FALSE;
        }
    }

    /**
     * 将多个元素插入列表尾部并设置过期时间
     *
     * @param key    键
     * @param values 元素列表
     * @param time   时间，大于 0；如果 time 小于等于 0 将设置无限期
     * @param unit   时间单位
     * @return true 成功 false 失败
     */
    public Boolean RPUSH(String key, List<Object> values, long time, TimeUnit unit) {
        try {
            redisTemplate.opsForList().rightPushAll(key, values);
            EXPIRE(key, time, unit);
            return Boolean.TRUE;
        } catch (Exception e) {
            log.error("Error inserting multiple elements into Redis list tail with expiration for key: {}, values: {}, time: {}, unit: {}", key, values, time, unit, e);
            return Boolean.FALSE;
        }
    }

    /**
     * 设置列表中指定索引的值
     *
     * @param key   键
     * @param index 索引
     * @param value 值
     * @return true 成功 false 失败
     */
    public Boolean LSET(String key, long index, Object value) {
        try {
            redisTemplate.opsForList().set(key, index, value);
            return Boolean.TRUE;
        } catch (Exception e) {
            log.error("Error setting value in Redis list for key: {}, index: {}, value: {}", key, index, value, e);
            return Boolean.FALSE;
        }
    }

    /**
     * 设置列表中指定索引的值并设置过期时间
     *
     * @param key   键
     * @param index 索引
     * @param value 值
     * @param time  时间，大于 0；如果 time 小于等于 0 将设置无限期
     * @param unit  时间单位
     * @return true 成功 false 失败
     */
    public Boolean LSET(String key, long index, Object value, long time, TimeUnit unit) {
        try {
            redisTemplate.opsForList().set(key, index, value);
            EXPIRE(key, time, unit);
            return Boolean.TRUE;
        } catch (Exception e) {
            log.error("Error setting value in Redis list with expiration for key: {}, index: {}, value: {}, time: {}, unit: {}", key, index, value, time, unit, e);
            return Boolean.FALSE;
        }
    }

    /**
     * @param key   键
     * @param count 移除个数
     * @param value 值
     * @return 成功个数
     */
    public Long LREM(String key, long count, Object value) {
        try {
            return redisTemplate.opsForList().remove(key, count, value);
        } catch (Exception e) {
            log.error("Error removing the first count elements with value from Redis list for key: {}, count: {}, value: {}", key, count, value, e);
            return 0L;
        }
    }

    /**
     * 执行 Lua 脚本
     *
     * @param script lua 脚本
     * @param keys   键
     * @param args   参数
     * @return 成功个数
     */
    public Long executeLua(RedisScript<Long> script, List<String> keys, Object... args) {
        try {
            return redisTemplate.execute(script, keys, args);
        } catch (Exception e) {
            log.error("Error executing Lua script: {}", script, e);
            return 0L;
        }
    }

    /**
     * 执行 Lua 脚本
     *
     * @param script lua 脚本
     * @param keys   键
     * @param args   参数
     * @return 结果列表
     */
    public List<?> executeLua(String script, List<String> keys, Object... args) {
        try {
            return stringRedisTemplate.execute(new DefaultRedisScript<>(script, List.class), keys, args);
        } catch (Exception e) {
            log.error("Error executing Lua script: {}", script, e);
            return null;
        }
    }

}


