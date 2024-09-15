package cn.cutepikachu.shorturl.util;

import static cn.cutepikachu.shorturl.constant.ShortUrlConstant.LONG_URL_PREFIX;
import static cn.cutepikachu.shorturl.constant.ShortUrlConstant.SHORT_URL_PREFIX;

/**
 * @author <a href="https://github.com/cutepikachu-cn">笨蛋皮卡丘</a>
 * @version 1.0
 * @since 2024-09-02 20:00-28
 */
public class ShortUrlUtils {

    public static String getShortUrlKey(String shortUrl) {
        return SHORT_URL_PREFIX + shortUrl;
    }

    public static String splitShortUrl(String shortUrlKey) {
        return shortUrlKey.substring(SHORT_URL_PREFIX.length());
    }

    public static String getLongUrlKey(String longUrl) {
        return LONG_URL_PREFIX + longUrl;
    }

    public static String splitLongUrl(String longUrlKey) {
        return longUrlKey.substring(LONG_URL_PREFIX.length());
    }

}
