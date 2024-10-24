package cn.cutepikachu.user.dao.repository;

import cn.cutepikachu.common.model.user.entity.User;
import cn.cutepikachu.user.dao.mapper.UserMapper;
import com.baomidou.mybatisplus.extension.repository.CrudRepository;
import org.springframework.stereotype.Component;

/**
 * @author <a href="https://github.com/cutepikachu-cn">笨蛋皮卡丘</a>
 * @version 1.0
 * @since 2024-10-24 21:09-43
 */
@Component
public class UserRepository extends CrudRepository<UserMapper, User> {
}
