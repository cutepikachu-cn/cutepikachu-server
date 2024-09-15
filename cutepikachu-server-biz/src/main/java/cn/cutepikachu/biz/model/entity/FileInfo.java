package cn.cutepikachu.biz.model.entity;

import cn.cutepikachu.biz.model.vo.FileInfoVO;
import cn.cutepikachu.common.model.BaseEntity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * 文件信息表
 *
 * @author <a href="https://github.com/cutepikachu-cn">笨蛋皮卡丘</a>
 * @version 0.0.1-SNAPSHOT
 * @since 2024-09-13 16:23:28
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "`file_info`", autoResultMap = true)
public class FileInfo extends BaseEntity<FileInfo, FileInfoVO> implements Serializable {

    @Serial
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    /**
     * 文件 id
     */
    @TableId(value = "`file_id`", type = IdType.ASSIGN_ID)
    private Long fileId;

    /**
     * 文件路径
     */
    @TableField("`path`")
    private String path;

    /**
     * 文件名
     */
    @TableField("`name`")
    private String name;

    /**
     * 文件大小
     */
    @TableField("`size`")
    private Integer size;

    /**
     * 文件扩展名
     */
    @TableField("`extension`")
    private String extension;

    /**
     * 业务标识
     */
    @TableField("`biz_tag`")
    private String bizTag;

    public static final String FILE_ID = "file_id";

    public static final String PATH = "path";

    public static final String NAME = "name";

    public static final String SIZE = "size";

    public static final String EXTENSION = "extension";

    public static final String BIZ_TAG = "biz_tag";

}
