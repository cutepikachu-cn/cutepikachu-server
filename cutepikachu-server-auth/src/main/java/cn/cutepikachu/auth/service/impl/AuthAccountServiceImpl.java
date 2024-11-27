package cn.cutepikachu.auth.service.impl;

import cn.cutepikachu.auth.dao.repository.AuthAccountRepository;
import cn.cutepikachu.auth.dao.repository.UserRepository;
import cn.cutepikachu.auth.dao.repository.UserRoleRepository;
import cn.cutepikachu.auth.model.convert.AuthAccountConvert;
import cn.cutepikachu.auth.model.convert.UserConvert;
import cn.cutepikachu.auth.model.dto.AuthAccountUpdateDTO;
import cn.cutepikachu.auth.model.dto.UserRegisterDTO;
import cn.cutepikachu.auth.service.IAuthAccountService;
import cn.cutepikachu.common.constant.CommonConstant;
import cn.cutepikachu.common.constant.DistributedBizTag;
import cn.cutepikachu.common.model.auth.entity.AuthAccount;
import cn.cutepikachu.common.model.auth.entity.UserRole;
import cn.cutepikachu.common.model.auth.enums.AuthStatus;
import cn.cutepikachu.common.model.auth.enums.RoleEnum;
import cn.cutepikachu.common.model.user.convert.UserInfoConvert;
import cn.cutepikachu.common.model.user.entity.User;
import cn.cutepikachu.common.model.user.entity.UserInfo;
import cn.cutepikachu.common.model.user.vo.UserInfoVO;
import cn.cutepikachu.common.response.BaseResponse;
import cn.cutepikachu.common.response.ErrorCode;
import cn.cutepikachu.common.security.util.PasswordUtil;
import cn.cutepikachu.common.util.RegularExpressionUtils;
import cn.cutepikachu.common.util.SecurityUtils;
import cn.cutepikachu.inner.leaf.DistributedIdInnerService;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.StrUtil;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

import java.util.Objects;

import static cn.cutepikachu.common.exception.ExceptionFactory.bizException;

/**
 * 认证账户表 服务实现类
 *
 * @author <a href="https://github.com/cutepikachu-cn">笨蛋皮卡丘</a>
 * @version 0.0.1-SNAPSHOT
 * @since 2024-08-01 19:22:35
 */
@Service
public class AuthAccountServiceImpl implements IAuthAccountService {

    @Resource
    private AuthAccountRepository authAccountRepository;

    @Resource
    private UserRoleRepository userRoleRepository;

    @Resource
    private UserRepository userRepository;

    @Resource
    private PasswordUtil passwordUtil;

    @Resource
    private DistributedIdInnerService distributedIdInnerService;

    private static final AuthAccountConvert AUTH_ACCOUNT_CONVERT = AuthAccountConvert.INSTANCE;

    private static final UserConvert USER_CONVERT = UserConvert.INSTANCE;

    private static final UserInfoConvert USER_INFO_CONVERT = UserInfoConvert.INSTANCE;

    /**
     * 校验 AuthAccount 对象信息
     *
     * @param authAccount 认证账户信息
     */
    private void verifyAuthAccount(AuthAccount authAccount) {
        String username = authAccount.getUsername();
        if (StrUtil.isBlank(username) || !RegularExpressionUtils.isValidUsername(username)) {
            throw bizException(ErrorCode.BAD_REQUEST, "账户名不能为空");
        }

        String password = authAccount.getPassword();
        if (StrUtil.isBlank(password) || !RegularExpressionUtils.isValidPassword(password)) {
            throw bizException(ErrorCode.BAD_REQUEST, "密码不合法");
        }
    }

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
    public UserInfoVO authUserRegister(UserRegisterDTO userRegisterDTO, HttpServletRequest request) {
        // 验证 User 信息
        String password = userRegisterDTO.getPassword();
        String confirmPassword = userRegisterDTO.getConfirmPassword();
        if (!Objects.equals(password, confirmPassword)) {
            throw bizException(ErrorCode.BAD_REQUEST, "两次密码不一致");
        }
        User newUser = USER_CONVERT.convert(userRegisterDTO);
        this.verifyUser(newUser);

        // 获取注册 IP
        String ip = request.getHeader("X-Client-IP");

        // 验证认证账户信息
        AuthAccount authAccount = AUTH_ACCOUNT_CONVERT.convert(userRegisterDTO);
        authAccount.setCreateIp(ip);
        this.verifyAuthAccount(authAccount);

        // 判断账户是否已存在
        Long count = authAccountRepository.lambdaQuery()
                .eq(AuthAccount::getUsername, authAccount.getUsername())
                .count();

        if (count != 0) {
            throw bizException(ErrorCode.BAD_REQUEST, "账户已存在");
        }

        // 获取分布式用户 ID
        BaseResponse<Long> resp = distributedIdInnerService.getDistributedID(DistributedBizTag.AUTH_ACCOUNT.getKey());
        resp.check();
        authAccount.setUserId(resp.getData());

        // 设置默认状态
        authAccount.setStatus(AuthStatus.ENABLE);

        // 加密密码
        String cryptoPassword = passwordUtil.crypto(authAccount.getPassword());
        authAccount.setPassword(cryptoPassword);

        // 保存账户信息
        boolean saveAuthAccountSuccess = authAccountRepository.save(authAccount);
        if (!saveAuthAccountSuccess) {
            throw bizException(ErrorCode.INTERNAL_SERVER_ERROR, "保存认证账户信息失败");
        }

        // 保存用户角色信息
        UserRole userRole = new UserRole()
                .setUserId(authAccount.getUserId())
                .setRoleId(RoleEnum.USER.ordinal());
        boolean saveUserRoleSuccess = userRoleRepository.save(userRole);
        if (!saveUserRoleSuccess) {
            throw bizException(ErrorCode.INTERNAL_SERVER_ERROR, "保存用户角色信息失败");
        }

        // 保存用户信息
        newUser.setUserId(authAccount.getUserId());
        if (StrUtil.isBlank(newUser.getAvatarUrl())) {
            newUser.setAvatarUrl(CommonConstant.DEFAULT_AVATAR_URL);
        }
        boolean saveUserSuccess = userRepository.save(newUser);
        if (!saveUserSuccess) {
            throw bizException(ErrorCode.INTERNAL_SERVER_ERROR, "保存用户信息失败");
        }

        UserInfo userInfo = USER_INFO_CONVERT.convert(newUser, authAccount);
        UserInfoVO userInfoVO = USER_INFO_CONVERT.convert(userInfo);

        return userInfoVO;
    }

    @Override
    public void updateAuthAccount(AuthAccountUpdateDTO authAccountUpdateDTO) {
        UserInfo loginUser = SecurityUtils.getLoginUser();
        AuthAccount authAccount = authAccountRepository.getById(loginUser.getUserId());
        AUTH_ACCOUNT_CONVERT.copy(authAccountUpdateDTO, authAccount);
        this.verifyAuthAccount(authAccount);
        authAccount.setPassword(passwordUtil.crypto(authAccount.getPassword()));
        boolean updateSuccess = authAccountRepository.updateById(authAccount);
        if (!updateSuccess) {
            throw bizException(ErrorCode.INTERNAL_SERVER_ERROR, "更新认证账户信息失败");
        }
        StpUtil.kickout(loginUser.getUserId());
    }

}
