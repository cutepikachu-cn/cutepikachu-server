package cn.cutepikachu.common.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 分布式业务标识
 *
 * @author <a href="https://github.com/cutepikachu-cn">笨蛋皮卡丘</a>
 * @version 1.0
 * @since 2024-08-03 19:09-42
 */
@Getter
@AllArgsConstructor
public enum DistributedBizTag {

    AUTH_ACCOUNT("cutepikachu-server-auth-account"),
    TIMER("cutepikachu-server-xtimer"),
    SHORT_URL("cutepikachu-server-shorturl"),
    FILE("cutepikachu-server-file"),
    AI_IMAGE("cutepikachu-server-ai-image"),
    ;

    private final String key;


}
