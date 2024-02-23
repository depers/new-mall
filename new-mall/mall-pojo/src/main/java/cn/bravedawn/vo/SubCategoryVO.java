package cn.bravedawn.vo;

import lombok.Data;

/**
 * @Author 冯晓
 * @Date 2020/1/6 15:48
 */
@Data
public class SubCategoryVO {

    private Integer subId;
    private String subName;
    private String subType;
    private Integer subFatherId;
}
