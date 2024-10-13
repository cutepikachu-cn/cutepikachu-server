package cn.cutepikachu.biz.controller;

import cn.cutepikachu.biz.model.entity.FileInfo;
import cn.cutepikachu.biz.model.enums.FileBizTag;
import cn.cutepikachu.biz.model.enums.OssType;
import cn.cutepikachu.biz.model.vo.FileInfoVO;
import cn.cutepikachu.biz.service.IFileInfoService;
import cn.cutepikachu.biz.service.OssService;
import cn.cutepikachu.biz.service.factory.OssServiceFactory;
import cn.cutepikachu.biz.util.OssUtils;
import cn.cutepikachu.common.response.ResponseCode;
import cn.cutepikachu.common.response.ResponseEntity;
import cn.cutepikachu.common.util.ResponseUtils;
import cn.cutepikachu.common.util.ThrowUtils;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

/**
 * 文件服务 对外接口
 *
 * @author <a href="https://github.com/cutepikachu-cn">笨蛋皮卡丘</a>
 * @version 1.0
 * @since 2024-09-13 22:54-51
 */
@RestController
public class OssController {

    @Resource
    private IFileInfoService fileInfoService;

    @Resource
    private OssServiceFactory ossServiceFactory;

    private OssService ossService;

    /**
     * 构建文件信息
     *
     * @param file    文件
     * @param bizTag  文件业务标签
     * @param ossType 文件存储类型
     * @return 文件信息
     */
    private FileInfo buildFileInfo(MultipartFile file, FileBizTag bizTag, OssType ossType) {
        FileInfo fileInfo = new FileInfo();
        String path = OssUtils.getObjectPath(bizTag);
        fileInfo.setOssType(ossType.getValue())
                .setBucket(bizTag.getBucket())
                .setName(file.getOriginalFilename())
                .setBizTag(bizTag.getValue())
                .setSize(file.getSize())
                .setType(file.getContentType())
                .setPath(path);
        return fileInfo;
    }

    /**
     * 校验文件类型
     *
     * @param file       文件
     * @param fileBizTag 文件业务标签
     * @return 是否是有效的文件类型
     */
    private boolean isValidFileType(MultipartFile file, FileBizTag fileBizTag) throws IOException {
        // 校验 Content-Type
        String contentType = file.getContentType();
        if (contentType == null) {
            return false;
        }
        Map<String, String> fileMagicNumberMap = fileBizTag.getFileMagicNumberMap();
        boolean valid = fileMagicNumberMap.keySet().stream().anyMatch(contentType::contains);
        if (!valid) {
            return false;
        }
        // 校验文件头魔数
        byte[] bytes = file.getBytes();
        StringBuilder headerHex = new StringBuilder();
        for (int i = 0; i < bytes.length && i < 8; i++) {
            headerHex.append(String.format("%02X", bytes[i]));
        }
        String headerHexString = headerHex.toString();
        valid = fileMagicNumberMap.values().stream().anyMatch(headerHexString::startsWith);
        return valid;
    }

    @PostMapping("/upload_image")
    public ResponseEntity<FileInfoVO> uploadImage(@RequestParam MultipartFile file) throws IOException {
        ThrowUtils.throwIf(file.isEmpty(), ResponseCode.BAD_REQUEST, "文件为空");
        // 文件类型校验
        ThrowUtils.throwIf(!isValidFileType(file, FileBizTag.IMAGE_OTHER), ResponseCode.BAD_REQUEST, "文件类型错误");
        // 创建文件信息
        FileBizTag fileBizTag = FileBizTag.IMAGE_OTHER;
        // todo 临时
        OssType ossType = OssType.MINIO;
        FileInfo fileInfo = buildFileInfo(file, fileBizTag, ossType);
        // 上传文件到 OSS
        ossService = ossServiceFactory.getDefaultOssService();
        ossService.upload(file.getBytes(), fileBizTag, fileInfo.getPath(), fileInfo.getType());
        // 保存文件信息
        FileInfoVO fileInfoVO = fileInfoService.saveFileInfo(fileInfo);
        return ResponseUtils.success(fileInfoVO);
    }

}
