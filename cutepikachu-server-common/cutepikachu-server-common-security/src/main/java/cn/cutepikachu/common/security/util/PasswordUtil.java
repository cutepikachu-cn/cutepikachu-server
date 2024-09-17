package cn.cutepikachu.common.security.util;

import cn.cutepikachu.common.security.config.SecurityConfiguration;
import cn.cutepikachu.common.util.SpringContextUtils;
import cn.hutool.crypto.digest.DigestAlgorithm;
import cn.hutool.crypto.digest.DigestUtil;
import cn.hutool.crypto.digest.Digester;

import java.nio.charset.StandardCharsets;

/**
 * 密码工具类
 *
 * @author <a href="https://github.com/cutepikachu-cn">笨蛋皮卡丘</a>
 * @version 1.0
 * @since 2024-08-03 11:12-38
 */
public class PasswordUtil {

    private static String SALT;

    private static DigestAlgorithm ALGORITHM;

    private static final Digester DIGESTER;

    private static final SecurityConfiguration SECURITY_CONFIGURATION = SpringContextUtils.getBean(SecurityConfiguration.class);

    static {
        PasswordUtil.SALT = SECURITY_CONFIGURATION.getSalt();
        PasswordUtil.ALGORITHM = SECURITY_CONFIGURATION.getAlgorithm();
        DIGESTER = DigestUtil.digester(PasswordUtil.ALGORITHM);
        DIGESTER.setSalt(PasswordUtil.SALT.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * 加密密码
     *
     * @param password 密码
     * @return 加密后的密码
     */
    public static String crypto(String password) {
        return DIGESTER.digestHex(password);
    }

    /**
     * 验证密码
     *
     * @param password        密码
     * @param encodedPassword 加密后的密码
     * @return 是否匹配
     */
    public static boolean matches(String password, String encodedPassword) {
        return DIGESTER.digestHex(password).equals(encodedPassword);
    }

}
