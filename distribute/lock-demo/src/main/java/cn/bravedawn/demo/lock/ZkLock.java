package cn.bravedawn.demo.lock;

import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

/**
 * @author : depers
 * @program : demo
 * @description: zookeeper锁
 * @date : Created in 2021/6/4 21:57
 */
@Slf4j
public class ZkLock implements AutoCloseable, Watcher {

    private ZooKeeper zooKeeper;
    private String zkNode;

    public ZkLock() throws IOException {
        this.zooKeeper = new ZooKeeper("localhost:2181", 600000, this);
    }
    
    public boolean getLock(String businessCode) {
        try {
            // 创建业务根节点
            String rootNode = "/" + businessCode;
            Stat stat = zooKeeper.exists(rootNode, false);
            if (stat == null) {
                // 对根节点要进行持久化
                zooKeeper.create(rootNode, businessCode.getBytes(),
                        ZooDefs.Ids.OPEN_ACL_UNSAFE,
                        CreateMode.PERSISTENT);
            }

            // 创建瞬时有序节点 /order/order_0000001，从1开始逐次递增。zk会维护这个顺序
            zkNode = zooKeeper.create(rootNode + rootNode + "_", businessCode.getBytes(),
                    ZooDefs.Ids.READ_ACL_UNSAFE,
                    CreateMode.EPHEMERAL_SEQUENTIAL);

            // 获取业务节点下 所有的子节点
            List<String> childrenNode = zooKeeper.getChildren(rootNode, false);
            // 子节点排序
            Collections.sort(childrenNode);
            // 获取序号最小的（第一个）子节点
            String firstNode = childrenNode.get(0);
            // 如果创建的节点是第一个子节点，则获得锁
            if (zkNode.endsWith(firstNode)) {
                return true;
            }

            // 不是第一个子节点，则当前节点监听他的前一个节点
            String lastNode = firstNode;
            for (String node : childrenNode) {
                if (zkNode.endsWith(node)) {
                    zooKeeper.exists(rootNode + "/" + lastNode, true);
                    break;
                } else {
                    lastNode = node;
                }
            }

            // 这里加锁的原因在于，若同一台服务器同时有多个线程同时请求锁，就要在这里进行堵塞，不然都会直接返回true
            synchronized (this) {
                wait();
            }
            return true;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


    @Override
    public void close() throws Exception {
        zooKeeper.delete(zkNode, -1);
        zooKeeper.close();
        log.info("我已经释放了锁！");
    }

    @Override
    public void process(WatchedEvent watchedEvent) {
        if (watchedEvent.getType() == Event.EventType.NodeDeleted) {
            synchronized (this) {
                notify();
            }
        }
    }

}
