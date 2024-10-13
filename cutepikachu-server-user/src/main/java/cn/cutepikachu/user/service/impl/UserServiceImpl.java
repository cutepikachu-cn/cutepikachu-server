package cn.cutepikachu.user.service.impl;

import cn.cutepikachu.common.exception.BusinessException;
import cn.cutepikachu.common.model.auth.entity.AuthAccount;
import cn.cutepikachu.common.model.user.entity.User;
import cn.cutepikachu.common.model.user.vo.UserInfoVO;
import cn.cutepikachu.common.response.ResponseCode;
import cn.cutepikachu.common.response.ResponseEntity;
import cn.cutepikachu.common.security.util.PasswordUtil;
import cn.cutepikachu.common.util.BeanUtils;
import cn.cutepikachu.common.util.RegularExpressionUtils;
import cn.cutepikachu.common.util.ResponseUtils;
import cn.cutepikachu.common.util.ThrowUtils;
import cn.cutepikachu.inner.auth.AuthInnerService;
import cn.cutepikachu.user.mapper.UserMapper;
import cn.cutepikachu.user.model.dto.UserUpdateDTO;
import cn.cutepikachu.user.service.IUserService;
import cn.dev33.satoken.session.SaSession;
import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

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

    /**
     * 校验 User 对象信息
     *
     * @param user 用户信息
     */
    private void verifyUser(User user) {
        String nickName = user.getNickName();
        if (!RegularExpressionUtils.isValidNickName(nickName)) {
            throw new BusinessException(ResponseCode.BAD_REQUEST, "昵称不合法");
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
        String cryptoPassword = PasswordUtil.crypto(password);
        ResponseEntity<AuthAccount> resp = authInnerService.getAuthAccountByUsernameAndPassword(username, cryptoPassword);
        ResponseUtils.throwIfNotSuccess(resp);
        AuthAccount authAccount = resp.getData();
        Long userId = authAccount.getUserId();
        User user = this.getById(userId);
        UserInfoVO userInfoVO = user.toUserInfoVO(authAccount);
        // 存储登录状态
        StpUtil.login(userId);
        SaSession session = StpUtil.getSession();
        session.set("user_info", userInfoVO);
        return userInfoVO;
    }

    @Override
    public UserInfoVO updateUserInfo(UserUpdateDTO userUpdateDTO) {
        // 获取登录用户信息
        SaSession session = StpUtil.getSession();
        UserInfoVO userInfo = session.getModel("user_info", UserInfoVO.class);
        // 校验用户信息
        User user = BeanUtils.copyProperties(userInfo, User.class);
        BeanUtils.copyProperties(userUpdateDTO, user);
        this.verifyUser(user);
        // 更新用户信息
        ThrowUtils.throwIf(!this.updateById(user), new BusinessException(ResponseCode.INTERNAL_SERVER_ERROR, "更新用户信息失败"));
        BeanUtils.copyProperties(this.getById(user.getUserId()), userInfo);
        // 更新登录状态
        session.set("user_info", userInfo);
        return userInfo;
    }

}
