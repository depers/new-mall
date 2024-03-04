package cn.bravedawn.demo.service;

import cn.bravedawn.demo.lock.RedisLock;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

/**
 * @author : depers
 * @program : demo
 * @description: 定时任务
 * @date : Created in 2021/5/25 13:45
 */
@Service
@Slf4j
public class SchedulerService {

    @Autowired
    private RedisTemplate redisTemplate;

//    @Scheduled(cron = "0/5 * * * * ?")
    public void sendSms() {
        try (RedisLock redisLock = new RedisLock(redisTemplate, "autoSms", 30)){
            if (redisLock.getLock()) {
                log.info("获取锁成功");
                log.info("向138xxxxxxxx发送短信！");
            } else {
                log.info("获取锁失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
