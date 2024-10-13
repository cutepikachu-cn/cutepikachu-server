package cn.cutepikachu.common.model.auth.vo;

import cn.cutepikachu.common.model.BaseVO;
import cn.cutepikachu.common.model.auth.entity.AuthAccount;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 认证账户表 VO
 *
 * @author <a href="https://github.com/cutepikachu-cn">笨蛋皮卡丘</a>
 * @version 0.0.1-SNAPSHOT
 * @since 2024-08-01 19:22:35
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthAccountVO extends BaseVO<AuthAccount, AuthAccountVO> implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 用户 id
     */
    private Long userId;

    /**
     * 用户名
     */
    private String username;

    /**
     * 用户密码
     */
    private String password;

    /**
     * 用户状态（0 禁用；1 启用）
     */
    private Byte status;

    /**
     * 创建 ip
     */
    private String createIp;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

}
