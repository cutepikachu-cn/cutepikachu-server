package cn.cutepikachu.biz.model.dto;

import java.io.Serializable;
import java.io.Serial;
import lombok.Data;

/**
 *  更新 DTO
 *
 * @author <a href="https://github.com/cutepikachu-cn">笨蛋皮卡丘</a>
 * @version 0.0.1-SNAPSHOT
 * @since 2024-09-13 16:23:28
 */
@Data
public class FileInfoUpdateDTO implements Serializable {

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
    private Integer size;

    /**
     * 文件扩展名
     */
    private String extension;

    /**
     * 业务标识
     */
    private String bizTag;

}
