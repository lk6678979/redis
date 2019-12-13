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

import java.util.Set;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = RedisApplication.class)
public class RedisApplicationTests {

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @Autowired
    RedisTemplate redisTemplate;

    @Autowired
    RedisTemplate<String,String> redisTemplate2;

    @Test
    public void contextLoads() {
        System.out.println("测试");
//        Set<String> keys = redisTemplate.keys("monitor:gb:info_*");
//        for(String item : keys){
//            redisTemplate.delete(item);
//            System.out.println(item);
//        }
//        System.out.println(keys);
    }

}
