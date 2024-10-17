package cn.cutepikachu.auth.inner;

import cn.cutepikachu.auth.service.IAuthAccountService;
import cn.cutepikachu.auth.service.IUserRoleService;
import cn.cutepikachu.common.model.BaseEnum;
import cn.cutepikachu.common.model.auth.entity.AuthAccount;
import cn.cutepikachu.common.model.auth.entity.UserRole;
import cn.cutepikachu.common.model.auth.enums.RoleEnum;
import cn.cutepikachu.common.response.BaseResponse;
import cn.cutepikachu.common.response.ErrorCode;
import cn.cutepikachu.common.util.ResponseUtils;
import cn.cutepikachu.inner.auth.AuthInnerService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static cn.cutepikachu.common.exception.ExceptionFactory.bizException;

/**
 * 认证鉴权内部服务实现
 *
 * @author <a href="https://github.com/cutepikachu-cn">笨蛋皮卡丘</a>
 * @version 1.0
 * @since 2024-08-01 20:51-04
 */
@RestController
public class AuthInnerServiceController implements AuthInnerService {

    @Resource
    private IAuthAccountService authAccountService;

    @Resource
    private IUserRoleService userRoleService;

    @Override
    public BaseResponse<AuthAccount> getAuthAccountByUserId(Long userId) {
        AuthAccount authAccount = authAccountService.getById(userId);
        if (authAccount == null) {
            throw bizException(ErrorCode.NOT_FOUND, "账户不存在");
        }
        return ResponseUtils.success(authAccount);
    }

    @Override
    public BaseResponse<AuthAccount> getAuthAccountByUsernameAndPassword(String username, String password) {
        // 根据用户名和加密后的密码查询账户信息
        AuthAccount authAccount = authAccountService.lambdaQuery()
                .eq(AuthAccount::getUsername, username)
                .one();

        if (authAccount == null) {
            throw bizException(ErrorCode.NOT_FOUND, "账户不存在");
        }
        if (!authAccount.getPassword().equals(password)) {
            throw bizException(ErrorCode.BAD_REQUEST, "密码错误");
        }

        return ResponseUtils.success(authAccount);
    }

    @Override
    public BaseResponse<List<RoleEnum>> getAuthAccountRoleByUserId(Long userId) {
        AuthAccount authAccount = authAccountService.getById(userId);
        if (authAccount == null) {
            throw bizException(ErrorCode.NOT_FOUND, "账户不存在");
        }
        List<RoleEnum> roleList = userRoleService.lambdaQuery()
                .eq(UserRole::getUserId, userId)
                .list()
                .stream()
                .map(userRole -> BaseEnum.getEnumByValue(RoleEnum.class, userRole.getRoleId()))
                .toList();
        return ResponseUtils.success(roleList);
    }

}
