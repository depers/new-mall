package cn.bravedawn.demo.service;

import cn.bravedawn.demo.dao.OrderItemMapper;
import cn.bravedawn.demo.dao.OrderMapper;
import cn.bravedawn.demo.dao.ProductMapper;
import cn.bravedawn.demo.model.Order;
import cn.bravedawn.demo.model.OrderItem;
import cn.bravedawn.demo.model.Product;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Service
@Slf4j
public class OrderService {

    @Resource
    private OrderMapper orderMapper;

    @Resource
    private OrderItemMapper orderItemMapper;

    @Resource
    private ProductMapper productMapper;

    @Autowired
    private PlatformTransactionManager platformTransactionManager;

    @Autowired
    private TransactionDefinition transactionDefinition;

    private Lock lock = new ReentrantLock();

    //购买商品id
    private int purchaseProductId = 1;

    //购买商品数量
    private int purchaseProductNum = 1;


    /**
     * 五个人同时下单，存库为1，五人下单后数据库中有5个订单，商品库存变为了0
     * @return
     * @throws Exception
     */
    @Transactional(rollbackFor = Exception.class)
    public Integer createOrderV1() throws Exception{
        Product product = productMapper.selectByPrimaryKey(purchaseProductId);
        if (product==null){
            throw new Exception("购买商品：" + purchaseProductId + "不存在");
        }

        // 商品当前库存
        Integer currentCount = product.getCount();

        // 校验库存
        if (purchaseProductNum > currentCount) {
            throw new Exception("商品" + purchaseProductId + "仅剩" + currentCount + "件，无法购买");
        }

        // 计算剩余库存，是放在程序中进行的 ---------------------------------
        Integer leftCount = currentCount - purchaseProductNum;

        // 更新库存
        product.setCount(leftCount);
        product.setUpdateTime(new Date());
        product.setUpdateUser("user");
        productMapper.updateByPrimaryKey(product);
        // ------------------------------------------------------------

        Order order = new Order();
        order.setOrderAmount(product.getPrice().multiply(new BigDecimal(purchaseProductNum)));
        order.setOrderStatus(1);//待处理
        order.setReceiverName("xxx");
        order.setReceiverMobile("13311112222");
        order.setCreateTime(new Date());
        order.setCreateUser("xxx");
        order.setUpdateTime(new Date());
        order.setUpdateUser("xxx");
        orderMapper.insertSelective(order);

        OrderItem orderItem = new OrderItem();
        orderItem.setOrderId(order.getId());
        orderItem.setProductId(product.getId());
        orderItem.setPurchasePrice(product.getPrice());
        orderItem.setPurchaseNum(purchaseProductNum);
        orderItem.setCreateUser("xxx");
        orderItem.setCreateTime(new Date());
        orderItem.setUpdateTime(new Date());
        orderItem.setUpdateUser("xxx");
        orderItemMapper.insertSelective(orderItem);
        return order.getId();
    }


    /**
     * 利用数据库update行锁来解决超卖现象
     * 五个人同时下单，存库为1，五个人下单后效果就是还是会生成5个订单，并且库存会减为-4
     * @return
     * @throws Exception
     */
    @Transactional(rollbackFor = Exception.class)
    public Integer createOrderV2() throws Exception{
        Product product = productMapper.selectByPrimaryKey(purchaseProductId);
        if (product==null){
            throw new Exception("购买商品：" + purchaseProductId + "不存在");
        }

        // 商品当前库存
        Integer currentCount = product.getCount();

        // 校验库存
        if (purchaseProductNum > currentCount) {
            throw new Exception("商品" + purchaseProductId + "仅剩" + currentCount + "件，无法购买");
        }

        // 计算剩余库存
        productMapper.updateProductCount(purchaseProductNum, "xxx", new Date(), product.getId());

        Order order = new Order();
        order.setOrderAmount(product.getPrice().multiply(new BigDecimal(purchaseProductNum)));
        order.setOrderStatus(1);//待处理
        order.setReceiverName("xxx");
        order.setReceiverMobile("13311112222");
        order.setCreateTime(new Date());
        order.setCreateUser("xxx");
        order.setUpdateTime(new Date());
        order.setUpdateUser("xxx");
        orderMapper.insertSelective(order);

        OrderItem orderItem = new OrderItem();
        orderItem.setOrderId(order.getId());
        orderItem.setProductId(product.getId());
        orderItem.setPurchasePrice(product.getPrice());
        orderItem.setPurchaseNum(purchaseProductNum);
        orderItem.setCreateUser("xxx");
        orderItem.setCreateTime(new Date());
        orderItem.setUpdateTime(new Date());
        orderItem.setUpdateUser("xxx");
        orderItemMapper.insertSelective(orderItem);
        return order.getId();
    }


