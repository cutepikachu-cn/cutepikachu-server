package cn.cutepikachu.auth.service.impl;

import cn.cutepikachu.auth.dao.repository.UserRoleRepository;
import cn.cutepikachu.auth.service.IUserRoleService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 * 用户角色关联表 服务实现类
 *
 * @author <a href="https://github.com/cutepikachu-cn">笨蛋皮卡丘</a>
 * @version 0.0.1-SNAPSHOT
 * @since 2024-08-01 19:22:35
 */
@Service
public class UserRoleServiceImpl implements IUserRoleService {

    @Resource
    private UserRoleRepository userRoleRepository;

}
