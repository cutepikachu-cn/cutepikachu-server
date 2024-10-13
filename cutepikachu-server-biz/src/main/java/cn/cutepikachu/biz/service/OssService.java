package cn.cutepikachu.biz.service;

import cn.cutepikachu.biz.model.enums.OssType;

/**
 * 对象存储服务接口
 *
 * @author <a href="https://github.com/cutepikachu-cn">笨蛋皮卡丘</a>
 * @version 1.0
 * @since 2024-09-13 19:52-21
 */
public interface OssService {

    OssType getSelfOssType();

    String getEndpoint();

    void upload(byte[] bytes, String bucket, String path, String contentType);

    boolean remove(String bucket, String objectPath);

    boolean makeBucket(String bucket);

    boolean existsBucket(String bucket);

    String getPresignedObjectUrl(String bucketName, String objectPath);

}
