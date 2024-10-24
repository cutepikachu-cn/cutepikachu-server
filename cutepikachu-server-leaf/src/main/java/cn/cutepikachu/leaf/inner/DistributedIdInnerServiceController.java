package cn.cutepikachu.leaf.inner;

import cn.cutepikachu.common.response.BaseResponse;
import cn.cutepikachu.common.response.ErrorCode;
import cn.cutepikachu.common.util.ResponseUtils;
import cn.cutepikachu.inner.leaf.DistributedIdInnerService;
import cn.cutepikachu.leaf.common.Result;
import cn.cutepikachu.leaf.common.Status;
import cn.cutepikachu.leaf.service.SegmentService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

import static cn.cutepikachu.common.exception.ExceptionFactory.sysException;

/**
 * 分布式 ID 内部服务实现
 *
 * @author <a href="https://github.com/cutepikachu-cn">笨蛋皮卡丘</a>
 * @version 1.0
 * @since 2024-08-03 18:58-08
 */
@RestController
public class DistributedIdInnerServiceController implements DistributedIdInnerService {

    @Resource
    private SegmentService segmentService;

    @Override
    public BaseResponse<Long> getDistributedID(String bizTag) {
        Result result = segmentService.getId(bizTag);
        if (Objects.equals(result.getStatus(), Status.EXCEPTION)) {
            throw sysException(ErrorCode.BAD_REQUEST, result.toString());
        }
        return ResponseUtils.success(result.getId());
    }

}
