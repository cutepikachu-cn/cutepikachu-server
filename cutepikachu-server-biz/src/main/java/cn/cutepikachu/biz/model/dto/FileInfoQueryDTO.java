package cn.cutepikachu.biz.model.dto;

import cn.cutepikachu.common.request.PageDTO;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 文件信息表 查询 DTO
 *
 * @author <a href="https://github.com/cutepikachu-cn">笨蛋皮卡丘</a>
 * @version 0.0.1-SNAPSHOT
 * @since 2024-09-13 16:23:28
 */
@Data
public class FileInfoQueryDTO extends PageDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 文件 id
     */
    private Long fileId;

    /**
     * 文件路径
     */
    private String path;

    /**
     * 文件名
     */
    private String name;

    /**
     * 文件大小
     */
    private Long size;

    /**
     * 文件类型
     */
    private String type;

    /**
     * 业务标识
     */
    private String bizTag;

}
