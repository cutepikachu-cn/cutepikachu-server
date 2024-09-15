package cn.cutepikachu.xtimer.model.dto;

import lombok.Data;

import java.util.Map;

/**
 * 定时任务执行回调 HTTP 请求参数
 *
 * @author <a href="https://github.com/cutepikachu-cn">笨蛋皮卡丘</a>
 * @version 1.0
 * @since 2024-08-13 15:52-34
 */
@Data
public class NotifyHTTPParam {

    private String method;

    private String url;

    private Map<String, String> header;

    private String body;

}
