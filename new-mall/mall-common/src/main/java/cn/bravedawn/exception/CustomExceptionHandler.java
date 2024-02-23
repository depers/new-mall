package cn.bravedawn.exception;

import cn.bravedawn.dto.JsonResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @Author : fengx9
 * @Project : new-mall
 * @Date : Created in 2024-01-02 11:30
 */

@RestControllerAdvice
@Slf4j
public class CustomExceptionHandler {

    /**
     * 这里Spring会捕获特定的异常分开进行处理
     */


    @ExceptionHandler(BaseRunTimeException.class)
    public JsonResult handleBaseRunTimeException(BaseRunTimeException e) {
        log.error("发生业务逻辑错误");
        return JsonResult.errorMsg(e.getUserMessage());
    }

    @ExceptionHandler(Throwable.class)
    public JsonResult handleThrowable(Throwable e) {
        log.error("发生未知异常");
        return JsonResult.errorMsg(e.getMessage());
    }

}
