package cn.bravedawn.vo;

import lombok.Data;

/**
 * @author : depers
 * @description :
 * @program : new-mall
 * @date : Created in 2024/1/8 22:57
 *
 * 用户中心，我的订单列表嵌套商品VO
 */

@Data
public class MySubOrderItemVO {

    private String itemId;
    private String itemImg;
    private String itemName;
    private String itemSpecId;
    private String itemSpecName;
    private Integer buyCounts;
    private Integer price;
}
