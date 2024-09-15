package cn.cutepikachu.biz.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * @author <a href="https://github.com/cutepikachu-cn">笨蛋皮卡丘</a>
 * @version 1.0
 * @since 2024-09-13 19:52-21
 */
public interface OssService {

    void upload(String bucketName, String objectName, MultipartFile file);

    void remove(String bucketName, String objectName);

}
