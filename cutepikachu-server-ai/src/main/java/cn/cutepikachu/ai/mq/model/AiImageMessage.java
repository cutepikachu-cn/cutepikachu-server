package cn.cutepikachu.ai.mq.model;

import cn.cutepikachu.ai.model.image.entity.AiImage;
import cn.cutepikachu.common.mq.model.IMessage;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author <a href="https://github.com/cutepikachu-cn">笨蛋皮卡丘</a>
 * @version 1.0
 * @since 2024-11-26 09:52-12
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AiImageMessage implements IMessage<AiImage> {

    private AiImage content;

}
