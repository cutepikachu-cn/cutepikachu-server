package cn.cutepikachu.leaf.common;

import cn.cutepikachu.leaf.service.IDGen;

/**
 * @author <a href="https://github.com/Meituan-Dianping/Leaf"> leaf 美团分布式 ID 生成</a>
 * @version 1.0.1
 */
public class ZeroIDGen implements IDGen {

    @Override
    public Result get(String key) {
        return new Result(0, Status.SUCCESS);
    }

}
