package cn.bravedawn.exception;

import cn.bravedawn.enums.ExceptionEnum;
import org.apache.commons.lang3.StringUtils;

/**
 * @Author : fengx9
 * @Project : new-mall
 * @Date : Created in 2024-01-02 11:31
 */
public class BaseRunTimeException extends RuntimeException {

    private String code;
    private String message;
    private String userMessage;


    public BaseRunTimeException(ExceptionEnum exceptionEnum) {
        this(StringUtils.join("[" + exceptionEnum.getCode() + "]", "-", exceptionEnum.getMessage()), false);
        this.code = exceptionEnum.getCode();
        this.message = exceptionEnum.getMessage();
        this.userMessage = exceptionEnum.getUserMessage();
    }

    /**
     * 打印异常堆栈，禁用抑制异常
     * @param exceptionEnum
     * @param cause
     */
    public BaseRunTimeException(ExceptionEnum exceptionEnum, Throwable cause) {
        super(StringUtils.join("[" + exceptionEnum.getCode() + "]", "-", exceptionEnum.getMessage()), cause, false, true);
        this.code = exceptionEnum.getCode();
        this.message = exceptionEnum.getMessage();
        this.userMessage = exceptionEnum.getUserMessage();

    }

    /**
     * @param message
     * @param recordStackTrace 是否需要记录异常堆栈
     */
    private BaseRunTimeException(String message, boolean recordStackTrace) {
        super(message, null, false, recordStackTrace);
    }

    public String getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public String getUserMessage() {
        return userMessage;
    }
}
