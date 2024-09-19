package cn.cutepikachu.common.auth.model.entity;

import cn.cutepikachu.common.auth.model.vo.RoleVO;
import cn.cutepikachu.common.model.BaseEntity;
import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 认证角色表
 *
 * @author <a href="https://github.com/cutepikachu-cn">笨蛋皮卡丘</a>
 * @version 0.0.1-SNAPSHOT
 * @since 2024-08-01 19:22:35
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "`role`", autoResultMap = true)
public class Role extends BaseEntity<Role, RoleVO> implements Serializable {

    @Serial
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    /**
     * 角色 id
     */
    @TableId(value = "`role_id`")
    private Long roleId;

    /**
     * 角色名称
     */
    @TableField("`role_name`")
    private String roleName;

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

    public static final String ROLE_ID = "role_id";

    public static final String ROLE_NAME = "role_name";

    public static final String CREATE_TIME = "create_time";

    public static final String UPDATE_TIME = "update_time";

    public static final String IS_DELETE = "is_delete";

}
