import cn.cutepikachu.shorturl.model.entity.UrlMap;
import cn.cutepikachu.shorturl.ShortUrlApplication;
import cn.cutepikachu.shorturl.service.IUrlMapService;
import cn.cutepikachu.shorturl.util.Base62Utils;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author <a href="https://github.com/cutepikachu-cn">笨蛋皮卡丘</a>
 * @version 1.0
 * @since 2024-09-02 19:26-27
 */
@SpringBootTest(classes = ShortUrlApplication.class)
public class MainTest {

    @Resource
    IUrlMapService urlMapService;

    @Test
    public void test() {
        UrlMap urlMap = urlMapService.getById(1);
        System.out.println(urlMap);
    }

    @Test
    public void base62() {
        System.out.println(Base62Utils.generateBase62ShortUrl(1000000000000000000L));
    }

}
