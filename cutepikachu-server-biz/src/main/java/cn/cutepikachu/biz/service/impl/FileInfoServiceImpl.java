package cn.cutepikachu.biz.service.impl;

import cn.cutepikachu.biz.mapper.FileInfoMapper;
import cn.cutepikachu.biz.model.convert.FileInfoConvert;
import cn.cutepikachu.biz.service.IFileInfoService;
import cn.cutepikachu.biz.service.OssService;
import cn.cutepikachu.biz.service.factory.OssServiceFactory;
import cn.cutepikachu.biz.util.OssUtils;
import cn.cutepikachu.common.constant.DistributedBizTag;
import cn.cutepikachu.common.model.biz.entity.FileInfo;
import cn.cutepikachu.common.model.biz.enums.FileBizTag;
import cn.cutepikachu.common.model.biz.enums.OssType;
import cn.cutepikachu.common.model.biz.vo.FileInfoVO;
import cn.cutepikachu.common.response.BaseResponse;
import cn.cutepikachu.common.response.ErrorCode;
import cn.cutepikachu.inner.leaf.DistributedIdInnerService;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

import static cn.cutepikachu.common.exception.ExceptionFactory.bizException;
import static cn.cutepikachu.common.exception.ExceptionFactory.sysException;

/**
 * 文件信息表 服务实现类
 *
 * @author <a href="https://github.com/cutepikachu-cn">笨蛋皮卡丘</a>
 * @version 0.0.1-SNAPSHOT
 * @since 2024-09-13 16:23:28
 */
@Service
public class FileInfoServiceImpl extends ServiceImpl<FileInfoMapper, FileInfo> implements IFileInfoService {

    @Resource
    private DistributedIdInnerService distributedIdInnerService;

    @Resource
    private OssServiceFactory ossServiceFactory;

    private static final FileInfoConvert FILE_INFO_CONVERT = FileInfoConvert.INSTANCE;

    @Override
    public FileInfoVO uploadFile(MultipartFile file, FileBizTag bizTag) {
        // 校验文件类型
        this.checkFileType(file, bizTag);

        byte[] bytes;
        try {
            bytes = file.getBytes();
        } catch (IOException e) {
            throw sysException(ErrorCode.INTERNAL_SERVER_ERROR, "读取文件失败");
        }

        FileInfo fileInfo = this.uploadFile(bytes, bizTag, file.getOriginalFilename(), file.getContentType());

        return FILE_INFO_CONVERT.convert(fileInfo);
    }

    @Override
    public FileInfo uploadFile(byte[] bytes, FileBizTag bizTag, String fileName, String contentType) {
        // 获取 OSS 服务
        // todo 暂时只支持 Minio
        OssService ossService = ossServiceFactory.getDefaultOssService();

        // 构建文件信息
        FileInfo fileInfo = this.buildFileInfo(bizTag, ossService.getSelfOssType(), fileName, (long) bytes.length, contentType);

        // 上传文件
        ossService.upload(bytes, fileInfo.getBucket(), fileInfo.getPath(), fileInfo.getType());

        // 保存文件信息
        BaseResponse<Long> resp = distributedIdInnerService.getDistributedID(DistributedBizTag.FILE);
        resp.check();
        fileInfo.setFileId(resp.getData())
                .setEndpoint(ossService.getEndpoint());
        boolean saveFileInfoSuccess = this.save(fileInfo);
        if (!saveFileInfoSuccess) {
            throw sysException(ErrorCode.INTERNAL_SERVER_ERROR, "保存文件信息失败");
        }
        return fileInfo;
    }

    /**
     * 校验文件类型
     *
     * @param file       文件
     * @param fileBizTag 文件业务标签
     */
    private void checkFileType(MultipartFile file, FileBizTag fileBizTag) {
        // 校验 Content-Type
        String contentType = file.getContentType();
        if (StrUtil.isBlank(contentType)) {
            throw bizException(ErrorCode.BAD_REQUEST, "文件类型不合法");
        }

        // 校验文件类型
        Map<String, String> fileMagicNumberMap = fileBizTag.getFileMagicNumberMap();
        boolean isValidContentType = fileMagicNumberMap.keySet().stream().anyMatch(contentType::contains);
        if (!isValidContentType) {
            throw bizException(ErrorCode.BAD_REQUEST, "文件类型不合法");
        }

        // 校验文件头魔数
        byte[] bytes;
        try {
            bytes = file.getBytes();
        } catch (IOException e) {
            throw sysException(ErrorCode.INTERNAL_SERVER_ERROR, e);
        }
        StringBuilder headerHex = new StringBuilder();
        for (int i = 0; i < bytes.length && i < 8; i++) {
            headerHex.append(String.format("%02X", bytes[i]));
        }
        String headerHexString = headerHex.toString();
        boolean isValidMagicNumber = fileMagicNumberMap.values().stream().anyMatch(headerHexString::startsWith);
        if (!isValidMagicNumber) {
            throw bizException(ErrorCode.BAD_REQUEST, "文件类型不合法");
        }
    }

    /**
     * 构建文件信息对象
     *
     * @param bizTag      业务标识
     * @param ossType     存储类型
     * @param fileName    文件名
     * @param fileSize    文件大小
     * @param contentType 文件类型
     * @return 文件信息
     */
    private FileInfo buildFileInfo(FileBizTag bizTag, OssType ossType, String fileName, Long fileSize, String contentType) {
        FileInfo fileInfo = new FileInfo();
        String path = OssUtils.getObjectPath(bizTag);
        fileInfo.setOssType(ossType)
                .setBucket(bizTag.getBucket())
                .setName(fileName)
                .setBizTag(bizTag.name())
                .setSize(fileSize)
                .setType(contentType)
                .setPath(path);
        return fileInfo;
    }

}
