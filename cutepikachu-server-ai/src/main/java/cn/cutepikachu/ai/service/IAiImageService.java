package cn.cutepikachu.ai.service;

import cn.cutepikachu.ai.model.image.dto.AiImageDrawDTO;
import cn.cutepikachu.ai.model.image.entity.AiImage;
import cn.cutepikachu.ai.model.image.vo.AiImageVO;
import cn.cutepikachu.common.model.user.vo.UserInfoVO;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * AI 文生图表 服务类
 *
 * @author <a href="https://github.com/cutepikachu-cn">笨蛋皮卡丘</a>
 * @version 0.0.1-SNAPSHOT
 * @since 2024-10-12 19:44:03
 */
public interface IAiImageService extends IService<AiImage> {

    AiImageVO drawImage(AiImageDrawDTO aiImageDrawDTO, UserInfoVO user);

    AiImageVO getAiImage(Long id, UserInfoVO user);

}
