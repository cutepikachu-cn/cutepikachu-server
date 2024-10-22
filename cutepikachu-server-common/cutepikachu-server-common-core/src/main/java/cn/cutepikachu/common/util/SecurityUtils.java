package cn.cutepikachu.common.util;

import cn.cutepikachu.common.model.user.entity.UserInfo;
import cn.dev33.satoken.session.SaSession;
import cn.dev33.satoken.stp.StpUtil;

import static cn.cutepikachu.common.constant.SecurityConstant.SESSION_LOGIN_USER;

/**
 * 认证鉴权工具类
 *
 * @author <a href="https://github.com/cutepikachu-cn">笨蛋皮卡丘</a>
 * @version 1.0
 * @since 2024-10-22 17:11-14
 */
public class SecurityUtils {

    /**
     * 设置登录用户信息
     *
     * @param userInfo 登录用户信息
     */
    public static void setLoginUser(UserInfo userInfo) {
        SaSession session = StpUtil.getSession();
        session.set(SESSION_LOGIN_USER, userInfo);
    }

    /**
     * 获取登录用户信息
     *
     * @return 登录用户信息
     */
    public static UserInfo getLoginUser() {
        SaSession session = StpUtil.getSession();
        UserInfo userInfo = session.getModel(SESSION_LOGIN_USER, UserInfo.class);
        return userInfo;
    }

    /**
     * 获取登录用户 ID
     *
     * @return 登录用户 ID
     */
    public static Long getLoginUserId() {
        UserInfo userInfo = getLoginUser();
        return userInfo == null ? null : userInfo.getUserId();
    }


}
