package cn.cutepikachu.biz.service.factory;

import cn.cutepikachu.biz.model.enums.OssType;
import cn.cutepikachu.biz.service.OssService;
import cn.cutepikachu.biz.service.impl.MinioService;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @author <a href="https://github.com/cutepikachu-cn">笨蛋皮卡丘</a>
 * @version 1.0
 * @since 2024-09-18 21:46-03
 */
@Component
public class OssServiceFactory {

    private final Map<OssType, OssService> ossServiceMap = new HashMap<>();

    public OssServiceFactory(MinioService minioService) {
        ossServiceMap.put(OssType.MINIO, minioService);
    }

    public OssService getOssService(OssType ossType) {
        return ossServiceMap.get(ossType);
    }

}
