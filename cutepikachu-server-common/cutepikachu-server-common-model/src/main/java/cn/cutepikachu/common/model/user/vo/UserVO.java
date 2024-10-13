package cn.cutepikachu.common.model.user.vo;

import cn.cutepikachu.common.model.BaseVO;
import cn.cutepikachu.common.model.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * 用户表 VO
 *
 * @author <a href="https://github.com/cutepikachu-cn">笨蛋皮卡丘</a>
 * @version 0.0.1-SNAPSHOT
 * @since 2024-08-01 19:21:35
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserVO extends BaseVO<User, UserVO> implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 用户 id
     */
    private Long userId;

    /**
     * 用户昵称
     */
    private String nickName;

    /**
     * 用户头像 URL
     */
    private String avatarUrl;

}
