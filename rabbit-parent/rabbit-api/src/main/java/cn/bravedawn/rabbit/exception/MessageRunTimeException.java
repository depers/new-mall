package cn.bravedawn.rabbit.exception;

/**
 * @author : depers
 * @program : rabbit-parent
 * @description: 消息运行时异常
 * @date : Created in 2021/2/24 22:14
 */
public class MessageRunTimeException extends RuntimeException{

    private static final long serialVersionUID = -919314392333997957L;

    public MessageRunTimeException() {
        super();
    }

    public MessageRunTimeException(String message) {
        super(message);
    }

    public MessageRunTimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public MessageRunTimeException(Throwable cause) {
        super(cause);
    }
}
