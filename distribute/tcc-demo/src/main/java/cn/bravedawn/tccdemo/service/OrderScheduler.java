package cn.bravedawn.tccdemo.service;

import cn.bravedawn.tccdemo.dao.db139.PaymentMsgMapper;
import cn.bravedawn.tccdemo.model.db139.PaymentMsg;
import cn.bravedawn.tccdemo.model.db139.PaymentMsgExample;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author : depers
 * @program : tcc-demo
 * @description:
 * @date : Created in 2021/9/24 21:21
 */
@Service
@Slf4j
public class OrderScheduler {

    @Autowired
    private PaymentMsgMapper paymentMsgMapper;


//    @Scheduled(cron = "0/10 * * * * ?")
    @Transactional(transactionManager = "tm139")
    public void orderNotify() throws IOException {
        log.info("------------------------执行定时任务开始------------------------");

        try {
            PaymentMsgExample paymentMsgExample = new PaymentMsgExample();
            paymentMsgExample.createCriteria().andStatusEqualTo(0);
            List<PaymentMsg> paymentMsgList = paymentMsgMapper.selectByExample(paymentMsgExample);

            if (paymentMsgList.size() == 0) {
                return;
            }

            for (PaymentMsg paymentMsg : paymentMsgList) {
                int orderId = paymentMsg.getOrderId();

                CloseableHttpClient httpClient = HttpClients.createDefault();
                HttpPost httpPost = new HttpPost("http://localhost:8080/handleOrder");

                NameValuePair nameValuePair = new BasicNameValuePair("orderId", String.valueOf(orderId));
                List<NameValuePair> list = new ArrayList<>();
                list.add(nameValuePair);

                HttpEntity httpEntity = new UrlEncodedFormEntity(list);
                httpPost.setEntity(httpEntity);

                CloseableHttpResponse httpResponse = httpClient.execute(httpPost);
                String resStr = EntityUtils.toString(httpEntity);

                if ("success".equals(resStr)) {
                    paymentMsg.setStatus(1);
                    paymentMsg.setUpdateUser(0);
                    paymentMsg.setUpdateTime(new Date());
                    paymentMsgMapper.updateByPrimaryKey(paymentMsg);

                } else {
                    int failureCnt = paymentMsg.getFailureCnt();
                    failureCnt++;
                    paymentMsg.setFailureCnt(failureCnt);

                    if (failureCnt >= 5) {
                        paymentMsg.setStatus(2); // 超过最大发送次数
                        paymentMsg.setUpdateTime(new Date());
                        paymentMsg.setUpdateUser(0); // 系统更新
                        paymentMsgMapper.updateByPrimaryKey(paymentMsg);
                    }
                }

            }
        } finally {
            log.info("------------------------执行定时任务结束------------------------");
        }


    }
}
