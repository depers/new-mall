package cn.bravedawn.bo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @Author : fengx9
 * @Project : new-mall
 * @Date : Created in 2024-01-08 10:24
 */
@Schema(description = "从客户端，由用户传入的数据封装在此entity中")
@Data
public class UserBO {

    @Schema(description = "用户名", example = "imooc", required = true)
    private String username;
    @Schema(description = "密码", example = "123456", required = true)
    private String password;
    @Schema(description = "确认密码", example = "123456", required = false)
    private String confirmPassword;


}
