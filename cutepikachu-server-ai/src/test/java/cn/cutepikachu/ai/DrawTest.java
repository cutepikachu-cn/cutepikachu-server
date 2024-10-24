package cn.cutepikachu.ai;

import cn.cutepikachu.ai.dao.repository.AiImageRepository;
import cn.cutepikachu.ai.model.enums.AiImageStatus;
import cn.cutepikachu.ai.model.enums.AiPlatform;
import cn.cutepikachu.ai.model.image.dto.AiImageDrawDTO;
import cn.cutepikachu.ai.model.image.entity.AiImage;
import cn.cutepikachu.ai.model.image.vo.AiImageVO;
import cn.cutepikachu.ai.service.IAiImageService;
import cn.cutepikachu.common.model.user.entity.UserInfo;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Map;

/**
 * @author <a href="https://github.com/cutepikachu-cn">笨蛋皮卡丘</a>
 * @version 1.0
 * @since 2024-10-12 21:06-19
 */
@SpringBootTest(classes = AiApplication.class)
public class DrawTest {

    @Resource
    AiImageRepository aiImageRepository;

    @Resource
    IAiImageService aiImageService;

    @Test
    void contextLoads() {
        System.out.println();
    }

    @Test
    void typeHandler() {
        AiImage aiImage = new AiImage();
        aiImage.setStatus(AiImageStatus.IN_PROGRESS)
                .setPrompt("一只皮卡丘")
                .setHeight(1024)
                .setWidth(1024)
                .setOptions(Map.of("style", "cute", "color", "rainbow"))
                .setModel("wanx-v1")
                .setPlatform(AiPlatform.TONGYI)
                .setUserId(1L);
        aiImageRepository.save(aiImage);
    }

    @Test
    void draw() {
        AiImageDrawDTO aiImageDrawDTO = new AiImageDrawDTO();
        aiImageDrawDTO.setPlatform(AiPlatform.TONGYI)
                .setModel("wanx-v1")
                .setWidth(1024)
                .setHeight(1024)
                .setPrompt("一只皮卡丘");
        UserInfo userInfo = new UserInfo();
        userInfo.setUserId(100200L);
        AiImageVO aiImageVO = aiImageService.drawImage(aiImageDrawDTO, userInfo);
        System.out.println(aiImageVO);
    }

}
