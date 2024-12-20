package cn.cutepikachu.shorturl.service;

/**
 * 短链接映射表 服务类
 *
 * @author <a href="https://github.com/cutepikachu-cn">笨蛋皮卡丘</a>
 * @version 0.0.1-SNAPSHOT
 * @since 2024-09-02 19:12:07
 */
public interface IUrlMapService {

    String getLongUrlByShortUrl(String shortUrl);

    String createUrlMap(String longUrl);

}
