package cn.cutepikachu.shorturl.constant;

import java.util.concurrent.TimeUnit;

/**
 * @author <a href="https://github.com/cutepikachu-cn">笨蛋皮卡丘</a>
 * @version 1.0
 * @since 2024-09-02 19:56-45
 */
public class ShortUrlConstant {

    public static final String NOT_FOUND_SHORT_URL = "https://www.cutepikachu.cn/404";

    public static final String SHORT_URL_BLOOM_FILTER = "short_url_bloom_filter";

    public static final Long SHORT_URL_BLOOM_FILTER_EXPECTED_INSERTIONS = 100L;

    public static final Double SHORT_URL_BLOOM_FILTER_FALSE_PROBABILITY = 0.1;

    public static final String SHORT_URL_PREFIX = "shorturl_";

    public static final String LONG_URL_PREFIX = "longurl_";

    public static final Long CACHE_EXPIRE_TIME = 1L;

    public static final TimeUnit CACHE_EXPIRE_TIME_UNIT = TimeUnit.DAYS;

}
