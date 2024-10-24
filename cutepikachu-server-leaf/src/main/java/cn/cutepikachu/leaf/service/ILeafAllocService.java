package cn.cutepikachu.leaf.service;

import cn.cutepikachu.leaf.model.LeafAlloc;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author <a href="https://github.com/cutepikachu-cn">笨蛋皮卡丘</a>
 * @version 1.0
 * @since 2024-10-18 19:44-28
 */
public interface ILeafAllocService {

    List<LeafAlloc> getAllLeafAllocs();

    @Transactional
    LeafAlloc updateMaxIdAndGetLeafAlloc(String tag);

    @Transactional
    LeafAlloc updateMaxIdByCustomStepAndGetLeafAlloc(LeafAlloc leafAlloc);

    List<String> getAllTags();

}
