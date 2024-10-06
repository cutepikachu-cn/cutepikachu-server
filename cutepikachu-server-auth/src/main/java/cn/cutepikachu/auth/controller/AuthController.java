package cn.cutepikachu.auth.controller;

import cn.cutepikachu.auth.service.IAuthAccountService;
import cn.cutepikachu.common.auth.model.dto.AuthAccountUpdateDTO;
import cn.cutepikachu.common.response.ResponseEntity;
import cn.cutepikachu.common.user.model.dto.UserRegisterDTO;
import cn.cutepikachu.common.user.model.vo.UserInfoVO;
import cn.cutepikachu.common.util.ResponseUtils;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 认证鉴权服务 对外接口
 *
 * @author <a href="https://github.com/cutepikachu-cn">笨蛋皮卡丘</a>
 * @version 1.0
 * @since 2024-08-01 19:55-22
 */
@RestController
public class AuthController {

    @Resource
    private IAuthAccountService authAccountService;

    @PostMapping("/update_password")
    public ResponseEntity<Boolean> updateAuthPassword(@RequestBody AuthAccountUpdateDTO authAccountUpdateDTO) {
        authAccountService.updateAuthAccount(authAccountUpdateDTO);
        return ResponseUtils.success(Boolean.TRUE);
    }

    @PostMapping("/register")
    public ResponseEntity<UserInfoVO> register(@RequestBody UserRegisterDTO userRegisterDTO,
                                               HttpServletRequest request) {
        UserInfoVO userInfoVO = authAccountService.authUserRegister(userRegisterDTO, request);
        return ResponseUtils.success(userInfoVO);
    }

}
