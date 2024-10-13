package cn.cutepikachu.common.model.biz.entity;

import cn.cutepikachu.common.model.BaseEntity;
import cn.cutepikachu.common.model.biz.vo.FileInfoVO;
import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

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
    @TableId(value = "`file_id`")
    private Long fileId;

    /**
     * OSS 类型
     */
    @TableField("`oss_type`")
    private String ossType;

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
    @TableField("`biz_tag`")
    private String bizTag;

    /**
     * 创建时间
     */
    @TableField(value = "`create_time`", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField(value = "`update_time`", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    /**
     * 是否删除
     */
    @TableField("`is_delete`")
    @TableLogic
    private Boolean isDelete;

    public static final String FILE_ID = "file_id";

    public static final String PATH = "path";

    public static final String NAME = "name";

    public static final String SIZE = "size";

    public static final String TYPE = "type";

    public static final String BIZ_TAG = "biz_tag";

    public static final String CREATE_TIME = "create_time";

    public static final String UPDATE_TIME = "update_time";

    public static final String IS_DELETE = "is_delete";

    @Override
    public FileInfoVO toVO(Class<FileInfoVO> voClass) {
        FileInfoVO vo = super.toVO(voClass);
        vo.setPath("/" + this.getBucket() + this.getPath());
        return vo;
    }

}
