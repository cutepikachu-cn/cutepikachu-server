package cn.cutepikachu.common.model.user.convert;

import cn.cutepikachu.common.model.auth.entity.AuthAccount;
import cn.cutepikachu.common.model.user.entity.User;
import cn.cutepikachu.common.model.user.entity.UserInfo;
import cn.cutepikachu.common.model.user.vo.UserInfoVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

/**
 * @author <a href="https://github.com/cutepikachu-cn">笨蛋皮卡丘</a>
 * @version 1.0
 * @since 2024-10-18 17:32-49
 */
@Mapper
public interface UserInfoConvert {

    UserInfoConvert INSTANCE = Mappers.getMapper(UserInfoConvert.class);

    UserInfoVO convert(UserInfo userInfo);

    @Mappings({
            @Mapping(source = "authAccount.userId", target = "userId"),
            @Mapping(source = "authAccount.username", target = "username"),
            @Mapping(source = "user.nickName", target = "nickName"),
            @Mapping(source = "user.avatarUrl", target = "avatarUrl"),
            @Mapping(source = "user.createTime", target = "createTime")
    })
    UserInfo convert(User user, AuthAccount authAccount);

}
