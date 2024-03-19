package cn.bravedawn.consumer.component;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

/**
 * @author : depers
 * @program : mall-rabbit
 * @description: 消息消费者
 * @date : Created in 2021/2/19 21:03
 */
@Component
public class RabbitReceiver {

    /**
     * 组合使用监听
     * @RabbitListener @QueueBinding @Queue @Exchange
     * @param message
     * @param channel
     * @throws Exception
     */
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "${spring.rabbitmq.listener.order.queue.name}", durable = "${spring.rabbitmq.listener.order.queue.durable}"),
            exchange = @Exchange(name = "${spring.rabbitmq.listener.order.exchange.name}",
                                 durable = "${spring.rabbitmq.listener.order.exchange.durable}",
                                 type = "${spring.rabbitmq.listener.order.exchange.type}",
                                 ignoreDeclarationExceptions = "true"),
            key = "${spring.rabbitmq.listener.order.exchange.key}"
            )
    )
    @RabbitHandler
    public void onMessage(Message message, Channel channel) throws Exception{
        // 1.收到消息以后进行业务端消费处理
        System.err.println("-----------------------");
        System.err.println("消费消息:" + message.getPayload());

        // 2.处理成功之后 获取deliveryTag 并进行手工的ACK操作, 因为我们配置文件里配置的是 手工签收
        // spring.rabbitmq.listener.simple.acknowledge-mode=manual
        Long deliveryTag = (Long)message.getHeaders().get(AmqpHeaders.DELIVERY_TAG);
        channel.basicAck(deliveryTag, false);
    }
}
