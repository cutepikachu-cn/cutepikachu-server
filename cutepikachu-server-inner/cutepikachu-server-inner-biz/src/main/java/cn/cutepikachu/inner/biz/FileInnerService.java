package cn.cutepikachu.inner.biz;

import cn.cutepikachu.common.model.biz.entity.FileInfo;
import cn.cutepikachu.common.response.BaseResponse;
import cn.cutepikachu.inner.biz.dto.FileSaveDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import static cn.cutepikachu.common.constant.FeignConstant.INNER_SERVICE_PREFIX;

/**
 * 文件内部服务
 *
 * @author <a href="https://github.com/cutepikachu-cn">笨蛋皮卡丘</a>
 * @version 1.0
 * @since 2024-10-13 16:17-27
 */
@FeignClient(name = "cutepikachu-server-biz")
public interface FileInnerService {

    @PostMapping(INNER_SERVICE_PREFIX + "/file/save")
    BaseResponse<FileInfo> saveFile(@RequestBody FileSaveDTO fileSaveDTO);

}
