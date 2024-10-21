package cn.cutepikachu.user.model.convert;

import cn.cutepikachu.common.model.user.entity.User;
import cn.cutepikachu.common.model.user.entity.UserInfo;
import cn.cutepikachu.common.model.user.vo.UserVO;
import cn.cutepikachu.user.model.dto.UserUpdateDTO;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

/**
 * @author <a href="https://github.com/cutepikachu-cn">笨蛋皮卡丘</a>
 * @version 1.0
 * @since 2024-10-18 16:42-43
 */
@Mapper
public interface UserConvert {

    UserConvert INSTANCE = Mappers.getMapper(UserConvert.class);

    User convert(UserUpdateDTO userUpdateDTO);

    void copy(UserUpdateDTO source, @MappingTarget User target);

    User convert(UserInfo userInfo);

    void copy(UserInfo source, @MappingTarget User target);

    void copy(User source, @MappingTarget UserInfo target);

    UserVO convert(User user);

}
