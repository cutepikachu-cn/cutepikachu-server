package cn.cutepikachu.biz.inner;

import cn.cutepikachu.biz.service.IFileInfoService;
import cn.cutepikachu.common.model.biz.entity.FileInfo;
import cn.cutepikachu.common.response.ResponseEntity;
import cn.cutepikachu.common.util.ResponseUtils;
import cn.cutepikachu.inner.biz.FileInnerService;
import cn.cutepikachu.inner.biz.dto.FileSaveDTO;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.RestController;

/**
 * 文件内部服务实现
 *
 * @author <a href="https://github.com/cutepikachu-cn">笨蛋皮卡丘</a>
 * @version 1.0
 * @since 2024-10-13 16:31-09
 */
@RestController
public class FileInnerServiceController implements FileInnerService {

    @Resource
    private IFileInfoService fileInfoService;

    @Override
    public ResponseEntity<FileInfo> saveFile(FileSaveDTO fileSaveDTO) {
        FileInfo fileInfo = fileInfoService.uploadFile(fileSaveDTO.getBytes(), fileSaveDTO.getFileBizTag(), fileSaveDTO.getFileName(), fileSaveDTO.getContentType());
        return ResponseUtils.success(fileInfo);
    }

}
