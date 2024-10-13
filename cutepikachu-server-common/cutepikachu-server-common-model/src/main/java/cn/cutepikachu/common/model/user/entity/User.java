package cn.cutepikachu.common.model.user.entity;

import cn.cutepikachu.common.model.BaseEntity;
import cn.cutepikachu.common.model.auth.entity.AuthAccount;
import cn.cutepikachu.common.model.user.vo.UserInfoVO;
import cn.cutepikachu.common.model.user.vo.UserVO;
import cn.cutepikachu.common.util.BeanUtils;
import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 用户表
 *
 * @author <a href="https://github.com/cutepikachu-cn">笨蛋皮卡丘</a>
 * @version 0.0.1-SNAPSHOT
 * @since 2024-08-01 19:21:35
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "`user`", autoResultMap = true)
public class User extends BaseEntity<User, UserVO> implements Serializable {

    @Serial
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    /**
     * 用户 id
     */
    @TableId(value = "`user_id`")
    private Long userId;

    /**
     * 用户昵称
     */
    @TableField("`nick_name`")
    private String nickName;

    /**
     * 用户头像 URL
     */
    @TableField("`avatar_url`")
    private String avatarUrl;

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

    public static final String NICK_NAME = "nick_name";

    public static final String AVATAR_URL = "avatar_url";

    public static final String CREATE_TIME = "create_time";

    public static final String UPDATE_TIME = "update_time";

    public static final String IS_DELETE = "is_delete";

    public UserInfoVO toUserInfoVO(AuthAccount authAccount) {
        UserInfoVO userInfoVO = new UserInfoVO();
        BeanUtils.copyProperties(this, userInfoVO);
        BeanUtils.copyProperties(authAccount, userInfoVO);
        return userInfoVO;
    }

}
