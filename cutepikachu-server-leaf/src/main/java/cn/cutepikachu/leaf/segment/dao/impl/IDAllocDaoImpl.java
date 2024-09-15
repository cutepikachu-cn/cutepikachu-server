package cn.cutepikachu.leaf.segment.dao.impl;

import cn.cutepikachu.leaf.segment.dao.IDAllocDao;
import cn.cutepikachu.leaf.segment.dao.IDAllocMapper;
import cn.cutepikachu.leaf.segment.model.LeafAlloc;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.TransactionFactory;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;

import javax.sql.DataSource;
import java.util.List;

/**
 * @author <a href="https://github.com/Meituan-Dianping/Leaf"> leaf 美团分布式 ID 生成</a>
 * @version 1.0.1
 */
public class IDAllocDaoImpl implements IDAllocDao {

    SqlSessionFactory sqlSessionFactory;

    public IDAllocDaoImpl(DataSource dataSource) {
        TransactionFactory transactionFactory = new JdbcTransactionFactory();
        Environment environment = new Environment("development", transactionFactory, dataSource);
        Configuration configuration = new Configuration(environment);
        configuration.addMapper(IDAllocMapper.class);
        sqlSessionFactory = new SqlSessionFactoryBuilder().build(configuration);
    }

    @Override
    public List<LeafAlloc> getAllLeafAllocs() {
        try (SqlSession sqlSession = sqlSessionFactory.openSession(false)) {
            return sqlSession.selectList("cn.cutepikachu.leaf.segment.dao.IDAllocMapper.getAllLeafAllocs");
        }
    }

    @Override
    public LeafAlloc updateMaxIdAndGetLeafAlloc(String tag) {
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
            sqlSession.update("cn.cutepikachu.leaf.segment.dao.IDAllocMapper.updateMaxId", tag);
            LeafAlloc result = sqlSession.selectOne("cn.cutepikachu.leaf.segment.dao.IDAllocMapper.getLeafAlloc", tag);
            sqlSession.commit();
            return result;
        }
    }

    @Override
    public LeafAlloc updateMaxIdByCustomStepAndGetLeafAlloc(LeafAlloc leafAlloc) {
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
            sqlSession.update("cn.cutepikachu.leaf.segment.dao.IDAllocMapper.updateMaxIdByCustomStep", leafAlloc);
            LeafAlloc result = sqlSession.selectOne("cn.cutepikachu.leaf.segment.dao.IDAllocMapper.getLeafAlloc", leafAlloc.getKey());
            sqlSession.commit();
            return result;
        }
    }

    @Override
    public List<String> getAllTags() {
        try (SqlSession sqlSession = sqlSessionFactory.openSession(false)) {
            return sqlSession.selectList("cn.cutepikachu.leaf.segment.dao.IDAllocMapper.getAllTags");
        }
    }
}
