package cn.bravedawn.demo.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisStringCommands;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.data.redis.core.types.Expiration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**
 * @author : depers
 * @program : demo
 * @description: RedisLock
 * @date : Created in 2021/5/24 19:56
 */

@RestController
@Slf4j
public class RedisLockController {

    @Autowired
    private RedisTemplate redisTemplate;

    @RequestMapping("redisLock")
    public String redisLock() throws InterruptedException {
        log.info("我进入了方法");

        String key = "redisKey";
        String value = UUID.randomUUID().toString();

        RedisCallback<Boolean> redisCallback = connection -> {
            // 设置NX
            RedisStringCommands.SetOption setOption = RedisStringCommands.SetOption.ifAbsent();
            // 设置时间
            Expiration expiration = Expiration.seconds(30);
            // 序列化key
            byte[] redisKey = redisTemplate.getKeySerializer().serialize(key);
            // 序列化value
            byte[] redisValue = redisTemplate.getValueSerializer().serialize(value);
            // 执行setNx操作
            Boolean result = connection.set(redisKey, redisValue, expiration, setOption);
            return result;
        };

        Boolean lock = (Boolean) redisTemplate.execute(redisCallback);

        Thread.sleep(60000);

        if (lock) {
            String script = "if redis.call(\"get\", KEYS[1]) == ARGV[1] then\n" +
                    "\treturn redis.call(\"del\", KEYS[1])\n" +
                    "else\n" +
                    "\treturn 0\n" +
                    "end";
            RedisScript<Boolean> redisScript = RedisScript.of(script, Boolean.class);
            List<String> keys = Arrays.asList(key);

            Boolean result = (Boolean) redisTemplate.execute(redisScript, keys, value);
            log.info("释放锁的结果：" + result);
        }

        log.info("方法执行完成");
        return "方法执行完成";
    }

}
