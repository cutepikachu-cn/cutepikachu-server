package cn.cutepikachu.auth.inner;

import cn.cutepikachu.auth.service.IAuthAccountService;
import cn.cutepikachu.auth.service.IUserRoleService;
import cn.cutepikachu.common.auth.model.entity.AuthAccount;
import cn.cutepikachu.common.auth.model.entity.UserRole;
import cn.cutepikachu.common.auth.model.enums.RoleEnum;
import cn.cutepikachu.common.model.BaseEnum;
import cn.cutepikachu.common.response.ResponseCode;
import cn.cutepikachu.common.response.ResponseEntity;
import cn.cutepikachu.common.util.ResponseUtils;
import cn.cutepikachu.common.util.ThrowUtils;
import cn.cutepikachu.inner.auth.AuthInnerService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
    public ResponseEntity<AuthAccount> getAuthAccountByUserId(Long userId) {
        AuthAccount authAccount = authAccountService.getById(userId);
        ThrowUtils.throwIf(authAccount == null, ResponseCode.NOT_FOUND, "账户不存在");
        return ResponseUtils.success(authAccount);
    }

    @Override
    public ResponseEntity<AuthAccount> getAuthAccountByUsernameAndPassword(String username, String password) {
        // 根据用户名和加密后的密码查询账户信息
        AuthAccount authAccount = authAccountService.lambdaQuery()
                .eq(AuthAccount::getUsername, username)
                .one();
        ThrowUtils.throwIf(authAccount == null, ResponseCode.NOT_FOUND, "账户不存在");
        ThrowUtils.throwIf(!authAccount.getPassword().equals(password), ResponseCode.BAD_REQUEST, "密码错误");
        return ResponseUtils.success(authAccount);
    }

    @Override
    public ResponseEntity<List<RoleEnum>> getAuthAccountRoleByUserId(Long userId) {
        AuthAccount authAccount = authAccountService.getById(userId);
        ThrowUtils.throwIf(authAccount == null, ResponseCode.NOT_FOUND, "账户不存在");
        List<RoleEnum> roleList = userRoleService.lambdaQuery()
                .eq(UserRole::getUserId, userId)
                .list()
                .stream()
                .map(userRole -> BaseEnum.getEnumByValue(RoleEnum.class, userRole.getRoleId()))
                .toList();
        return ResponseUtils.success(roleList);
    }

}
