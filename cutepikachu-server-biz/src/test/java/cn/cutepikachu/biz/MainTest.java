package cn.cutepikachu.biz;

import cn.cutepikachu.biz.service.OssService;
import cn.cutepikachu.common.model.biz.enums.FileBizTag;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ResourceLoader;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * @author <a href="https://github.com/cutepikachu-cn">笨蛋皮卡丘</a>
 * @version 1.0
 * @since 2024-09-13 21:46-06
 */
@SpringBootTest(classes = BizApplication.class)
public class MainTest {

    @Resource
    OssService minioService;

    @Resource
    private ResourceLoader resourceLoader;

    @Test
    void upload() throws IOException {
        org.springframework.core.io.Resource resource = resourceLoader.getResource("classpath:avatar.jpg");
        Path path = resource.getFile().toPath();
        String contentType = Files.probeContentType(path);
        MultipartFile file = new MockMultipartFile(
                resource.getFilename(),
                resource.getFilename(),
                contentType,
                resource.getInputStream()
        );
    }

    @Test
    void get() {
        String url = minioService.getPresignedObjectUrl(FileBizTag.IMAGE_OTHER.getBucket(), "/user_avatar/1726416000000/6092582639a84cb09ac83cb0c31f7fb9");
        System.out.println(url);
    }

    @Test
    void remove() {
        minioService.remove("image", "/other/1728748800000/d208e27fc55647a1bb8c212f66d88865");
    }

}
