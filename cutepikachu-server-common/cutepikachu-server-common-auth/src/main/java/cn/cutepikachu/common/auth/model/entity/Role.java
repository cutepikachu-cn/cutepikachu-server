package cn.cutepikachu.common.auth.model.entity;

import cn.cutepikachu.common.auth.model.vo.RoleVO;
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
    @TableId(value = "`role_id`", type = IdType.ASSIGN_ID)
    private Long roleId;

    /**
     * 角色名称
     */
    @TableField("`role_name`")
    private String roleName;

    public static final String ROLE_ID = "role_id";

    public static final String ROLE_NAME = "role_name";

}
