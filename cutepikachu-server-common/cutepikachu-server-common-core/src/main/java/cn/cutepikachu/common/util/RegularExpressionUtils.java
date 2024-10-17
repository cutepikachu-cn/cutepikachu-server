package cn.cutepikachu.common.util;

import cn.hutool.core.util.ReUtil;

import static cn.cutepikachu.common.constant.RegularExpressionConstant.*;

/**
 * 正则表达式工具
 *
 * @author <a href="https://github.com/cutepikachu-cn">笨蛋皮卡丘</a>
 * @version 1.0
 * @since 2024-0-28 17:55:55
 */
public class RegularExpressionUtils {

    /**
     * 是否是有效的用户名
     *
     * @param value 输入值
     * @return 匹配结果
     */
    public static boolean isValidUsername(String value) {
        return isMatch(USERNAME_REGEXP, value);
    }

    /**
     * 是否是有效的密码
     *
     * @param value 输入值
     * @return 匹配结果
     */
    public static boolean isValidPassword(String value) {
        return isMatch(PASSWORD_REGEXP, value);
    }

    /**
     * 是否是有效的用户昵称
     *
     * @param value 输入值
     * @return 匹配结果
     */
    public static boolean isValidNickName(String value) {
        return isMatch(NICK_NAME_REGEXP, value);
    }

    /**
     * 是否符合字段规则
     *
     * @param value 输入值
     * @return 匹配结果
     */
    public static boolean isValidField(String value) {
        return isMatch(FIELD_REGEXP, value);
    }

    /**
     * 是否是有效的手机号
     *
     * @param value 输入值
     * @return 匹配结果
     */
    public static boolean isValidMobile(String value) {
        return isMatch(MOBILE_REGEXP, value);
    }

    /**
     * 是否是有效的邮箱
     *
     * @param value 输入值
     * @return 匹配结果
     */
    public static boolean isValidEmail(String value) {
        return isMatch(EMAIL_REGEXP, value);
    }

    /**
     * 是否匹配表达式
     *
     * @param regexp 正则表达式
     * @param value  输入值
     * @return 匹配结果
     */
    public static boolean isMatch(String regexp, String value) {
        return ReUtil.isMatch(regexp, value);
    }

    /**
     * 是否包含匹配表达式
     *
     * @param regexp 正则表达式
     * @param value  输入值
     * @return 匹配结果
     */
    public static boolean isFind(String regexp, String value) {
        return ReUtil.contains(regexp, value);
    }

}
