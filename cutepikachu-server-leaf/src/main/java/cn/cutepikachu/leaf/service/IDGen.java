package cn.cutepikachu.leaf.service;

import cn.cutepikachu.leaf.common.Result;

/**
 * @author <a href="https://github.com/Meituan-Dianping/Leaf"> leaf 美团分布式 ID 生成</a>
 * @version 1.0.1
 */
public interface IDGen {

    Result get(String key);

}
