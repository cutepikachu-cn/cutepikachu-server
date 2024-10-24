package cn.cutepikachu.user.service;

import cn.cutepikachu.common.model.user.vo.UserInfoVO;
import cn.cutepikachu.common.model.user.vo.UserVO;
import cn.cutepikachu.user.model.dto.UserUpdateDTO;
import org.springframework.transaction.annotation.Transactional;

/**
 * 用户表 服务接口
 *
 * @author <a href="https://github.com/cutepikachu-cn">笨蛋皮卡丘</a>
 * @version 0.0.1-SNAPSHOT
 * @since 2024-08-01 19:21:35
 */
public interface IUserService {

    /**
     * 获取登录用户信息（如果用户名和密码为空，则获取当前登录用户信息）
     *
     * @param username 用户名
     * @param password 密码
     * @return 用户信息
     */
    UserInfoVO getLoginUserInfo(String username, String password);

    /**
     * 更新用户信息
     *
     * @param userUpdateDTO 用户更新信息
     * @return 用户信息
     */
    @Transactional(rollbackFor = Exception.class)
    UserInfoVO updateUserInfo(UserUpdateDTO userUpdateDTO);

    /**
     * 根据用户ID获取用户信息
     *
     * @param userId 用户ID
     * @return 用户信息
     */
    UserVO getUserVoById(Long userId);

}
