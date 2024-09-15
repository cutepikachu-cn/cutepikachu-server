package cn.cutepikachu.auth.service.impl;

import cn.cutepikachu.auth.mapper.AuthAccountMapper;
import cn.cutepikachu.auth.service.IAuthAccountService;
import cn.cutepikachu.auth.service.IUserRoleService;
import cn.cutepikachu.common.auth.model.dto.AuthAccountUpdateDTO;
import cn.cutepikachu.common.auth.model.entity.AuthAccount;
import cn.cutepikachu.common.auth.model.entity.UserRole;
import cn.cutepikachu.common.auth.model.enums.RoleEnum;
import cn.cutepikachu.common.constant.DistributedBizTag;
import cn.cutepikachu.common.exception.BusinessException;
import cn.cutepikachu.common.response.ResponseCode;
import cn.cutepikachu.common.response.ResponseEntity;
import cn.cutepikachu.common.security.util.PasswordUtil;
import cn.cutepikachu.common.user.model.vo.UserInfoVO;
import cn.cutepikachu.common.util.BeanUtils;
import cn.cutepikachu.common.util.RegularExpressionUtils;
import cn.cutepikachu.common.util.ResponseUtils;
import cn.cutepikachu.common.util.ThrowUtils;
import cn.cutepikachu.inner.leaf.DistributedIDInnerService;
import cn.dev33.satoken.session.SaSession;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
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
    private DistributedIDInnerService distributedIDInnerService;

    @Override
    public void verify(AuthAccount authAccount) {
        String username = authAccount.getUsername();
        if (StrUtil.isBlank(username) || !RegularExpressionUtils.isValidUsername(username)) {
            throw new BusinessException(ResponseCode.PARAMS_ERROR, "用户名不合法");
        }
        String password = authAccount.getPassword();
        if (StrUtil.isBlank(password) || !RegularExpressionUtils.isValidPassword(password)) {
            throw new BusinessException(ResponseCode.PARAMS_ERROR, "密码不合法");
        }
    }

    @Override
    public AuthAccount saveAuthAccount(AuthAccount authAccount) {
        // 验证认证账户信息
        this.verify(authAccount);

        // 判断账户是否已存在
        Long count = lambdaQuery()
                .eq(AuthAccount::getUsername, authAccount.getUsername())
                .count();
        ThrowUtils.throwIf(count != 0, ResponseCode.PARAMS_ERROR, "用户名已存在");

        // 获取分布式用户 ID
        ResponseEntity<Long> resp = distributedIDInnerService.getDistributedID(DistributedBizTag.AUTH_ACCOUNT);
        ResponseUtils.throwIfNotSuccess(resp);
        authAccount.setUserId(resp.getData());

        // 设置默认状态
        authAccount.setStatus((byte) 1);

        // 加密密码
        String cryptoPassword = PasswordUtil.crypto(authAccount.getPassword());
        authAccount.setPassword(cryptoPassword);

        ThrowUtils.throwIf(!this.save(authAccount), ResponseCode.SYSTEM_ERROR, "保存认证账户信息失败");

        // 保存用户角色信息
        UserRole userRole = new UserRole()
                .setUserId(authAccount.getUserId())
                .setRoleId(RoleEnum.USER.getValue());

        ThrowUtils.throwIf(!userRoleService.save(userRole), ResponseCode.SYSTEM_ERROR, "保存用户角色信息失败");

        return authAccount;
    }

    @Override
    public void updateAuthAccount(AuthAccountUpdateDTO authAccountUpdateDTO) {
        SaSession session = StpUtil.getSession();
        UserInfoVO userInfo = session.getModel("user_info", UserInfoVO.class);
        AuthAccount authAccount = BeanUtils.copyProperties(userInfo, AuthAccount.class);
        BeanUtils.copyProperties(authAccountUpdateDTO, authAccount);
        verify(authAccount);
        String cryptoPassword = PasswordUtil.crypto(authAccount.getPassword());
        authAccount.setPassword(cryptoPassword);
        ThrowUtils.throwIf(!this.updateById(authAccount), ResponseCode.SYSTEM_ERROR, "更新认证账户信息失败");
        StpUtil.kickout(userInfo.getUserId());
    }

}
