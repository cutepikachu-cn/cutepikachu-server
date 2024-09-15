package cn.cutepikachu.biz.service.impl;

import cn.cutepikachu.biz.config.MinIOConfiguration;
import cn.cutepikachu.biz.service.OssService;
import cn.cutepikachu.biz.util.OssUtils;
import cn.cutepikachu.common.exception.BusinessException;
import cn.cutepikachu.common.response.ResponseCode;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.RemoveObjectArgs;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

/**
 * @author <a href="https://github.com/cutepikachu-cn">笨蛋皮卡丘</a>
 * @version 1.0
 * @since 2024-09-13 19:58-01
 */
@Service
@Slf4j
public class MinIOService implements OssService, InitializingBean {

    @Resource
    private MinIOConfiguration minIOConfiguration;

    private MinioClient minioClient;

    private MinIOService() {
    }

    @Override
    public void afterPropertiesSet() {
        String endpoint = minIOConfiguration.getEndpoint();
        String accessKey = minIOConfiguration.getAccessKey();
        String secretKey = minIOConfiguration.getSecretKey();
        this.minioClient = MinioClient.builder()
                .endpoint(endpoint)
                .credentials(accessKey, secretKey)
                .build();
    }

    @Override
    public void upload(String bucketName, String objectName, MultipartFile file) {
        try (InputStream inputStream = new ByteArrayInputStream(file.getBytes())) {
            String filePath = OssUtils.getFilePath();
            PutObjectArgs uploadObjectArgs = PutObjectArgs.builder()
                    .bucket(bucketName)
                    .object(filePath)
                    .contentType(file.getContentType())
                    .stream(inputStream, inputStream.available(), -1)
                    .build();
            minioClient.putObject(uploadObjectArgs);
        } catch (Exception e) {
            throw new BusinessException(ResponseCode.OPERATION_ERROR, "上传文件失败");
        }
    }

    @Override
    public void remove(String bucketName, String objectName) {
        try {
            RemoveObjectArgs removeObjectArgs = RemoveObjectArgs.builder()
                    .bucket(bucketName)
                    .object(objectName)
                    .build();
            minioClient.removeObject(removeObjectArgs);
        } catch (Exception e) {
            throw new BusinessException(ResponseCode.OPERATION_ERROR, "删除文件失败");
        }
    }

}
