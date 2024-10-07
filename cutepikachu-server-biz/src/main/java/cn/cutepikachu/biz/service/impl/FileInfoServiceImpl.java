package cn.cutepikachu.biz.service.impl;

import cn.cutepikachu.biz.mapper.FileInfoMapper;
import cn.cutepikachu.biz.model.entity.FileInfo;
import cn.cutepikachu.biz.model.vo.FileInfoVO;
import cn.cutepikachu.biz.service.IFileInfoService;
import cn.cutepikachu.common.constant.DistributedBizTag;
import cn.cutepikachu.common.response.ResponseCode;
import cn.cutepikachu.common.response.ResponseEntity;
import cn.cutepikachu.common.util.ResponseUtils;
import cn.cutepikachu.common.util.ThrowUtils;
import cn.cutepikachu.inner.leaf.DistributedIDInnerService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 * 文件信息表 服务实现类
 *
 * @author <a href="https://github.com/cutepikachu-cn">笨蛋皮卡丘</a>
 * @version 0.0.1-SNAPSHOT
 * @since 2024-09-13 16:23:28
 */
@Service
public class FileInfoServiceImpl extends ServiceImpl<FileInfoMapper, FileInfo> implements IFileInfoService {

    @Resource
    private DistributedIDInnerService distributedIDInnerService;

    @Override
    public FileInfoVO saveFile(FileInfo fileInfo) {
        ResponseEntity<Long> resp = distributedIDInnerService.getDistributedID(DistributedBizTag.FILE);
        ResponseUtils.throwIfNotSuccess(resp);
        fileInfo.setFileId(resp.getData());
        ThrowUtils.throwIf(!save(fileInfo), ResponseCode.INTERNAL_SERVER_ERROR, "保存文件信息失败");
        return fileInfo.toVO(FileInfoVO.class);
    }

}
