package cn.cutepikachu.leaf.service;

import cn.cutepikachu.leaf.common.Result;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author <a href="https://github.com/Meituan-Dianping/Leaf"> leaf 美团分布式 ID 生成</a>
 * @version 1.0.1
 */
@Slf4j
@Service
public class SegmentService {

    @Resource
    private IDGen idGen;

    public Result getId(String key) {
        return idGen.get(key);
    }

}
