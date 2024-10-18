package cn.cutepikachu.user.service.impl;

import cn.cutepikachu.common.model.auth.entity.AuthAccount;
import cn.cutepikachu.common.model.user.convert.UserInfoConvert;
import cn.cutepikachu.common.model.user.entity.User;
import cn.cutepikachu.common.model.user.entity.UserInfo;
import cn.cutepikachu.common.model.user.vo.UserInfoVO;
import cn.cutepikachu.common.response.BaseResponse;
import cn.cutepikachu.common.response.ErrorCode;
import cn.cutepikachu.common.security.util.PasswordUtil;
import cn.cutepikachu.common.util.RegularExpressionUtils;
import cn.cutepikachu.inner.auth.AuthInnerService;
import cn.cutepikachu.user.mapper.UserMapper;
import cn.cutepikachu.user.model.convert.UserConvert;
import cn.cutepikachu.user.model.dto.UserUpdateDTO;
import cn.cutepikachu.user.service.IUserService;
import cn.dev33.satoken.session.SaSession;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import static cn.cutepikachu.common.exception.ExceptionFactory.bizException;
import static cn.cutepikachu.common.exception.ExceptionFactory.sysException;

/**
 * 用户表 服务实现类
 *
 * @author <a href="https://github.com/cutepikachu-cn">笨蛋皮卡丘</a>
 * @version 0.0.1-SNAPSHOT
 * @since 2024-08-01 19:21:35
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    @Resource
    private AuthInnerService authInnerService;

    @Resource
    private PasswordUtil passwordUtil;

    private static final UserConvert USER_CONVERT = UserConvert.INSTANCE;

    private static final UserInfoConvert USER_INFO_CONVERT = UserInfoConvert.INSTANCE;

    /**
     * 校验 User 对象信息
     *
     * @param user 用户信息
     */
    private void verifyUser(User user) {
        String nickName = user.getNickName();
        if (StrUtil.isBlank(nickName) || !RegularExpressionUtils.isValidNickName(nickName)) {
            throw bizException(ErrorCode.BAD_REQUEST, "昵称不合法");
        }
    }

    @Override
    public UserInfoVO getLoginUserInfo(String username, String password) {
        // 是否已登录
        if (StpUtil.isLogin()) {
            SaSession session = StpUtil.getSession();
            UserInfoVO userInfo = session.getModel("user_info", UserInfoVO.class);
            return userInfo;
        }
        // 未登录
        String cryptoPassword = passwordUtil.crypto(password);
        BaseResponse<AuthAccount> resp = authInnerService.getAuthAccountByUsernameAndPassword(username, cryptoPassword);
        resp.check();
        AuthAccount authAccount = resp.getData();
        Long userId = authAccount.getUserId();
        User user = this.getById(userId);
        UserInfo userInfo = USER_INFO_CONVERT.convert(user, authAccount);
        // 存储登录状态
        StpUtil.login(userId);
        SaSession session = StpUtil.getSession();
        session.set("user_info", userInfo);
        return USER_INFO_CONVERT.convert(userInfo);
    }

    @Override
    public UserInfoVO updateUserInfo(UserUpdateDTO userUpdateDTO) {
        // 获取登录用户信息
        SaSession session = StpUtil.getSession();
        UserInfoVO userInfo = session.getModel("user_info", UserInfoVO.class);
        // 校验用户信息
        User user = USER_CONVERT.convert(userInfo);
        USER_CONVERT.copy(userUpdateDTO, user);
        this.verifyUser(user);
        // 更新用户信息
        boolean updateSuccess = this.updateById(user);
        if (!updateSuccess) {
            throw sysException(ErrorCode.INTERNAL_SERVER_ERROR, "更新用户信息失败");
        }
        USER_CONVERT.copy(this.getById(user.getUserId()), userInfo);
        // 更新登录状态
        session.set("user_info", userInfo);
        return userInfo;
    }

}
