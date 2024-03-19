package cn.bravedawn.rabbit.producer.task;

import cn.bravedawn.rabbit.producer.broker.RabbitBroker;
import cn.bravedawn.rabbit.producer.constant.BrokerMessageConst;
import cn.bravedawn.rabbit.producer.constant.BrokerMessageStatus;
import cn.bravedawn.rabbit.producer.entity.BrokerMessage;
import cn.bravedawn.rabbit.producer.service.MessageStoreService;
import cn.bravedawn.rabbit.task.annotation.ElasticJobConfig;
import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.dataflow.DataflowJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author : depers
 * @program : rabbit-parent
 * @description: 可靠性消息补偿任务
 * @date : Created in 2021/3/15 22:03
 */
@Component
@ElasticJobConfig(
        name = "cn.bravedawn.rabbit.producer.task.RetryMessageDataflowJob",
        cron = "0/10 * * * * ?",
        description = "可靠性消息补偿任务",
        overwrite = true,
        shardingTotalCount = 1
)
@Slf4j
public class RetryMessageDataflowJob implements DataflowJob<BrokerMessage> {

    @Autowired
    private MessageStoreService messageStoreService;

    @Autowired
    private RabbitBroker rabbitBroker;


    @Override
    public List<BrokerMessage> fetchData(ShardingContext shardingContext) {
        List<BrokerMessage> list = messageStoreService.fetchTimeOutMessage4Retry(BrokerMessageStatus.SENDING);
        log.info("抓取数据集合，数量={}.", list.size());
        return list;
    }

    @Override
    public void processData(ShardingContext shardingContext, List<BrokerMessage> data) {
        data.forEach(brokerMessage -> {
            String messageId = brokerMessage.getMessageId();
            if(brokerMessage.getTryCount() >= BrokerMessageConst.MAX_RETRY_COUNT) {
                messageStoreService.failure(messageId);
                log.warn("消息设置为最终失败，消息ID={}.", messageId);
            } else {
                log.info("进行消息重发，messageId={}, tryCount={}.", messageId, brokerMessage.getTryCount() + 1);
                // 每次重发的时候要更新一下try count字段
                messageStoreService.updateTryCount(messageId);
                // 重发消息
                rabbitBroker.reliantSend(brokerMessage.getMessage());
            }
        });
    }
}
