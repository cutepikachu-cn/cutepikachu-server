package cn.cutepikachu.biz.service.factory;

import cn.cutepikachu.biz.model.enums.OssType;
import cn.cutepikachu.biz.service.OssService;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * OSS 服务工厂实现
 *
 * @author <a href="https://github.com/cutepikachu-cn">笨蛋皮卡丘</a>
 * @version 1.0
 * @since 2024-09-18 21:46-03
 */
@Component
public class OssServiceFactoryImpl implements OssServiceFactory {

    private final Map<OssType, OssService> ossServiceMap = new HashMap<>();

    @Override
    public void registerOssService(OssType ossType, OssService ossService) {
        Objects.requireNonNull(ossType, "OssType must not be null");
        Objects.requireNonNull(ossService, "OssService must not be null");
        ossServiceMap.put(ossType, ossService);
    }

    @Override
    public OssService getDefaultOssService() {
        OssService ossService = ossServiceMap.get(OssType.MINIO);
        Objects.requireNonNull(ossService, "Default OssService is null");
        return ossService;
    }

    @Override
    public OssService getOssService(OssType ossType) {
        Objects.requireNonNull(ossType, "OssType must not be null");
        return ossServiceMap.get(ossType);
    }

}
