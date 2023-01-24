import com.star.common.tool.IpUtils;
import org.junit.Test;

import jakarta.annotation.Resource;

/**
 * @Author: zzStar
 * @Date: 03-28-2021 10:55
 */
public class IpTest {

    @Test
    public void ipTest() {
        IpUtils.initIp2regionResource();
        String ipSource = IpUtils.getIpSource("20.247.36.169");
        System.out.println(ipSource);
    }

}
