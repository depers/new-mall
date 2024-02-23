package cn.bravedawn.vo;

import lombok.Data;

import java.util.Date;

/**
 * @author : depers
 * @description :
 * @program : new-mall
 * @date : Created in 2024/1/8 22:58
 */


@Data
public class MyCommentVO {

    private String commentId;
    private String content;
    private Date createdTime;
    private String itemId;
    private String itemName;
    private String specName;
    private String itemImg;
}
