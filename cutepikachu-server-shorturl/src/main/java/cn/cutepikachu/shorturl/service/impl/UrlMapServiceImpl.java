package cn.cutepikachu.shorturl.service.impl;

import cn.cutepikachu.common.redis.util.RedisUtils;
import cn.cutepikachu.common.response.ResponseCode;
import cn.cutepikachu.common.snowflake.service.SnowflakeIdGenerateService;
import cn.cutepikachu.common.util.ThrowUtils;
import cn.cutepikachu.inner.leaf.DistributedIdInnerService;
import cn.cutepikachu.shorturl.mapper.UrlMapMapper;
import cn.cutepikachu.shorturl.model.entity.UrlMap;
import cn.cutepikachu.shorturl.service.IUrlMapService;
import cn.cutepikachu.shorturl.util.Base62Utils;
import cn.cutepikachu.shorturl.util.ShortUrlUtils;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import static cn.cutepikachu.shorturl.constant.ShortUrlConstant.*;

/**
 * 短链接映射表 服务实现类
 *
 * @author <a href="https://github.com/cutepikachu-cn">笨蛋皮卡丘</a>
 * @version 0.0.1-SNAPSHOT
 * @since 2024-09-02 19:12:07
 */
@Service
public class UrlMapServiceImpl extends ServiceImpl<UrlMapMapper, UrlMap> implements IUrlMapService, InitializingBean {

    @Resource
    private RedisUtils redisUtils;

    @Resource
    private RedissonClient redissonClient;

    private RBloomFilter<String> bloomFilter;

    @Resource
    private DistributedIdInnerService distributedIdInnerService;

    @Resource
    private SnowflakeIdGenerateService snowflakeIdGenerateService;

    @Override
    public void afterPropertiesSet() {
        // 初始化布隆过滤器
        bloomFilter = redissonClient.getBloomFilter(SHORT_URL_BLOOM_FILTER);
        bloomFilter.tryInit(SHORT_URL_BLOOM_FILTER_EXPECTED_INSERTIONS, SHORT_URL_BLOOM_FILTER_FALSE_PROBABILITY);
    }

    @Override
    public String getLongUrlByShortUrl(String shortUrl) {
        // 从布隆过滤器中查询是否存在
        boolean exist = bloomFilter.contains(shortUrl);
        if (!exist) {
            return NOT_FOUND_SHORT_URL;
        }

        // 查询缓存
        String shortUrlKey = ShortUrlUtils.getShortUrlKey(shortUrl);
        String longUrl = (String) redisUtils.GET(shortUrlKey);
        if (longUrl != null && !StrUtil.isBlank(longUrl)) {
            return longUrl;
        }

        // 查询数据库
        UrlMap urlMap = lambdaQuery()
                .eq(UrlMap::getShortUrl, shortUrl)
                .one();
        if (urlMap == null) {
            return NOT_FOUND_SHORT_URL;
        }

        // 写缓存
        redisUtils.SET(shortUrlKey, urlMap.getLongUrl(), CACHE_EXPIRE_TIME, CACHE_EXPIRE_TIME_UNIT);

        return urlMap.getLongUrl();
    }

    @Override
    public String createUrlMap(String longUrl) {
        // 查询是否已经存在
        // 查询缓存
        String longUrlKey = ShortUrlUtils.getLongUrlKey(longUrl);
        String shortUrl = (String) redisUtils.GET(longUrlKey);
        if (shortUrl != null && !StrUtil.isBlank(shortUrl)) {
            return shortUrl;
        }
        // 查询数据库
        UrlMap urlMap = lambdaQuery()
                .eq(UrlMap::getLongUrl, longUrl)
                .one();
        if (urlMap == null) {
            // 不存在，新建短链
            // BaseResponse<Long> resp = distributedIdInnerService.getDistributedID(DistributedBizTag.SHORT_URL);
            // ResponseUtils.throwIfNotSuccess(resp);
            // Long urlId = resp.getData();
            long urlId = snowflakeIdGenerateService.nextId("short_url");
            // 利用 Base62 编码生成短链接
            shortUrl = Base62Utils.generateBase62ShortUrl(urlId);

            // 存储到数据库
            urlMap = new UrlMap();
            urlMap.setUrlId(urlId)
                    .setLongUrl(longUrl)
                    .setShortUrl(shortUrl);
            ThrowUtils.throwIf(!save(urlMap), ResponseCode.INTERNAL_SERVER_ERROR, "创建短链失败");
        }
        shortUrl = urlMap.getShortUrl();

        // 存储短链到布隆过滤器中
        bloomFilter.add(shortUrl);

        // 存储到缓存
        redisUtils.SET(longUrlKey, shortUrl, CACHE_EXPIRE_TIME, CACHE_EXPIRE_TIME_UNIT);
        String shortUrlKey = ShortUrlUtils.getShortUrlKey(shortUrl);
        redisUtils.SET(shortUrlKey, longUrl, CACHE_EXPIRE_TIME, CACHE_EXPIRE_TIME_UNIT);

        return urlMap.getShortUrl();
    }

}
