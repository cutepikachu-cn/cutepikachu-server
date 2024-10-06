package cn.cutepikachu.auth.service;

import cn.cutepikachu.common.auth.model.dto.AuthAccountUpdateDTO;
import cn.cutepikachu.common.auth.model.entity.AuthAccount;
import cn.cutepikachu.common.user.model.dto.UserRegisterDTO;
import cn.cutepikachu.common.user.model.vo.UserInfoVO;
import com.baomidou.dynamic.datasource.annotation.DSTransactional;
import com.baomidou.mybatisplus.extension.service.IService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.transaction.annotation.Transactional;

/**
 * 认证账户表 服务接口
 *
 * @author <a href="https://github.com/cutepikachu-cn">笨蛋皮卡丘</a>
 * @version 0.0.1-SNAPSHOT
 * @since 2024-08-01 19:22:35
 */
public interface IAuthAccountService extends IService<AuthAccount> {

    @DSTransactional(rollbackFor = Exception.class)
    UserInfoVO authUserRegister(UserRegisterDTO userRegisterDTO, HttpServletRequest request);

    @Transactional(rollbackFor = Exception.class)
    void updateAuthAccount(AuthAccountUpdateDTO authAccountUpdateDTO);

}
