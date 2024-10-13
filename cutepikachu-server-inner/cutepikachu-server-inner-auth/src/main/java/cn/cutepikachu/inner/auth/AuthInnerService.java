package cn.cutepikachu.inner.auth;

import cn.cutepikachu.common.constant.FeignConstant;
import cn.cutepikachu.common.model.auth.entity.AuthAccount;
import cn.cutepikachu.common.model.auth.enums.RoleEnum;
import cn.cutepikachu.common.response.ResponseEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * 认证鉴权内部服务接口
 *
 * @author <a href="https://github.com/cutepikachu-cn">笨蛋皮卡丘</a>
 * @version 1.0
 * @since 2024-08-01 20:30-01
 */
@FeignClient(value = "cutepikachu-server-auth")
public interface AuthInnerService {

    /**
     * 根据用户 id 获取认证授权账户
     *
     * @param userId 用户 id
     * @return 证授权账户
     */
    @GetMapping(FeignConstant.INNER_SERVICE_PREFIX + "/get/auth/account/user_id")
    ResponseEntity<AuthAccount> getAuthAccountByUserId(@RequestParam Long userId);

    /**
     * 根据用户账号密码获取认证授权账户
     *
     * @param username 用户名
     * @param password 密码
     * @return 证授权账户
     */
    @GetMapping(FeignConstant.INNER_SERVICE_PREFIX + "/get/auth/account/username_password")
    ResponseEntity<AuthAccount> getAuthAccountByUsernameAndPassword(@RequestParam String username, @RequestParam String password);

    /**
     * 根据用户 id 获取认证账户角色
     *
     * @param userId 用户 id
     * @return 认证账户角色
     */
    @GetMapping(FeignConstant.INNER_SERVICE_PREFIX + "/get/auth/account/role")
    ResponseEntity<List<RoleEnum>> getAuthAccountRoleByUserId(@RequestParam Long userId);

}
