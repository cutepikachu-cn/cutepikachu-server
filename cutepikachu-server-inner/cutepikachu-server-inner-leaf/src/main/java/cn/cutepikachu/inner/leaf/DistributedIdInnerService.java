package cn.cutepikachu.inner.leaf;

import cn.cutepikachu.common.constant.FeignConstant;
import cn.cutepikachu.common.response.BaseResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 分布式 ID 内部服务接口
 *
 * @author <a href="https://github.com/cutepikachu-cn">笨蛋皮卡丘</a>
 * @version 1.0
 * @since 2024-08-03 18:51-59
 */
@FeignClient(value = "cutepikachu-server-leaf")
public interface DistributedIdInnerService {

    /**
     * 获取分布式 ID
     *
     * @param bizTag 业务标签
     * @return 分布式 ID
     */
    @GetMapping(FeignConstant.INNER_SERVICE_PREFIX + "/get/distributed/id")
    BaseResponse<Long> getDistributedID(@RequestParam String bizTag);

}
