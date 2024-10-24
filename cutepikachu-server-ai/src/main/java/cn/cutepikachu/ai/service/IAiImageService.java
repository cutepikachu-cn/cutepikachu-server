package cn.cutepikachu.ai.service;

import cn.cutepikachu.ai.model.image.dto.AiImageDrawDTO;
import cn.cutepikachu.ai.model.image.vo.AiImageVO;
import cn.cutepikachu.common.model.user.entity.UserInfo;

/**
 * AI 文生图表 服务类
 *
 * @author <a href="https://github.com/cutepikachu-cn">笨蛋皮卡丘</a>
 * @version 0.0.1-SNAPSHOT
 * @since 2024-10-12 19:44:03
 */
public interface IAiImageService {

    AiImageVO drawImage(AiImageDrawDTO aiImageDrawDTO, UserInfo user);

    AiImageVO getAiImage(Long id, UserInfo user);

}
