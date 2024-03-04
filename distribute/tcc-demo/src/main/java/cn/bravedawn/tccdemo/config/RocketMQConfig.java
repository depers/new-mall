package cn.bravedawn.tccdemo.config;

import org.apache.rocketmq.client.consumer.DefaultMQPullConsumer;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author : depers
 * @program : tcc-demo
 * @description: RocketMQ配置类
 * @date : Created in 2021/9/26 13:30
 */

@Configuration
public class RocketMQConfig {

    public static final String MQ_GROUP_NAME = "paymentGroup";
    public static final String MQ_TOPICS = "payment";


    @Bean(initMethod = "start", destroyMethod = "shutdown")
    public DefaultMQProducer defaultMQProducer() {
        // Instantiate with a producer group name.
        DefaultMQProducer producer = new
                DefaultMQProducer(MQ_GROUP_NAME);
        // Specify name server addresses.
        producer.setNamesrvAddr("192.168.156.135:9876");
        return producer;
    }


    @Bean(initMethod = "start", destroyMethod = "shutdown")
    public DefaultMQPushConsumer defaultMQPushConsumer(@Qualifier("messageListener") MessageListenerConcurrently messageListener)
            throws MQClientException {
        // Instantiate with specified consumer group name.
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer(MQ_GROUP_NAME);

        // Specify name server addresses.
        consumer.setNamesrvAddr("192.168.156.135:9876");

        // Subscribe one more more topics to consume.
        consumer.subscribe(MQ_TOPICS, "*");

        consumer.registerMessageListener(messageListener);

        return consumer;
    }
}
