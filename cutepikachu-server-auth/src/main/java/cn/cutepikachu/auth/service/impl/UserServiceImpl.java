package cn.cutepikachu.auth.service.impl;

import cn.cutepikachu.auth.mapper.UserMapper;
import cn.cutepikachu.auth.service.IUserService;
import cn.cutepikachu.common.user.model.entity.User;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * 用户表 服务实现类
 *
 * @author <a href="https://github.com/cutepikachu-cn">笨蛋皮卡丘</a>
 * @version 0.0.1-SNAPSHOT
 * @since 2024-10-06 21:02:30
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

}
