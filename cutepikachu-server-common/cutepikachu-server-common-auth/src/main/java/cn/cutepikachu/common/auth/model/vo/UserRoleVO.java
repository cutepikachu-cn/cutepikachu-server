package cn.cutepikachu.common.auth.model.vo;

import cn.cutepikachu.common.auth.model.entity.UserRole;
import cn.cutepikachu.common.model.BaseVO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 用户角色关联表 VO
 *
 * @author <a href="https://github.com/cutepikachu-cn">笨蛋皮卡丘</a>
 * @version 0.0.1-SNAPSHOT
 * @since 2024-08-01 19:22:35
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRoleVO extends BaseVO<UserRole, UserRoleVO> implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 关联主键 id
     */
    private Long id;

    /**
     * 用户 id
     */
    private Long userId;

    /**
     * 角色 id
     */
    private Long roleId;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

}
