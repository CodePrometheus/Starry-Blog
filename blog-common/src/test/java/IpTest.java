import com.star.tool.IpUtil;
import org.junit.Test;

/**
 * @Author: zzStar
 * @Date: 03-28-2021 10:55
 */
public class IpTest {

    @Test
    public void ipTest() {
        String ipSource = IpUtil.getIpSource("8.8.8.8");
        System.out.println(ipSource);
    }
}
