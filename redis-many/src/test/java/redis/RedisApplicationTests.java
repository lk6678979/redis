package redis;

import com.demo.redis.RedisApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = RedisApplication.class)
public class RedisApplicationTests {

    @Autowired
    @Qualifier("redisTemplate")
    RedisTemplate redisTemplate;

    @Autowired
    @Qualifier("redisTemplate2")
    RedisTemplate redisTemplate2;

    @Autowired
    @Qualifier("stringRedisTemplate2")
    StringRedisTemplate stringRedisTemplate;

    @Test
    public void contextLoads() {
        Map<Object, Object> ss = stringRedisTemplate.opsForHash().entries("monitor:ALARM_RULATION:gb");
        System.out.println("测试");
    }
}
