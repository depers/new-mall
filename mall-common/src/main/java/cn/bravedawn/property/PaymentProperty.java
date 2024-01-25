package cn.bravedawn.property;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @Author : fengx9
 * @Project : new-mall
 * @Date : Created in 2024-01-04 17:22
 *
 *
 * 支付相关配置
 */


@Component
@ConfigurationProperties(prefix = "mall.payment")
@Data
public class PaymentProperty {

    // 支付中心的调用地址
    private String url;

    // 支付回调的地址
    private String payReturnUrl;
}
