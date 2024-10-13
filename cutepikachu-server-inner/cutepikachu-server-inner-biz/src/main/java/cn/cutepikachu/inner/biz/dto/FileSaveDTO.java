package cn.cutepikachu.inner.biz.dto;

import cn.cutepikachu.common.model.biz.enums.FileBizTag;
import lombok.Data;

/**
 * 内部服务 - 文件保存 DTO
 *
 * @author <a href="https://github.com/cutepikachu-cn">笨蛋皮卡丘</a>
 * @version 1.0
 * @since 2024-10-13 16:27-22
 */
@Data
public class FileSaveDTO {

    /**
     * 文件字节数组
     */
    byte[] bytes;

    /**
     * 业务标识
     */
    FileBizTag fileBizTag;

    /**
     * 文件名
     */
    String fileName;

    /**
     * 文件路径
     */
    String path;

    /**
     * 文件类型
     */
    String contentType;

}
