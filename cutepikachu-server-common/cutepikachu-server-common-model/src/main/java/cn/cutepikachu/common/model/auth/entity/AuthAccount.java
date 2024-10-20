package cn.cutepikachu.common.model.auth.entity;

import cn.cutepikachu.common.model.BaseEntity;
import cn.cutepikachu.common.model.auth.enums.AuthStatus;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * 认证账户表
 *
 * @author <a href="https://github.com/cutepikachu-cn">笨蛋皮卡丘</a>
 * @version 0.0.1-SNAPSHOT
 * @since 2024-08-01 19:22:35
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "`auth_account`", autoResultMap = true)
public class AuthAccount extends BaseEntity implements Serializable {

    @Serial
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    /**
     * 用户 id
     */
    @TableId(value = "`user_id`")
    private Long userId;

    /**
     * 用户名
     */
    @TableField("`username`")
    private String username;

    /**
     * 用户密码
     */
    @TableField("`password`")
    private String password;

    /**
     * 用户状态（DISABLE 禁用；ENABLE 启用）
     */
    @TableField("`status`")
    private AuthStatus status;

    /**
     * 创建 ip
     */
    @TableField("`create_ip`")
    private String createIp;

    public static final String USER_ID = "user_id";

    public static final String USERNAME = "username";

    public static final String PASSWORD = "password";

    public static final String STATUS = "status";

    public static final String CREATE_IP = "create_ip";

}
