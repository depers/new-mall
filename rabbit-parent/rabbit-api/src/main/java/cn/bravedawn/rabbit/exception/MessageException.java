package cn.bravedawn.rabbit.exception;

/**
 * @author : depers
 * @program : rabbit-parent
 * @description: 消息异常
 * @date : Created in 2021/2/24 22:11
 */
public class MessageException extends Exception{

    private static final long serialVersionUID = 48164885560182677L;

    public MessageException() {
        super();
    }

    public MessageException(String message) {
        super(message);
    }

    public MessageException(String message, Throwable cause) {
        super(message, cause);
    }

    public MessageException(Throwable cause) {
        super(cause);
    }

}
