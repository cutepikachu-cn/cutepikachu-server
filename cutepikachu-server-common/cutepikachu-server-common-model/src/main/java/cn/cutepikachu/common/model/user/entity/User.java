package cn.cutepikachu.common.model.user.entity;

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
public class User extends BaseEntity implements Serializable {

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

    public static final String USER_ID = "user_id";

    public static final String NICK_NAME = "nick_name";

    public static final String AVATAR_URL = "avatar_url";

}
