package cn.cutepikachu.biz.controller;

import cn.cutepikachu.biz.factory.OssFactory;
import cn.cutepikachu.biz.model.vo.FileInfoVO;
import cn.cutepikachu.biz.service.IFileInfoService;
import cn.cutepikachu.common.exception.BusinessException;
import cn.cutepikachu.common.response.ResponseCode;
import cn.cutepikachu.common.response.ResponseEntity;
import cn.cutepikachu.common.util.ResponseUtils;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author <a href="https://github.com/cutepikachu-cn">笨蛋皮卡丘</a>
 * @version 1.0
 * @since 2024-09-13 22:54-51
 */
@RestController
public class FileController {

    @Resource
    private IFileInfoService fileInfoService;

    @Resource
    private OssFactory ossFactory;

    @PostMapping("/upload")
    public ResponseEntity<FileInfoVO> upload(@RequestParam MultipartFile file) {
        if (file.isEmpty()) {
            throw new BusinessException(ResponseCode.PARAMS_ERROR, "文件为空");
        }
        FileInfoVO fileInfoVO = new FileInfoVO();
        return ResponseUtils.success(fileInfoVO);
    }
}
