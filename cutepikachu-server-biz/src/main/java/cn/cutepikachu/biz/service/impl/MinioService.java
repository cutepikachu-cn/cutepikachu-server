package cn.cutepikachu.biz.service.impl;

import cn.cutepikachu.biz.config.MinioConfiguration;
import cn.cutepikachu.biz.service.OssService;
import cn.cutepikachu.biz.service.factory.OssServiceFactory;
import cn.cutepikachu.common.model.biz.enums.OssType;
import cn.cutepikachu.common.response.ErrorCode;
import io.minio.*;
import io.minio.http.Method;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;

import static cn.cutepikachu.common.exception.ExceptionFactory.sysException;

/**
 * Minio 对象存储服务类
 *
 * @author <a href="https://github.com/cutepikachu-cn">笨蛋皮卡丘</a>
 * @version 1.0
 * @since 2024-09-13 19:58-01
 */
public class MinioService implements OssService {

    private final MinioClient minioClient;

    private final MinioConfiguration minioConfiguration;

    public MinioService(MinioConfiguration minioConfiguration, OssServiceFactory ossServiceFactory) {
        this.minioConfiguration = minioConfiguration;
        String endpoint = minioConfiguration.getEndpoint();
        String accessKey = minioConfiguration.getAccessKey();
        String secretKey = minioConfiguration.getSecretKey();
        this.minioClient = MinioClient.builder()
                .endpoint(endpoint)
                .credentials(accessKey, secretKey)
                .build();
        ossServiceFactory.registerOssService(OssType.MINIO, this);
    }

    @Override
    public OssType getSelfOssType() {
        return OssType.MINIO;
    }

    @Override
    public String getEndpoint() {
        return minioConfiguration.getEndpoint();
    }

    @Override
    public void upload(byte[] bytes, String bucket, String path, String contentType) {
        this.existsBucket(bucket);
        try (InputStream inputStream = new ByteArrayInputStream(bytes)) {
            PutObjectArgs uploadObjectArgs = PutObjectArgs.builder()
                    .bucket(bucket)
                    .object(path)
                    .contentType(contentType)
                    .stream(inputStream, inputStream.available(), -1)
                    .build();
            minioClient.putObject(uploadObjectArgs);
        } catch (Exception e) {
            throw sysException(ErrorCode.INTERNAL_SERVER_ERROR, "上传文件失败");
        }
    }

    @Override
    public boolean remove(String bucket, String objectPath) {
        try {
            RemoveObjectArgs removeObjectArgs = RemoveObjectArgs.builder()
                    .bucket(bucket)
                    .object(objectPath)
                    .build();
            minioClient.removeObject(removeObjectArgs);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean makeBucket(String bucket) {
        if (this.existsBucket(bucket)) {
            return true;
        }
        try {
            MakeBucketArgs makeBucketArgs = MakeBucketArgs.builder()
                    .bucket(bucket)
                    .build();
            minioClient.makeBucket(makeBucketArgs);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean existsBucket(String bucket) {
        try {
            BucketExistsArgs bucketExistsArgs = BucketExistsArgs.builder()
                    .bucket(bucket)
                    .build();
            return minioClient.bucketExists(bucketExistsArgs);
        } catch (Exception e) {
            throw sysException(ErrorCode.INTERNAL_SERVER_ERROR, "检查 Bucket 是否存在失败");
        }
    }

    @Override
    public String getPresignedObjectUrl(String bucketName, String objectPath) {
        try {
            GetPresignedObjectUrlArgs getPresignedObjectUrlArgs = GetPresignedObjectUrlArgs.builder()
                    .method(Method.GET)
                    .bucket(bucketName)
                    .object(objectPath)
                    .expiry(1, TimeUnit.MINUTES)
                    .build();
            return minioClient.getPresignedObjectUrl(getPresignedObjectUrlArgs);
        } catch (Exception e) {
            throw sysException(ErrorCode.INTERNAL_SERVER_ERROR, "获取预签名 URL 失败");
        }
    }

}
