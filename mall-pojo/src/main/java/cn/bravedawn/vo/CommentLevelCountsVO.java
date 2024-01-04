package cn.bravedawn.vo;

import lombok.Data;

/**
 * 用于展示商品评价数量的vo
 */

@Data
public class CommentLevelCountsVO {

    public Integer totalCounts;
    public Integer goodCounts;
    public Integer normalCounts;
    public Integer badCounts;
}
