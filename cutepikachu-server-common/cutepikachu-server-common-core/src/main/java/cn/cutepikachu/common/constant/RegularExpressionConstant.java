package cn.cutepikachu.common.constant;

/**
 * @author <a href="https://github.com/cutepikachu-cn">笨蛋皮卡丘</a>
 * @version 1.0
 * @since 2024-07-30 21:05-14
 */
public class RegularExpressionConstant {

    /**
     * 手机号正则表达式
     * 以 1 开头，后面跟 10 位数
     */
    public static final String MOBILE_REGEXP = "^1[0-9]{10}$";

    /**
     * 用户名户正则表达式
     * 1. 4-16 位数字字母下划线
     */
    public static final String USERNAME_REGEXP = "^[a-zA-Z0-9_-]{4,16}$";

    /**
     * 用户密码正则表达式
     * 1. 6-20 数字字母下划线
     */
    public static final String PASSWORD_REGEXP = "^[a-zA-Z0-9_-]{6,20}$";

    /**
     * 用户昵称正则表达式
     * 1. 由数 2-16 位字符组成
     */
    public static final String NICK_NAME_REGEXP = "^[\\w\\u4e00-\\u9fa5]{2,16}$";

    /**
     * 字段名正则表达式
     * 1. 数字字母下划线
     */
    public static final String FIELD_REGEXP = "[a-zA-Z0-9_]+";

    /**
     * 邮箱正则表达式
     */
    public static final String EMAIL_REGEXP = "[\\w!#$%&'*+/=?^_`{|}~-]+(?:\\.[\\w!#$%&'*+/=?^_`{|}~-]+)*@(?:[\\w](?:[\\w-]*[\\w])?\\.)+[\\w](?:[\\w-]*[\\w])?";

}
