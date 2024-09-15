package cn.cutepikachu.biz.factory;

import cn.cutepikachu.biz.model.enums.OssType;
import cn.cutepikachu.biz.service.OssService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

/**
 * @author <a href="https://github.com/cutepikachu-cn">笨蛋皮卡丘</a>
 * @version 1.0
 * @since 2024-09-13 19:51-33
 */
@Component
public class OssFactory {

    @Resource
    private OssService miniOssService;

    public OssService getOssService(OssType ossType) {
        switch (ossType) {
            case MINIO:
                return miniOssService;
            default:
                return null;
        }
    }

}
