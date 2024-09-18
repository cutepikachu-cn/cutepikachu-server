package cn.cutepikachu.biz.service.impl;

import cn.cutepikachu.biz.config.MinioConfiguration;
import cn.cutepikachu.biz.model.enums.FileBizTag;
import cn.cutepikachu.biz.service.OssService;
import cn.cutepikachu.common.exception.BusinessException;
import cn.cutepikachu.common.response.ResponseCode;
import io.minio.GetPresignedObjectUrlArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.RemoveObjectArgs;
import io.minio.http.Method;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;

/**
 * Minio 对象存储服务类
 *
 * @author <a href="https://github.com/cutepikachu-cn">笨蛋皮卡丘</a>
 * @version 1.0
 * @since 2024-09-13 19:58-01
 */
@Service
public class MinioService implements OssService, InitializingBean {

    @Resource
    private MinioConfiguration minioConfiguration;

    private MinioClient minioClient;

    private MinioService() {
    }

    @Override
    public void afterPropertiesSet() {
        String endpoint = minioConfiguration.getEndpoint();
        String accessKey = minioConfiguration.getAccessKey();
        String secretKey = minioConfiguration.getSecretKey();
        this.minioClient = MinioClient.builder()
                .endpoint(endpoint)
                .credentials(accessKey, secretKey)
                .build();
    }

    @Override
    public void upload(byte[] bytes, FileBizTag bizTag, String path, String contentType) {
        try (InputStream inputStream = new ByteArrayInputStream(bytes)) {
            PutObjectArgs uploadObjectArgs = PutObjectArgs.builder()
                    .bucket(bizTag.getBucket())
                    .object(path)
                    .contentType(contentType)
                    .stream(inputStream, inputStream.available(), -1)
                    .build();
            minioClient.putObject(uploadObjectArgs);
        } catch (Exception e) {
            throw new BusinessException(ResponseCode.OPERATION_ERROR, "上传文件失败");
        }
    }

    @Override
    public String getPresignedObjectUrl(String bucketName, String objectName, String objectPath) {
        try {
            GetPresignedObjectUrlArgs getPresignedObjectUrlArgs = GetPresignedObjectUrlArgs.builder()
                    .method(Method.GET)
                    .bucket(bucketName)
                    .object(objectPath + objectName)
                    .expiry(1, TimeUnit.MINUTES)
                    .build();
            return minioClient.getPresignedObjectUrl(getPresignedObjectUrlArgs);
        } catch (Exception e) {
            throw new BusinessException(ResponseCode.OPERATION_ERROR, "获取文件失败");
        }
    }

    @Override
    public void remove(String bucketName, String objectName, String objectPath) {
        try {
            RemoveObjectArgs removeObjectArgs = RemoveObjectArgs.builder()
                    .bucket(bucketName)
                    .object(objectPath + objectName)
                    .build();
            minioClient.removeObject(removeObjectArgs);
        } catch (Exception e) {
            throw new BusinessException(ResponseCode.OPERATION_ERROR, "删除文件失败");
        }
    }

}
