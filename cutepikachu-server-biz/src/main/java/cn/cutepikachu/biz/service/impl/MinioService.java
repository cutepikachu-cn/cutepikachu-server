package cn.cutepikachu.biz.service.impl;

import cn.cutepikachu.biz.config.MinioConfiguration;
import cn.cutepikachu.biz.model.enums.OssType;
import cn.cutepikachu.biz.service.OssService;
import cn.cutepikachu.biz.service.factory.OssServiceFactory;
import cn.cutepikachu.common.exception.BusinessException;
import cn.cutepikachu.common.response.ResponseCode;
import io.minio.*;
import io.minio.http.Method;

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
public class MinioService implements OssService {

    private final MinioClient minioClient;

    public MinioService(MinioConfiguration minioConfiguration, OssServiceFactory ossServiceFactory) {
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
    public void upload(byte[] bytes, String bucket, String path, String contentType) {
        this.existsBucket(bucket, true);
        try (InputStream inputStream = new ByteArrayInputStream(bytes)) {
            PutObjectArgs uploadObjectArgs = PutObjectArgs.builder()
                    .bucket(bucket)
                    .object(path)
                    .contentType(contentType)
                    .stream(inputStream, inputStream.available(), -1)
                    .build();
            minioClient.putObject(uploadObjectArgs);
        } catch (Exception e) {
            throw new BusinessException(ResponseCode.INTERNAL_SERVER_ERROR, "上传文件失败");
        }
    }

    @Override
    public void remove(String bucket, String objectPath) {
        this.existsBucket(bucket, true);
        try {
            RemoveObjectArgs removeObjectArgs = RemoveObjectArgs.builder()
                    .bucket(bucket)
                    .object(objectPath)
                    .build();
            minioClient.removeObject(removeObjectArgs);
        } catch (Exception e) {
            throw new BusinessException(ResponseCode.INTERNAL_SERVER_ERROR, "删除文件失败");
        }
    }

    @Override
    public void makeBucket(String bucket) {
        try {
            MakeBucketArgs makeBucketArgs = MakeBucketArgs.builder()
                    .bucket(bucket)
                    .build();
            minioClient.makeBucket(makeBucketArgs);
        } catch (Exception e) {
            throw new BusinessException(ResponseCode.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public boolean existsBucket(String bucket, boolean makeIfNotExists) {
        try {
            BucketExistsArgs bucketExistsArgs = BucketExistsArgs.builder()
                    .bucket(bucket)
                    .build();
            boolean exists = minioClient.bucketExists(bucketExistsArgs);
            if (!exists && makeIfNotExists) {
                makeBucket(bucket);
            }
            return makeIfNotExists || exists;
        } catch (Exception e) {
            throw new BusinessException(ResponseCode.INTERNAL_SERVER_ERROR);
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
            throw new BusinessException(ResponseCode.INTERNAL_SERVER_ERROR, "获取文件失败");
        }
    }

}
