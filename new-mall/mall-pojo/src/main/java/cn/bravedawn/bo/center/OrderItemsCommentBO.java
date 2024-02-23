package cn.bravedawn.bo.center;

import lombok.Data;

/**
 * @Author : fengx9
 * @Project : new-mall-main
 * @Date : Created in 2024-01-09 15:28
 */

@Data
public class OrderItemsCommentBO {

    private String commentId;
    private String itemId;
    private String itemName;
    private String itemSpecId;
    private String itemSpecName;
    private Integer commentLevel;
    private String content;
}
