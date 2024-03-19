package cn.bravedawn.rabbit.producer.constant;

/**
 * @author : depers
 * @program : rabbit-parent
 * @description: 消息发送状态
 * @date : Created in 2021/3/3 22:15
 */
public enum BrokerMessageStatus {


    SENDING("0", "发送中"),
    SEND_OK("1", "发送成功"),
    SEND_FAIL("2", "发送失败"),
    SEND_FAIL_A_MOMENT("3", "");

    private String code;

    private String desc;

    BrokerMessageStatus(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
