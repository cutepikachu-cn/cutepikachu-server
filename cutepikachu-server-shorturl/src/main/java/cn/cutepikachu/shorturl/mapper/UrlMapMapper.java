package cn.cutepikachu.shorturl.mapper;

import cn.cutepikachu.shorturl.model.entity.UrlMap;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 短链接映射表 Mapper 接口
 *
 * @author <a href="https://github.com/cutepikachu-cn">笨蛋皮卡丘</a>
 * @version 0.0.1-SNAPSHOT
 * @since 2024-09-02 19:12:07
 */
@Mapper
public interface UrlMapMapper extends BaseMapper<UrlMap> {

}
