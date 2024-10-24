package cn.cutepikachu.biz.service;

import cn.cutepikachu.common.model.biz.entity.FileInfo;
import cn.cutepikachu.common.model.biz.enums.FileBizTag;
import cn.cutepikachu.common.model.biz.vo.FileInfoVO;
import org.springframework.web.multipart.MultipartFile;

/**
 * 文件信息表 服务类
 *
 * @author <a href="https://github.com/cutepikachu-cn">笨蛋皮卡丘</a>
 * @version 0.0.1-SNAPSHOT
 * @since 2024-09-13 16:23:28
 */
public interface IFileInfoService {

    FileInfoVO uploadFile(MultipartFile file, FileBizTag bizTag);

    FileInfo uploadFile(byte[] bytes, FileBizTag bizTag, String fileName, String contentType);

}
