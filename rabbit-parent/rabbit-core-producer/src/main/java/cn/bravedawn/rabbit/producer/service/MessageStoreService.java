package cn.bravedawn.rabbit.producer.service;

import cn.bravedawn.rabbit.producer.constant.BrokerMessageStatus;
import cn.bravedawn.rabbit.producer.entity.BrokerMessage;
import cn.bravedawn.rabbit.producer.mapper.BrokerMessageMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @author : depers
 * @program : rabbit-parent
 * @description: 消息落库
 * @date : Created in 2021/3/3 22:12
 */
@Service
public class MessageStoreService {

    @Autowired
    private BrokerMessageMapper brokerMessageMapper;

    public int insert(BrokerMessage message){
        return brokerMessageMapper.insert(message);
    }

    public BrokerMessage selectByMessageId(String messageId) {
        return brokerMessageMapper.selectByPrimaryKey(messageId);
    }

    public void success(String messageId){
        brokerMessageMapper.changeBrokerMessageStatus(messageId,
                BrokerMessageStatus.SEND_OK.getCode(), new Date());
    }

    public void failure(String messageId) {
        brokerMessageMapper.changeBrokerMessageStatus(messageId,
                BrokerMessageStatus.SEND_FAIL.getCode(), new Date());
    }

    public List<BrokerMessage> fetchTimeOutMessage4Retry(BrokerMessageStatus brokerMessageStatus){
        return brokerMessageMapper.queryBrokerMessageStatus4Timeout(brokerMessageStatus.getCode());
    }

    public int updateTryCount(String brokerMessageId) {
        return this.brokerMessageMapper.update4TryCount(brokerMessageId, new Date());
    }

}