    /**
     * 基于`Synchronized`锁解决超卖问题（最原始的锁）+ 声明式事务
     * 效果还是会出现库存变为负数的情况，因为第一个线程获得并释放锁之后，事务并没有提交；
     * 导致第二个线程查到的库存还是1，进而导致库存减为负数的现象
     * @return
     * @throws Exception
     */
    @Transactional(rollbackFor = Exception.class)
    public synchronized Integer createOrderV3() throws Exception{
        Product product = productMapper.selectByPrimaryKey(purchaseProductId);
        if (product == null){
            throw new Exception("购买商品：" + purchaseProductId + "不存在");
        }

        // 商品当前库存
        Integer currentCount = product.getCount();
        System.out.println(Thread.currentThread().getName() + "库存数：" + currentCount);

        // 校验库存
        if (purchaseProductNum > currentCount) {
            throw new Exception("商品" + purchaseProductId + "仅剩" + currentCount + "件，无法购买");
        }

        // 计算剩余库存
        productMapper.updateProductCount(purchaseProductNum, "xxx", new Date(), product.getId());

        Order order = new Order();
        order.setOrderAmount(product.getPrice().multiply(new BigDecimal(purchaseProductNum)));
        order.setOrderStatus(1);//待处理
        order.setReceiverName("xxx");
        order.setReceiverMobile("13311112222");
        order.setCreateTime(new Date());
        order.setCreateUser("xxx");
        order.setUpdateTime(new Date());
        order.setUpdateUser("xxx");
        orderMapper.insertSelective(order);

        OrderItem orderItem = new OrderItem();
        orderItem.setOrderId(order.getId());
        orderItem.setProductId(product.getId());
        orderItem.setPurchasePrice(product.getPrice());
        orderItem.setPurchaseNum(purchaseProductNum);
        orderItem.setCreateUser("xxx");
        orderItem.setCreateTime(new Date());
        orderItem.setUpdateTime(new Date());
        orderItem.setUpdateUser("xxx");
        orderItemMapper.insertSelective(orderItem);
        return order.getId();
    }

    /**
     * 基于`Synchronized`锁解决超卖问题（最原始的锁）+ 手动式事务
     * 通过手动式事务控制事务的提交和回滚进而保证一个获得锁的线程，可以及时的提交事务，保证不出现第二种超卖现象
     * @return
     * @throws Exception
     */
    public synchronized Integer createOrderV4() throws Exception{
        TransactionStatus transactionStatus = platformTransactionManager.getTransaction(transactionDefinition);

        try {
            Product product = productMapper.selectByPrimaryKey(purchaseProductId);
            if (product == null){
                throw new Exception("购买商品：" + purchaseProductId + "不存在");
            }

            // 商品当前库存
            Integer currentCount = product.getCount();
            System.out.println(Thread.currentThread().getName() + " 库存数：" + currentCount);

            // 校验库存
            if (purchaseProductNum > currentCount) {
                throw new Exception("商品" + purchaseProductId + "仅剩" + currentCount + "件，无法购买");
            }

            // 计算剩余库存
            productMapper.updateProductCount(purchaseProductNum, "xxx", new Date(), product.getId());

            Order order = new Order();
            order.setOrderAmount(product.getPrice().multiply(new BigDecimal(purchaseProductNum)));
            order.setOrderStatus(1);//待处理
            order.setReceiverName("xxx");
            order.setReceiverMobile("13311112222");
            order.setCreateTime(new Date());
            order.setCreateUser("xxx");
            order.setUpdateTime(new Date());
            order.setUpdateUser("xxx");
            orderMapper.insertSelective(order);

            OrderItem orderItem = new OrderItem();
            orderItem.setOrderId(order.getId());
            orderItem.setProductId(product.getId());
            orderItem.setPurchasePrice(product.getPrice());
            orderItem.setPurchaseNum(purchaseProductNum);
            orderItem.setCreateUser("xxx");
            orderItem.setCreateTime(new Date());
            orderItem.setUpdateTime(new Date());
            orderItem.setUpdateUser("xxx");
            orderItemMapper.insertSelective(orderItem);
            // 提交事务
            platformTransactionManager.commit(transactionStatus);
            return order.getId();
        } catch (Throwable t) {
            // 回滚事务
            platformTransactionManager.rollback(transactionStatus);
            System.out.println(t.getMessage());
        }

        return null;
    }


