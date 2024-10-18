import cn.cutepikachu.auth.AuthApplication;
import cn.cutepikachu.auth.model.convert.UserConvert;
import cn.cutepikachu.common.model.auth.entity.AuthAccount;
import cn.cutepikachu.common.model.user.entity.User;
import cn.cutepikachu.common.model.user.vo.UserInfoVO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.BeanUtils;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author <a href="https://github.com/cutepikachu-cn">笨蛋皮卡丘</a>
 * @version 1.0
 * @since 2024-10-06 21:34-45
 */
@SpringBootTest(classes = AuthApplication.class)
public class MainTest {

    private static final UserConvert USER_CONVERT = UserConvert.INSTANCE;

    @Test
    void testCopyBean() {
        AuthAccount authAccount = new AuthAccount();
        User user = new User();
        authAccount.setUserId(10000001318L);
        user.setUserId(1842921003967303682L);
        UserInfoVO userInfoVO = new UserInfoVO();
        BeanUtils.copyProperties(user, userInfoVO);
        BeanUtils.copyProperties(authAccount, userInfoVO);
        System.out.println(userInfoVO);
    }
}
