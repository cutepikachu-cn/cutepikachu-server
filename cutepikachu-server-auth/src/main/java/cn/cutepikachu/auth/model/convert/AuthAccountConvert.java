package cn.cutepikachu.auth.model.convert;

import cn.cutepikachu.auth.model.dto.AuthAccountUpdateDTO;
import cn.cutepikachu.auth.model.dto.UserRegisterDTO;
import cn.cutepikachu.common.model.auth.entity.AuthAccount;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @author <a href="https://github.com/cutepikachu-cn">笨蛋皮卡丘</a>
 * @version 1.0
 * @since 2024-10-18 16:23-50
 */
@Mapper
public interface AuthAccountConvert {

    AuthAccountConvert INSTANCE = Mappers.getMapper(AuthAccountConvert.class);

    AuthAccount convert(UserRegisterDTO userRegisterDTO);

    AuthAccount convert(AuthAccountUpdateDTO authAccountUpdateDTO);

}
