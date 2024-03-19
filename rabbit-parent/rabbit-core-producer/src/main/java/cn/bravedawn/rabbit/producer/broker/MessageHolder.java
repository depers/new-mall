package cn.bravedawn.rabbit.producer.broker;

import cn.bravedawn.rabbit.api.Message;
import com.google.common.collect.Lists;

import java.util.List;

/**
 * @author : depers
 * @program : rabbit-parent
 * @description: 消息缓存
 * @date : Created in 2021/3/16 22:02
 */
public class MessageHolder {

    private List<Message> messages = Lists.newArrayList();

    public static final ThreadLocal<MessageHolder> holder = new ThreadLocal() {
        @Override
        protected Object initialValue() {
            return new MessageHolder();
        }
    };

    public static void add(Message message) {
        holder.get().messages.add(message);
    }

    public static List<Message> clear() {
        List<Message> tmp = Lists.newArrayList(holder.get().messages);
        holder.remove();
        return tmp;
    }

}
