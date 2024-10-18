package cn.cutepikachu.leaf.service.impl;

import cn.cutepikachu.leaf.mapper.LeafAllocMapper;
import cn.cutepikachu.leaf.model.LeafAlloc;
import cn.cutepikachu.leaf.service.ILeafAllocService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author <a href="https://github.com/cutepikachu-cn">笨蛋皮卡丘</a>
 * @version 1.0
 * @since 2024-10-18 19:44-50
 */
@Service
public class LeafAllocService extends ServiceImpl<LeafAllocMapper, LeafAlloc> implements ILeafAllocService {

    @Override
    public List<LeafAlloc> getAllLeafAllocs() {
        return this.list();
    }

    @Override
    public LeafAlloc updateMaxIdAndGetLeafAlloc(String tag) {
        this.lambdaUpdate()
                .eq(LeafAlloc::getBizTag, tag)
                .setSql("max_id = max_id + step")
                .update();
        return this.getLeafAlloc(tag);
    }

    @Override
    public LeafAlloc updateMaxIdByCustomStepAndGetLeafAlloc(LeafAlloc leafAlloc) {
        String tag = leafAlloc.getBizTag();
        this.lambdaUpdate()
                .eq(LeafAlloc::getBizTag, tag)
                .setSql("max_id = max_id + " + leafAlloc.getStep())
                .update();
        return this.getLeafAlloc(tag);
    }

    private LeafAlloc getLeafAlloc(String tag) {
        return this.lambdaQuery()
                .eq(LeafAlloc::getBizTag, tag)
                .one();
    }

    @Override
    public List<String> getAllTags() {
        return this.lambdaQuery()
                .select(LeafAlloc::getBizTag)
                .list()
                .stream()
                .map(LeafAlloc::getBizTag)
                .toList();
    }

}
