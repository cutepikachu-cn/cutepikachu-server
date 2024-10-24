package cn.cutepikachu.auth.dao.mapper;

import cn.cutepikachu.common.model.user.entity.User;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户表 Mapper 接口
 *
 * @author <a href="https://github.com/cutepikachu-cn">笨蛋皮卡丘</a>
 * @version 0.0.1-SNAPSHOT
 * @since 2024-10-06 21:02:30
 */
@Mapper
@DS("cutepikachu_cn_user")
public interface UserMapper extends BaseMapper<User> {

}
