package cn.cutepikachu.auth.controller;

import cn.cutepikachu.auth.service.IAuthAccountService;
import cn.cutepikachu.common.auth.model.dto.AuthAccountUpdateDTO;
import cn.cutepikachu.common.response.ResponseEntity;
import cn.cutepikachu.common.util.ResponseUtils;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
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

}
