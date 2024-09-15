package cn.cutepikachu.common.constant;

import cn.hutool.core.bean.copier.CopyOptions;

/**
 * 通用常量
 *
 * @author <a href="https://github.com/cutepikachu-cn">笨蛋皮卡丘</a>
 * @version 1.0
 * @since 2024-0-28 17:55:55
 */
public class CommonConstant {

    /**
     * 盐，混淆密码
     */
    public static final String SALT = "pikachu";

    /**
     * hutool 工具 BeanUtil.copyProperties() 方法默认拷贝选项
     */
    public static final CopyOptions DEFAULT_COPY_OPTIONS = CopyOptions.create()
            .ignoreError()
            .ignoreNullValue();

    /**
     * 默认头像 URL
     */
    public static final String DEFAULT_AVATAR_URL = "https://cdn.cutepikachu.cn/avatar/default_avatar.png";

}
