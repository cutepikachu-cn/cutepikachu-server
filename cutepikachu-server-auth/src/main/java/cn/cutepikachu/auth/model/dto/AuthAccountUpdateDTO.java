package cn.cutepikachu.auth.model.dto;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 认证账户表 更新 DTO
 *
 * @author <a href="https://github.com/cutepikachu-cn">笨蛋皮卡丘</a>
 * @version 0.0.1-SNAPSHOT
 * @since 2024-08-01 19:22:35
 */
@Data
public class AuthAccountUpdateDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 用户密码
     */
    private String password;

}
