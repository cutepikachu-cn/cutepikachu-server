package cn.cutepikachu.user.controller;

import cn.cutepikachu.common.model.user.entity.User;
import cn.cutepikachu.common.model.user.vo.UserInfoVO;
import cn.cutepikachu.common.model.user.vo.UserVO;
import cn.cutepikachu.common.response.BaseResponse;
import cn.cutepikachu.common.response.ErrorCode;
import cn.cutepikachu.common.util.ResponseUtils;
import cn.cutepikachu.user.model.convert.UserConvert;
import cn.cutepikachu.user.model.dto.UserLoginDTO;
import cn.cutepikachu.user.model.dto.UserUpdateDTO;
import cn.cutepikachu.user.service.IUserService;
import cn.dev33.satoken.stp.StpUtil;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import static cn.cutepikachu.common.exception.ExceptionFactory.bizException;

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

    private static final UserConvert USER_CONVERT = UserConvert.INSTANCE;

    @PostMapping("/login")
    public BaseResponse<UserInfoVO> login(@RequestBody UserLoginDTO userLoginDTO) {
        String username = userLoginDTO.getUsername();
        String password = userLoginDTO.getPassword();
        UserInfoVO loginUserInfo = userService.getLoginUserInfo(username, password);
        return ResponseUtils.success(loginUserInfo);
    }

    @PostMapping("/logout")
    public BaseResponse<Boolean> logout() {
        StpUtil.logout();
        return ResponseUtils.success(Boolean.TRUE);
    }

    @PostMapping("/update_self_info")
    public BaseResponse<UserInfoVO> update(@RequestBody UserUpdateDTO userUpdateDTO) {
        UserInfoVO userInfoVO = userService.updateUserInfo(userUpdateDTO);
        return ResponseUtils.success(userInfoVO);
    }

    @GetMapping("/self_info")
    public BaseResponse<UserInfoVO> selfInfo() {
        UserInfoVO loginUserInfo = userService.getLoginUserInfo(null, null);
        return ResponseUtils.success(loginUserInfo);
    }

    @GetMapping("/info")
    public BaseResponse<UserVO> info(@RequestParam Long userId) {
        User user = userService.getById(userId);
        if (user == null) {
            throw bizException(ErrorCode.NOT_FOUND, "用户不存在");
        }
        UserVO userVO = USER_CONVERT.convert(user);
        return ResponseUtils.success(userVO);
    }

}
