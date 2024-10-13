package cn.cutepikachu.biz.controller;

import cn.cutepikachu.biz.service.IFileInfoService;
import cn.cutepikachu.common.model.biz.enums.FileBizTag;
import cn.cutepikachu.common.model.biz.vo.FileInfoVO;
import cn.cutepikachu.common.response.ResponseCode;
import cn.cutepikachu.common.response.BaseResponse;
import cn.cutepikachu.common.util.ResponseUtils;
import cn.cutepikachu.common.util.ThrowUtils;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

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

    @PostMapping("/upload_image")
    public BaseResponse<FileInfoVO> uploadImage(@RequestParam MultipartFile file) {
        ThrowUtils.throwIf(file.isEmpty(), ResponseCode.BAD_REQUEST, "文件为空");
        // 上传文件并保存文件信息
        FileInfoVO fileInfoVO = fileInfoService.uploadFile(file, FileBizTag.IMAGE_OTHER);
        return ResponseUtils.success(fileInfoVO);
    }

}
