package cn.cutepikachu.common.model.auth.entity;

import cn.cutepikachu.common.model.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * 用户角色关联表
 *
 * @author <a href="https://github.com/cutepikachu-cn">笨蛋皮卡丘</a>
 * @version 0.0.1-SNAPSHOT
 * @since 2024-08-01 19:22:35
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "`user_role`", autoResultMap = true)
public class UserRole extends BaseEntity implements Serializable {

    @Serial
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    /**
     * 关联主键 id
     */
    @TableId(value = "`id`")
    private Long id;

    /**
     * 用户 id
     */
    @TableField("`user_id`")
    private Long userId;

    /**
     * 角色 id
     */
    @TableField("`role_id`")
    private Long roleId;

    public static final String ID = "id";

    public static final String USER_ID = "user_id";

    public static final String ROLE_ID = "role_id";

}
