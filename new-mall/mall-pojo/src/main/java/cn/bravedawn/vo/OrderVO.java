package cn.bravedawn.vo;

import cn.bravedawn.bo.ShopCartBO;
import lombok.Data;

import java.util.List;

/**
 * @Author : fengx9
 * @Project : new-mall
 * @Date : Created in 2024-01-04 16:36
 */

@Data
public class OrderVO {

    private String orderId;
    private MerchantOrdersVO merchantOrdersVO;
    private List<ShopCartBO> toBeRemovedShopcatdList;

}
