package cn.cutepikachu.common.snowflake.controller;

import cn.cutepikachu.common.snowflake.service.SnowflakeIdGenerateService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 雪花算法测试接口
 *
 * @author <a href="https://github.com/cutepikachu-cn">笨蛋皮卡丘</a>
 * @version 1.0
 * @since 2024-09-19 15:00-55
 */
@RestController
public class SnowflakeTestController {

    @Resource
    private SnowflakeIdGenerateService snowflakeIdGenerateService;

    @GetMapping("/snowflake")
    public Long snowflake(@RequestParam String key) {
        return snowflakeIdGenerateService.nextId(key);
    }

}
