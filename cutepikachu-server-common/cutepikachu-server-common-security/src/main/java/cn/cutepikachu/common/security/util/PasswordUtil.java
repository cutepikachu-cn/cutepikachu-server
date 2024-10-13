package cn.cutepikachu.common.security.util;

import cn.cutepikachu.common.security.config.SecurityConfiguration;
import cn.hutool.crypto.digest.DigestAlgorithm;
import cn.hutool.crypto.digest.DigestUtil;
import cn.hutool.crypto.digest.Digester;

/**
 * 密码工具类
 *
 * @author <a href="https://github.com/cutepikachu-cn">笨蛋皮卡丘</a>
 * @version 1.0
 * @since 2024-08-03 11:12-38
 */
public class PasswordUtil {

    private final Digester digester;

    public PasswordUtil(SecurityConfiguration securityConfiguration) {
        DigestAlgorithm algorithm = securityConfiguration.getAlgorithm();
        this.digester = DigestUtil.digester(algorithm)
                .setSalt(securityConfiguration.getSalt().getBytes());
    }

    /**
     * 加密密码
     *
     * @param password 密码
     * @return 加密后的密码
     */
    public String crypto(String password) {
        return digester.digestHex(password);
    }

    /**
     * 验证密码
     *
     * @param password        密码
     * @param encodedPassword 加密后的密码
     * @return 是否匹配
     */
    public boolean matches(String password, String encodedPassword) {
        return digester.digestHex(password).equals(encodedPassword);
    }

}
