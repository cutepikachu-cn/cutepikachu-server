package cn.cutepikachu.xtimer.mapper;

import cn.cutepikachu.xtimer.model.entity.Timer;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 定时任务信息表 Mapper 接口
 *
 * @author <a href="https://github.com/cutepikachu-cn">笨蛋皮卡丘</a>
 * @version 0.0.1-SNAPSHOT
 * @since 2024-08-12 22:15:16
 */
@Mapper
public interface TimerMapper extends BaseMapper<Timer> {

}
