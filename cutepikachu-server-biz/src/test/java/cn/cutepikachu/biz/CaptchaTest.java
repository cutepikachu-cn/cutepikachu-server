package cn.cutepikachu.biz;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.GifCaptcha;
import cn.hutool.core.lang.Console;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;

/**
 * @author <a href="https://github.com/cutepikachu-cn">笨蛋皮卡丘</a>
 * @version 1.0
 * @since 2024-10-13 22:25-48
 */
public class CaptchaTest {

    @Test
    void captcha() throws FileNotFoundException {
        // 定义图形验证码的长和宽
        GifCaptcha captcha = CaptchaUtil.createGifCaptcha(200, 100, 5);
        // 图形验证码写出，可以写出到文件，也可以写出到流
        captcha.write("f:/captcha.gif");
        Console.log("图形验证码: {}", captcha.getCode());
        // 验证图形验证码的有效性，返回 boolean 值
        boolean verify = captcha.verify("1234");
        Console.log("验证图形验证码: {}", verify);

    }

}
