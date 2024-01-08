package cn.bravedawn.vo;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author : depers
 * @description :
 * @program : new-mall
 * @date : Created in 2024/1/8 22:56
 */
@Data
public class MyOrdersVO {

    private String orderId;
    private Date createdTime;
    private Integer payMethod;
    private Integer realPayAmount;
    private Integer postAmount;
    private Integer isComment;
    private Integer orderStatus;
    private List<MySubOrderItemVO> subOrderItemList;
}
