package cn.cutepikachu.common.model.biz.enums;

import cn.cutepikachu.common.model.BaseEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 对象存储类型枚举
 *
 * @author <a href="https://github.com/cutepikachu-cn">笨蛋皮卡丘</a>
 * @version 1.0
 * @since 2024-09-13 19:59-55
 */
@Getter
@AllArgsConstructor
public enum OssType implements BaseEnum<String> {

    MINIO("MinIO");

    private final String desc;

}
