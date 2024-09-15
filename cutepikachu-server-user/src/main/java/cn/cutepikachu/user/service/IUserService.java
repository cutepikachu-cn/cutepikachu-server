package cn.cutepikachu.user.service;

import cn.cutepikachu.common.user.model.dto.UserRegisterDTO;
import cn.cutepikachu.common.user.model.dto.UserUpdateDTO;
import cn.cutepikachu.common.user.model.entity.User;
import cn.cutepikachu.common.user.model.vo.UserInfoVO;
import com.baomidou.mybatisplus.extension.service.IService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.transaction.annotation.Transactional;

/**
 * 用户表 服务接口
 *
 * @author <a href="https://github.com/cutepikachu-cn">笨蛋皮卡丘</a>
 * @version 0.0.1-SNAPSHOT
 * @since 2024-08-01 19:21:35
 */
public interface IUserService extends IService<User> {

    /**
     * 校验 User 对象信息
     *
     * @param user 用户信息
     */
    void verify(User user);

    /**
     * 保存用户信息
     *
     * @param userRegisterDTO 用户注册信息
     * @return 用户信息
     */
    @Transactional(rollbackFor = Exception.class)
    UserInfoVO saveUser(UserRegisterDTO userRegisterDTO, HttpServletRequest request);

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

}
