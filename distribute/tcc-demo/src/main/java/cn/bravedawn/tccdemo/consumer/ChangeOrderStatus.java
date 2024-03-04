package cn.bravedawn.tccdemo.consumer;

import cn.bravedawn.tccdemo.dao.db140.OrdersMapper;
import cn.bravedawn.tccdemo.model.db140.Orders;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

/**
 * @author : depers
 * @program : tcc-demo
 * @description: 订单消费者
 * @date : Created in 2021/9/26 14:57
 */
@Component("messageListener")
@Slf4j
public class ChangeOrderStatus implements MessageListenerConcurrently {

    @Autowired
    private OrdersMapper ordersMapper;


    @Override
    public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> list, ConsumeConcurrentlyContext consumeConcurrentlyContext) {
        if (list.size() == 0) {
            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        }

        for (MessageExt msg : list) {
            String orderId = msg.getKeys();
            String msgBody = new String(msg.getBody());
            log.info("msg, orderId={}, msgBody={}", orderId, msgBody);

            Orders order = ordersMapper.selectByPrimaryKey(Integer.parseInt(orderId));
            if (order == null) {
                return ConsumeConcurrentlyStatus.RECONSUME_LATER;
            }

            try {
                order.setOrderStatus(1); // 设置为已支付
                order.setUpdateTime(new Date());
                order.setUpdateUser(0); // 系统更新
                ordersMapper.updateByPrimaryKey(order);
            } catch (Throwable e) {
                log.info("更新订单数据报错", e);
                return ConsumeConcurrentlyStatus.RECONSUME_LATER;
            }

        }

        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
    }
}
