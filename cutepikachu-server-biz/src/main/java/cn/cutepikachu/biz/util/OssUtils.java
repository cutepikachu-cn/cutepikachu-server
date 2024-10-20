package cn.cutepikachu.biz.util;

import cn.cutepikachu.common.model.biz.enums.FileBizTag;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;

import java.util.Date;

/**
 * @author <a href="https://github.com/cutepikachu-cn">笨蛋皮卡丘</a>
 * @version 1.0
 * @since 2024-09-13 22:34-45
 */
public class OssUtils {

    public static String getObjectPath(FileBizTag bizTag) {
        StringBuilder filaPath = new StringBuilder();
        // 获取今天零点时间戳
        long zero = DateUtil.beginOfDay(new Date()).getTime();
        // 生成文件名
        String fileName = IdUtil.simpleUUID();
        filaPath.append(bizTag.name())
                .append("/")
                .append(zero)
                .append("/")
                .append(fileName);
        return filaPath.toString();
    }

}
