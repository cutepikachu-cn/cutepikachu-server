package cn.cutepikachu.leaf.service;

import cn.cutepikachu.leaf.IDGen;
import cn.cutepikachu.leaf.common.Result;
import cn.cutepikachu.leaf.exception.InitException;
import cn.cutepikachu.leaf.segment.SegmentIDGenImpl;
import cn.cutepikachu.leaf.segment.dao.IDAllocDao;
import cn.cutepikachu.leaf.segment.dao.impl.IDAllocDaoImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.SQLException;

/**
 * @author <a href="https://github.com/Meituan-Dianping/Leaf"> leaf 美团分布式 ID 生成</a>
 * @version 1.0.1
 */
@Slf4j
@Service
public class SegmentService {

    private final IDGen idGen;

    public SegmentService(DataSource dataSource) throws SQLException, InitException {
        // Config Dao
        IDAllocDao dao = new IDAllocDaoImpl(dataSource);
        // Config ID Gen
        idGen = new SegmentIDGenImpl();
        ((SegmentIDGenImpl) idGen).setDao(dao);
        if (idGen.init()) {
            log.info("Segment Service Init Successfully");
        } else {
            throw new InitException("Segment Service Init Fail");
        }
    }

    public Result getId(String key) {
        return idGen.get(key);
    }

    public SegmentIDGenImpl getIdGen() {
        if (idGen instanceof SegmentIDGenImpl) {
            return (SegmentIDGenImpl) idGen;
        }
        return null;
    }

}
