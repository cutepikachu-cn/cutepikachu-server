package cn.cutepikachu.biz.dao.repository;

import cn.cutepikachu.biz.dao.mapper.FileInfoMapper;
import cn.cutepikachu.common.model.biz.entity.FileInfo;
import com.baomidou.mybatisplus.extension.repository.CrudRepository;
import org.springframework.stereotype.Component;

/**
 * @author <a href="https://github.com/cutepikachu-cn">笨蛋皮卡丘</a>
 * @version 1.0
 * @since 2024-10-24 21:18-01
 */
@Component
public class FileInfoRepository extends CrudRepository<FileInfoMapper, FileInfo> {
}
