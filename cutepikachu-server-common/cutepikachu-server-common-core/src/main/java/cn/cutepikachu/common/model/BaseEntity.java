package cn.cutepikachu.common.model;

import cn.cutepikachu.common.util.BeanUtils;
import cn.hutool.core.util.ReflectUtil;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 基础 Entity 类
 *
 * @author <a href="https://github.com/cutepikachu-cn">笨蛋皮卡丘</a>
 * @version 1.0
 * @since 2024-0-28 17:55:55
 */
@Data
public abstract class BaseEntity<E extends BaseEntity<E, VO>, VO extends BaseVO<E, VO>> implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    public VO toVO(Class<VO> voClass) {
        VO vo = ReflectUtil.newInstance(voClass);
        BeanUtils.copyProperties(this, vo);
        return vo;
    }

}