    /**
     * 基于`Synchronized`块锁解决超卖问题（最原始的锁）+ 手动式事务
     * 注意：下面这段代码分别起了两个事务，注意不要嵌套事务
     * @return
     * @throws Exception
     */
    public synchronized Integer createOrderV5() throws Exception{
        Product product = null;
        synchronized (this) {
            // 开启第一个事务
            TransactionStatus transactionStatus = platformTransactionManager.getTransaction(transactionDefinition);
            product = productMapper.selectByPrimaryKey(purchaseProductId);
            if (product == null){
                throw new Exception("购买商品：" + purchaseProductId + "不存在");
            }

            // 商品当前库存
            Integer currentCount = product.getCount();
            System.out.println(Thread.currentThread().getName() + "库存数：" + currentCount);

            // 校验库存
            if (purchaseProductNum > currentCount) {
                throw new Exception("商品" + purchaseProductId + "仅剩" + currentCount + "件，无法购买");
            }

            // 计算剩余库存
            productMapper.updateProductCount(purchaseProductNum, "xxx", new Date(), product.getId());
            platformTransactionManager.commit(transactionStatus);
        }

        // 开启第二个事务
        TransactionStatus transactionStatus = platformTransactionManager.getTransaction(transactionDefinition);
        Order order = new Order();
        order.setOrderAmount(product.getPrice().multiply(new BigDecimal(purchaseProductNum)));
        order.setOrderStatus(1);//待处理
        order.setReceiverName("xxx");
        order.setReceiverMobile("13311112222");
        order.setCreateTime(new Date());
        order.setCreateUser("xxx");
        order.setUpdateTime(new Date());
        order.setUpdateUser("xxx");
        orderMapper.insertSelective(order);

        OrderItem orderItem = new OrderItem();
        orderItem.setOrderId(order.getId());
        orderItem.setProductId(product.getId());
        orderItem.setPurchasePrice(product.getPrice());
        orderItem.setPurchaseNum(purchaseProductNum);
        orderItem.setCreateUser("xxx");
        orderItem.setCreateTime(new Date());
        orderItem.setUpdateTime(new Date());
        orderItem.setUpdateUser("xxx");
        orderItemMapper.insertSelective(orderItem);
        platformTransactionManager.commit(transactionStatus);
        return order.getId();
    }


    /**
     * 基于`ReentrantLock`锁解决超卖问题（并发包中的锁）
     * 注意：这里一定要用try...finally包住并发代码
     * @return
     * @throws Exception
     */
    public synchronized Integer createOrderV6() throws Exception{
        TransactionStatus transactionStatus = platformTransactionManager.getTransaction(transactionDefinition);

        try {
            lock.lock();
            Product product = null;
            try {
                product = productMapper.selectByPrimaryKey(purchaseProductId);
                if (product == null){
                    throw new Exception("购买商品：" + purchaseProductId + "不存在");
                }

                // 商品当前库存
                Integer currentCount = product.getCount();
                System.out.println(Thread.currentThread().getName() + " 库存数：" + currentCount);

                // 校验库存
                if (purchaseProductNum > currentCount) {
                    throw new Exception("商品" + purchaseProductId + "仅剩" + currentCount + "件，无法购买");
                }

                // 计算剩余库存
                productMapper.updateProductCount(purchaseProductNum, "xxx", new Date(), product.getId());
            } finally {
                lock.unlock();
            }

            Order order = new Order();
            order.setOrderAmount(product.getPrice().multiply(new BigDecimal(purchaseProductNum)));
            order.setOrderStatus(1);//待处理
            order.setReceiverName("xxx");
            order.setReceiverMobile("13311112222");
            order.setCreateTime(new Date());
            order.setCreateUser("xxx");
            order.setUpdateTime(new Date());
            order.setUpdateUser("xxx");
            orderMapper.insertSelective(order);

            OrderItem orderItem = new OrderItem();
            orderItem.setOrderId(order.getId());
            orderItem.setProductId(product.getId());
            orderItem.setPurchasePrice(product.getPrice());
            orderItem.setPurchaseNum(purchaseProductNum);
            orderItem.setCreateUser("xxx");
            orderItem.setCreateTime(new Date());
            orderItem.setUpdateTime(new Date());
            orderItem.setUpdateUser("xxx");
            orderItemMapper.insertSelective(orderItem);
            // 提交事务
            platformTransactionManager.commit(transactionStatus);
            return order.getId();
        } catch (Throwable t) {
            // 回滚事务
            platformTransactionManager.rollback(transactionStatus);
            System.out.println(t.getMessage());
        }

        return null;
    }

}
