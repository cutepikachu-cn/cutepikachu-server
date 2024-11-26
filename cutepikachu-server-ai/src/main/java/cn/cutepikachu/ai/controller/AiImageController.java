package cn.cutepikachu.ai.controller;

import cn.cutepikachu.ai.model.image.dto.AiImageDrawDTO;
import cn.cutepikachu.ai.model.image.vo.AiImageVO;
import cn.cutepikachu.ai.service.IAiImageService;
import cn.cutepikachu.common.model.user.entity.UserInfo;
import cn.cutepikachu.common.response.BaseResponse;
import cn.cutepikachu.common.util.ResponseUtils;
import cn.cutepikachu.common.util.SecurityUtils;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

/**
 * 对外接口
 *
 * @author <a href="https://github.com/cutepikachu-cn">笨蛋皮卡丘</a>
 * @version 0.0.1-SNAPSHOT
 * @since 2024-10-12 19:44:03
 */
@RestController("/ai/image")
public class AiImageController {

    @Resource
    private IAiImageService aiImageService;

    @PostMapping("/draw")
    public BaseResponse<AiImageVO> draw(@RequestBody AiImageDrawDTO aiImageDrawDTO) {
        UserInfo userInfo = SecurityUtils.getLoginUser();
        AiImageVO aiImageVO = aiImageService.drawImage(aiImageDrawDTO, userInfo);
        return ResponseUtils.success(aiImageVO);
    }

    @GetMapping("/get")
    public BaseResponse<AiImageVO> get(@RequestParam Long id) {
        UserInfo loginUser = SecurityUtils.getLoginUser();
        AiImageVO aiImageVO = aiImageService.getAiImage(id, loginUser);
        return ResponseUtils.success(aiImageVO);
    }

}
