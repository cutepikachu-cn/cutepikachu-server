package cn.cutepikachu.auth.dao.mapper;

import cn.cutepikachu.common.model.auth.entity.UserRole;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户角色关联表 Mapper 接口
 *
 * @author <a href="https://github.com/cutepikachu-cn">笨蛋皮卡丘</a>
 * @version 0.0.1-SNAPSHOT
 * @since 2024-08-01 19:22:35
 */
@Mapper
public interface UserRoleMapper extends BaseMapper<UserRole> {

}
