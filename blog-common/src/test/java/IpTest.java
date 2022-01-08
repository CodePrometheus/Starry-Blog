import com.star.common.tool.IpUtil;
import org.junit.Test;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @Author: zzStar
 * @Date: 03-28-2021 10:55
 */
public class IpTest {

    @Test
    public void ipTest() {
        String ipSource = IpUtil.getIpSource("10.145.92.88");
        System.out.println(ipSource);
    }
}
