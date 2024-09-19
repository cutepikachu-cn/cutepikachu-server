package cn.cutepikachu.common.auth.model.entity;

import cn.cutepikachu.common.auth.model.vo.AuthAccountVO;
import cn.cutepikachu.common.model.BaseEntity;
import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

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
public class AuthAccount extends BaseEntity<AuthAccount, AuthAccountVO> implements Serializable {

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
     * 用户状态（0 禁用；1 启用）
     */
    @TableField("`status`")
    private Byte status;

    /**
     * 创建 ip
     */
    @TableField("`create_ip`")
    private String createIp;

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

    public static final String USER_ID = "user_id";

    public static final String USERNAME = "username";

    public static final String PASSWORD = "password";

    public static final String STATUS = "status";

    public static final String CREATE_IP = "create_ip";

    public static final String CREATE_TIME = "create_time";

    public static final String UPDATE_TIME = "update_time";

    public static final String IS_DELETE = "is_delete";

}
