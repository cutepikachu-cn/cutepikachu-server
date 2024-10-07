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
    private OssService ossService;

    // 文件魔数
    private static final Map<String, String> IMAGE_MAGIC_NUMBER = Map.of(
            "jpg", "FFD8FF",
            "jpeg", "FFD8FF",
            "png", "89504E47"
    );

    public OssController(OssServiceFactory ossServiceFactory) {
        this.ossService = ossServiceFactory.getOssService(OssType.MINIO);
    }

    private FileInfo getFileInfo(MultipartFile file, FileBizTag bizTag) {
        FileInfo fileInfo = new FileInfo();
        String path = OssUtils.getObjectPath(bizTag);
        fileInfo.setName(file.getName())
                .setBizTag(bizTag.getValue())
                .setSize(file.getSize())
                .setType(file.getContentType())
                .setPath(path);
        return fileInfo;
    }

    private boolean isValidFileType(MultipartFile file, Map<String, String> magicNumberMap) throws IOException {
        // 校验 Content-Type
        String contentType = file.getContentType();
        if (contentType == null) {
            return false;
        }
        boolean valid = magicNumberMap.keySet().stream().anyMatch(contentType::contains);
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
        valid = magicNumberMap.values().stream().anyMatch(headerHexString::startsWith);
        return valid;
    }

    @PostMapping("/upload_image")
    public ResponseEntity<FileInfoVO> uploadImage(@RequestParam MultipartFile file) throws IOException {
        ThrowUtils.throwIf(file.isEmpty(), ResponseCode.BAD_REQUEST, "文件为空");
        // 判断文件是否为 jpg / jpeg / png 类型图片
        ThrowUtils.throwIf(!isValidFileType(file, IMAGE_MAGIC_NUMBER), ResponseCode.BAD_REQUEST, "文件类型错误");
        FileInfo fileInfo = getFileInfo(file, FileBizTag.IMAGE_OTHER);
        ossService.upload(file.getBytes(), FileBizTag.IMAGE_OTHER, fileInfo.getPath(), fileInfo.getType());
        FileInfoVO fileInfoVO = fileInfoService.saveFile(fileInfo);
        return ResponseUtils.success(fileInfoVO);
    }

}
