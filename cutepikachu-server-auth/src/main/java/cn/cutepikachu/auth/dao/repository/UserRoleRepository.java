package cn.cutepikachu.auth.dao.repository;

import cn.cutepikachu.auth.dao.mapper.UserRoleMapper;
import cn.cutepikachu.common.model.auth.entity.UserRole;
import com.baomidou.mybatisplus.extension.repository.CrudRepository;
import org.springframework.stereotype.Component;

/**
 * @author <a href="https://github.com/cutepikachu-cn">笨蛋皮卡丘</a>
 * @version 1.0
 * @since 2024-10-24 21:20-25
 */
@Component
public class UserRoleRepository extends CrudRepository<UserRoleMapper, UserRole> {
}
