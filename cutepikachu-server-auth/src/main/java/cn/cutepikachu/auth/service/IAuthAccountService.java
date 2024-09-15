package cn.cutepikachu.auth.service;

import cn.cutepikachu.common.auth.model.dto.AuthAccountUpdateDTO;
import cn.cutepikachu.common.auth.model.entity.AuthAccount;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.transaction.annotation.Transactional;

/**
 * 认证账户表 服务接口
 *
 * @author <a href="https://github.com/cutepikachu-cn">笨蛋皮卡丘</a>
 * @version 0.0.1-SNAPSHOT
 * @since 2024-08-01 19:22:35
 */
public interface IAuthAccountService extends IService<AuthAccount> {

    void verify(AuthAccount authAccount);

    @Transactional(rollbackFor = Exception.class)
    AuthAccount saveAuthAccount(AuthAccount authAccount);

    @Transactional(rollbackFor = Exception.class)
    void updateAuthAccount(AuthAccountUpdateDTO authAccountUpdateDTO);

}
