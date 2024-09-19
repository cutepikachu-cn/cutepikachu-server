package cn.cutepikachu.common.snowflake.exception;

/**
 * 时钟回拨异常
 *
 * @author <a href="https://github.com/cutepikachu-cn">笨蛋皮卡丘</a>
 * @version 1.0
 * @since 2024-09-19 15:57-30
 */
public class ClockGoBackException extends RuntimeException {
    public ClockGoBackException(String message) {
        super(message);
    }
}
