package cn.cutepikachu.biz.service.factory;

import cn.cutepikachu.biz.service.OssService;
import cn.cutepikachu.common.model.biz.enums.OssType;

/**
 * OSS 服务工厂
 *
 * @author <a href="https://github.com/cutepikachu-cn">笨蛋皮卡丘</a>
 * @version 1.0
 * @since 2024-10-13 13:33-46
 */
public interface OssServiceFactory {

    /**
     * 注册 OSS 服务
     *
     * @param ossType    OSS 类型
     * @param ossService OSS 服务
     */
    void registerOssService(OssType ossType, OssService ossService);

    /**
     * 获取默认 OSS 服务
     *
     * @return 默认 OSS 服务
     */
    OssService getDefaultOssService();

    /**
     * 获取 OSS 服务
     *
     * @param ossType OSS 类型
     * @return OSS 服务
     */
    OssService getOssService(OssType ossType);

}
