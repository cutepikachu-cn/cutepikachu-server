package cn.cutepikachu.common.model.biz.entity;

import cn.cutepikachu.common.model.BaseEntity;
import cn.cutepikachu.common.model.biz.enums.FileBizTag;
import cn.cutepikachu.common.model.biz.enums.OssType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.ibatis.type.EnumTypeHandler;

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
public class FileInfo extends BaseEntity implements Serializable {

    @Serial
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    /**
     * 文件 id
     */
    @TableId(value = "`file_id`")
    private Long fileId;

    /**
     * OSS 类型
     */
    @TableField(value = "`oss_type`", typeHandler = EnumTypeHandler.class)
    private OssType ossType;

    /**
     * endpoint
     */
    @TableField("`endpoint`")
    private String endpoint;

    /**
     * bucket 名称
     */
    @TableField("`bucket`")
    private String bucket;

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
    private Long size;

    /**
     * 文件类型
     */
    @TableField("`type`")
    private String type;

    /**
     * 业务标识
     */
    @TableField(value = "`biz_tag`", typeHandler = EnumTypeHandler.class)
    private FileBizTag bizTag;

    public static final String FILE_ID = "file_id";

    public static final String PATH = "path";

    public static final String NAME = "name";

    public static final String SIZE = "size";

    public static final String TYPE = "type";

    public static final String BIZ_TAG = "biz_tag";

}
