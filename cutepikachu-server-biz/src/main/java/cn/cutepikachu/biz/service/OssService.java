package cn.cutepikachu.biz.service;

import cn.cutepikachu.biz.model.enums.FileBizTag;

/**
 * 对象存储服务接口
 *
 * @author <a href="https://github.com/cutepikachu-cn">笨蛋皮卡丘</a>
 * @version 1.0
 * @since 2024-09-13 19:52-21
 */
public interface OssService {

    void upload(byte[] bytes, FileBizTag bizTag, String path, String contentType);

    String getPresignedObjectUrl(String bucketName, String objectName, String objectPath);

    void remove(String bucketName, String objectName, String objectPath);

}
