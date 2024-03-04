package cn.bravedawn.demo;

import cn.bravedawn.demo.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.*;

/**
 * @author : depers
 * @program : demo
 * @description: DistributeDemo测试
 * @date : Created in 2021/5/7 23:02
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class DistributeDemoTest {

    @Autowired
    private OrderService orderService;

    @Test
    public void concurrentOrder() throws InterruptedException {
        CountDownLatch cdl = new CountDownLatch(5);
        CyclicBarrier cyclicBarrier = new CyclicBarrier(5);

        ExecutorService es = Executors.newFixedThreadPool(5);
        for (int i = 0; i < 5; i++) {
            es.execute(() -> {
                try {
                    // 让五个线程同步开始执行创建订单的操作
                    cyclicBarrier.await();
//                    Integer orderId = orderService.createOrderV1();
//                    Integer orderId = orderService.createOrderV2();
//                    Integer orderId = orderService.createOrderV3();
//                    Integer orderId = orderService.createOrderV4();
//                    Integer orderId = orderService.createOrderV5();
                    Integer orderId = orderService.createOrderV6();
                    System.out.println(Thread.currentThread().getName() + " 订单id: " + orderId);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    // 每执行完一个线程减1
                    cdl.countDown();
                }
            });
        }
        // 等待5个线程都执行完毕后，再关闭线程池杀掉主进程。如果不用CountDownLatch让线程进行等待的话，
        // 主线程提前结束会导致数据库连接关闭，新开的五个线程就获取不到数据库连接了，效果就是什么都没做
        cdl.await();
        es.shutdown();
    }


    @Test
    public void testCuratorLock(){
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
        CuratorFramework client = CuratorFrameworkFactory.newClient("localhost:2181", retryPolicy);
        client.start();
        InterProcessMutex lock = new InterProcessMutex(client, "/order");
        try {
            if ( lock.acquire(30, TimeUnit.SECONDS) ) {
                try  {
                    log.info("我获得了锁！！！");
                }
                finally  {
                    lock.release();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        client.close();
    }


    @Test
    public void testRedissonLock() {
        Config config = new Config();
        config.useSingleServer().setAddress("redis://localhost:6379");
        RedissonClient redissonClient = Redisson.create(config);

        RLock rLock = redissonClient.getLock("order");

        try {
            log.info("请求锁, count={}, name={}", rLock.getHoldCount(), rLock.getName());
            rLock.lock(60, TimeUnit.SECONDS);
            log.info("我获得了锁！");

            Thread.sleep(20000);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            log.info("我释放了锁！！");
            rLock.unlock();
        }

    }

    /**
     * 测试时先执行testRedissonLock方法，然后再启动该方法
     * redisson trylock方法是阻塞的，如果拿不到锁他就会等待waittime，如果waittime时间到了还没获取到锁就放弃了
     */
    @Test
    public void testRedissonLockNotTry() throws InterruptedException {
        Config config = new Config();
        config.useSingleServer().setAddress("redis://localhost:6379");
        RedissonClient redissonClient = Redisson.create(config);

        RLock rLock = redissonClient.getLock("order");
        log.info("请求锁, count={}, name={}", rLock.getHoldCount(), rLock.getName());

        rLock.lock(30, TimeUnit.SECONDS);
        log.info("我获得了锁！");

        Thread.sleep(10000);
        rLock.unlock();
        log.info("我释放了锁！！");

    }

    /**
     * 测试时先执行testRedissonLock方法，然后再启动该方法
     * redisson trylock方法是非阻塞的，如果拿不到锁他就会等待waittime，如果waittime时间到了还没获取到锁就放弃了
     */
    @Test
    public void testRedissonTrylock() {
        Config config = new Config();
        config.useSingleServer().setAddress("redis://localhost:6379");
        RedissonClient redissonClient = Redisson.create(config);

        RLock rLock = redissonClient.getLock("order");

        try {
            log.info("请求锁------------");
            boolean b = rLock.tryLock(10, 300, TimeUnit.SECONDS);
            if (b) {
                log.info("我获得了锁！");
            } else {
                log.info("我没有获得了锁！");
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            log.info("我释放了锁！！");
            // rLock.unlock();
        }

    }

}
