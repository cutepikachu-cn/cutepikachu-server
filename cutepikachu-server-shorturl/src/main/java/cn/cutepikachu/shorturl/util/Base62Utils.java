package cn.cutepikachu.shorturl.util;

import java.util.Random;

/**
 * Base62 编码工具类
 *
 * @author <a href="https://github.com/cutepikachu-cn">笨蛋皮卡丘</a>
 * @version 1.0
 * @since 2024-09-02 20:18-35
 */
public class Base62Utils {

    private static final String ALPHABET = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

    private static final Random RANDOM = new Random();

    private static final String shuffledAlphabet;

    static {
        shuffledAlphabet = shuffleString();
    }

    public static String generateBase62ShortUrl(Long urlId) {
        return base62Encode(urlId);
    }

    private static String base62Encode(Long decimal) {
        StringBuilder result = new StringBuilder();
        while (decimal > 0) {
            int remainder = (int) (decimal % 62);
            result.append(shuffledAlphabet.charAt(remainder));
            decimal /= 62;
        }
        return result.reverse().toString();
    }

    private static Long base62Decode(String base62) {
        long result = 0L;
        for (int i = 0; i < base62.length(); i++) {
            result = result * 62 + shuffledAlphabet.indexOf(base62.charAt(i));
        }
        return result;
    }

    private static String shuffleString() {
        char[] chars = ALPHABET.toCharArray();
        for (int i = chars.length - 1; i > 0; i--) {
            int j = RANDOM.nextInt(i + 1);
            char tmp = chars[i];
            chars[i] = chars[j];
            chars[j] = tmp;
        }
        return new String(chars);
    }

}
