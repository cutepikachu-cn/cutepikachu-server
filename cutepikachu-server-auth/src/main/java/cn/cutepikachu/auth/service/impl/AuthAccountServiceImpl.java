package cn.cutepikachu.auth.service.impl;

import cn.cutepikachu.auth.mapper.AuthAccountMapper;
import cn.cutepikachu.auth.model.dto.AuthAccountUpdateDTO;
import cn.cutepikachu.auth.model.dto.UserRegisterDTO;
import cn.cutepikachu.auth.service.IAuthAccountService;
import cn.cutepikachu.auth.service.IUserRoleService;
import cn.cutepikachu.auth.service.IUserService;
import cn.cutepikachu.common.constant.CommonConstant;
import cn.cutepikachu.common.constant.DistributedBizTag;
import cn.cutepikachu.common.exception.BusinessException;
import cn.cutepikachu.common.model.auth.entity.AuthAccount;
import cn.cutepikachu.common.model.auth.entity.UserRole;
import cn.cutepikachu.common.model.auth.enums.RoleEnum;
import cn.cutepikachu.common.model.user.entity.User;
import cn.cutepikachu.common.model.user.vo.UserInfoVO;
import cn.cutepikachu.common.response.BaseResponse;
import cn.cutepikachu.common.response.ResponseCode;
import cn.cutepikachu.common.security.util.PasswordUtil;
import cn.cutepikachu.common.util.BeanUtils;
import cn.cutepikachu.common.util.RegularExpressionUtils;
import cn.cutepikachu.common.util.ResponseUtils;
import cn.cutepikachu.common.util.ThrowUtils;
import cn.cutepikachu.inner.leaf.DistributedIdInnerService;
import cn.dev33.satoken.session.SaSession;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

/**
 * 认证账户表 服务实现类
 *
 * @author <a href="https://github.com/cutepikachu-cn">笨蛋皮卡丘</a>
 * @version 0.0.1-SNAPSHOT
 * @since 2024-08-01 19:22:35
 */
@Service
public class AuthAccountServiceImpl extends ServiceImpl<AuthAccountMapper, AuthAccount> implements IAuthAccountService {

    @Resource
    private IUserRoleService userRoleService;

    @Resource
    private IUserService userService;

    @Resource
    private PasswordUtil passwordUtil;

    @Resource
    private DistributedIdInnerService distributedIdInnerService;

    /**
     * 校验 AuthAccount 对象信息
     *
     * @param authAccount 认证账户信息
     */
    private void verifyAuthAccount(AuthAccount authAccount) {
        String username = authAccount.getUsername();
        if (StrUtil.isBlank(username) || !RegularExpressionUtils.isValidUsername(username)) {
            throw new BusinessException(ResponseCode.BAD_REQUEST, "账户名不合法");
        }
        String password = authAccount.getPassword();
        if (StrUtil.isBlank(password) || !RegularExpressionUtils.isValidPassword(password)) {
            throw new BusinessException(ResponseCode.BAD_REQUEST, "密码不合法");
        }
    }

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
    public UserInfoVO authUserRegister(UserRegisterDTO userRegisterDTO, HttpServletRequest request) {
        // 验证 User 信息
        String password = userRegisterDTO.getPassword();
        String confirmPassword = userRegisterDTO.getConfirmPassword();
        if (!StrUtil.equals(password, confirmPassword)) {
            throw new BusinessException(ResponseCode.BAD_REQUEST, "两次密码不一致");
        }
        User newUser = BeanUtils.copyProperties(userRegisterDTO, User.class);
        this.verifyUser(newUser);

        // 获取注册 IP
        String ip = request.getHeader("X-Client-IP");

        // 验证认证账户信息
        AuthAccount authAccount = BeanUtils.copyProperties(userRegisterDTO, AuthAccount.class);
        authAccount.setCreateIp(ip);
        this.verifyAuthAccount(authAccount);

        // 判断账户是否已存在
        Long count = lambdaQuery()
                .eq(AuthAccount::getUsername, authAccount.getUsername())
                .count();
        ThrowUtils.throwIf(count != 0, ResponseCode.BAD_REQUEST, "账户已存在");

        // 获取分布式用户 ID
        BaseResponse<Long> resp = distributedIdInnerService.getDistributedID(DistributedBizTag.AUTH_ACCOUNT);
        ResponseUtils.throwIfNotSuccess(resp);
        authAccount.setUserId(resp.getData());

        // 设置默认状态
        authAccount.setStatus((byte) 1);

        // 加密密码
        String cryptoPassword = passwordUtil.crypto(authAccount.getPassword());
        authAccount.setPassword(cryptoPassword);

        // 保存账户信息
        ThrowUtils.throwIf(!this.save(authAccount), ResponseCode.INTERNAL_SERVER_ERROR, "保存认证账户信息失败");

        // 保存用户角色信息
        UserRole userRole = new UserRole()
                .setUserId(authAccount.getUserId())
                .setRoleId(RoleEnum.USER.getValue());
        ThrowUtils.throwIf(!userRoleService.save(userRole), ResponseCode.INTERNAL_SERVER_ERROR, "保存用户角色信息失败");

        // 保存用户信息
        newUser.setUserId(authAccount.getUserId());
        if (StrUtil.isBlank(newUser.getAvatarUrl())) {
            newUser.setAvatarUrl(CommonConstant.DEFAULT_AVATAR_URL);
        }
        ThrowUtils.throwIf(!userService.save(newUser), ResponseCode.INTERNAL_SERVER_ERROR, "保存用户信息失败");

        UserInfoVO userInfoVO = newUser.toUserInfoVO(authAccount);

        return userInfoVO;
    }

    @Override
    public void updateAuthAccount(AuthAccountUpdateDTO authAccountUpdateDTO) {
        SaSession session = StpUtil.getSession();
        UserInfoVO userInfo = session.getModel("user_info", UserInfoVO.class);
        AuthAccount authAccount = BeanUtils.copyProperties(userInfo, AuthAccount.class);
        BeanUtils.copyProperties(authAccountUpdateDTO, authAccount);
        this.verifyAuthAccount(authAccount);
        String cryptoPassword = passwordUtil.crypto(authAccount.getPassword());
        authAccount.setPassword(cryptoPassword);
        ThrowUtils.throwIf(!this.updateById(authAccount), ResponseCode.INTERNAL_SERVER_ERROR, "更新认证账户信息失败");
        StpUtil.kickout(userInfo.getUserId());
    }

}
