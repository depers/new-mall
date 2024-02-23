package cn.bravedawn.enums;

/**
 * @Author : fengx9
 * @Project : new-mall
 * @Date : Created in 2024-01-02 11:34
 */
public enum ExceptionEnum {

    /**
     * 错误码定义
     * 第一位：1-业务异常，0-程序异常
     * 第二至: 1-主页，2-商品，3-订单，4-购物车
     *
     * 第三至五位：为错误码
     */

    // 业务异常
    IDEMPOTENT_EXCEPTION("10001", "错误，触发幂等性校验", "服务正忙，请稍后"),

    STOCK_DEFICIENCY_EXCEPTION("12001", "订单创建失败，原因：库存不足!", "库存不足，请稍后再试"),
    ;

    // 程序异常


    private String code;

    private String message;

    private String userMessage;


    ExceptionEnum(String code, String message, String userMessage) {
        this.code = code;
        this.message = message;
        this.userMessage = userMessage;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public String getUserMessage() {
        return userMessage;
    }
}
