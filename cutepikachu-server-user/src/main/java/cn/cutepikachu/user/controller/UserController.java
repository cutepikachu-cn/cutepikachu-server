package cn.cutepikachu.user.controller;

import cn.cutepikachu.common.exception.BusinessException;
import cn.cutepikachu.common.model.user.entity.User;
import cn.cutepikachu.common.model.user.vo.UserInfoVO;
import cn.cutepikachu.common.model.user.vo.UserVO;
import cn.cutepikachu.common.response.ResponseCode;
import cn.cutepikachu.common.response.ResponseEntity;
import cn.cutepikachu.common.util.ResponseUtils;
import cn.cutepikachu.common.util.ThrowUtils;
import cn.cutepikachu.user.model.dto.UserLoginDTO;
import cn.cutepikachu.user.model.dto.UserUpdateDTO;
import cn.cutepikachu.user.service.IUserService;
import cn.dev33.satoken.stp.StpUtil;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

/**
 * 用户服务 对外接口
 *
 * @author <a href="https://github.com/cutepikachu-cn">笨蛋皮卡丘</a>
 * @version 1.0
 * @since 2024-08-01 19:27-41
 */
@RestController
public class UserController {

    @Resource
    private IUserService userService;

    @PostMapping("/login")
    public ResponseEntity<UserInfoVO> login(@RequestBody UserLoginDTO userLoginDTO) {
        String username = userLoginDTO.getUsername();
        String password = userLoginDTO.getPassword();
        UserInfoVO loginUserInfo = userService.getLoginUserInfo(username, password);
        return ResponseUtils.success(loginUserInfo);
    }

    @PostMapping("/logout")
    public ResponseEntity<Boolean> logout() {
        StpUtil.logout();
        return ResponseUtils.success(Boolean.TRUE);
    }

    @PostMapping("/update_self_info")
    public ResponseEntity<UserInfoVO> update(@RequestBody UserUpdateDTO userUpdateDTO) {
        UserInfoVO userInfoVO = userService.updateUserInfo(userUpdateDTO);
        return ResponseUtils.success(userInfoVO);
    }

    @GetMapping("/self_info")
    public ResponseEntity<UserInfoVO> selfInfo() {
        UserInfoVO loginUserInfo = userService.getLoginUserInfo(null, null);
        return ResponseUtils.success(loginUserInfo);
    }

    @GetMapping("/info")
    public ResponseEntity<UserVO> info(@RequestParam Long userId) {
        User user = userService.getById(userId);
        ThrowUtils.throwIf(user == null, new BusinessException(ResponseCode.NOT_FOUND, "用户不存在"));
        return ResponseUtils.success(user.toVO(UserVO.class));
    }

}
