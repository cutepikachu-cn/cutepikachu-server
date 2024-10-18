package cn.cutepikachu.biz.model.convert;

import cn.cutepikachu.common.model.biz.entity.FileInfo;
import cn.cutepikachu.common.model.biz.vo.FileInfoVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @author <a href="https://github.com/cutepikachu-cn">笨蛋皮卡丘</a>
 * @version 1.0
 * @since 2024-10-18 17:37-34
 */
@Mapper
public interface FileInfoConvert {

    FileInfoConvert INSTANCE = Mappers.getMapper(FileInfoConvert.class);

    FileInfoVO convert(FileInfo fileInfo);

}
