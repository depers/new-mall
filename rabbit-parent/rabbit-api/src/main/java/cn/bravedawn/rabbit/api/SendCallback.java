package cn.bravedawn.rabbit.api;

/**
 * @author : depers
 * @program : rabbit-parent
 * @description: 回调函数操作
 * @date : Created in 2021/2/25 20:41
 */
public interface SendCallback {

    void onSuccess();

    void onFailure();
}
