package cn.cutepikachu.xtimer.model.convert;

import cn.cutepikachu.xtimer.model.dto.TimerCreateDTO;
import cn.cutepikachu.xtimer.model.dto.TimerUpdateDTO;
import cn.cutepikachu.xtimer.model.entity.Timer;
import cn.cutepikachu.xtimer.model.vo.TimerVO;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

/**
 * @author <a href="https://github.com/cutepikachu-cn">笨蛋皮卡丘</a>
 * @version 1.0
 * @since 2024-10-18 17:03-12
 */
@Mapper
public interface TimerConvert {

    TimerConvert INSTANCE = Mappers.getMapper(TimerConvert.class);

    Timer convert(TimerCreateDTO timerCreateDTO);

    TimerVO convert(Timer timer);

    void copy(TimerUpdateDTO timerUpdateDTO, @MappingTarget Timer timer);

}
