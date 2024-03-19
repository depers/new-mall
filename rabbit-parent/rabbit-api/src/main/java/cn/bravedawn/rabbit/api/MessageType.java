package cn.bravedawn.rabbit.api;

/**
 * @author : depers
 * @program : rabbit-parent
 * @description: 消息的类型
 * @date : Created in 2021/2/24 21:55
 */
public final class MessageType {

    /**
     * 迅速消息：不需要保障消息的可靠性，也不需要做confirm确认
     */
    public final static String RAPID = "0";

    /**
     * 确认消息：不需要保障消息的可靠性，但是会做消息的confirm确认
     */
    public final static String CONFIRM = "1";

    /**
     * 可靠性消息：一定要保障消息的100%可靠性投递，不允许有任何消息的丢失
     * PS：保障数据库和所发消息是原子性的（最终一致性的）
     */
    public final static String RELIANT = "2";


}
