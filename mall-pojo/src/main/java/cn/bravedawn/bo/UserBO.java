package cn.bravedawn.bo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author : fengx9
 * @Project : new-mall
 * @Date : Created in 2024-01-08 10:24
 */
@ApiModel(value = "用户对象BO", description = "从客户端，由用户传入的数据封装在此entity中")
@Data
public class UserBO {

    @ApiModelProperty(value = "用户名", name = "username", example = "imooc", required = true)
    private String username;
    @ApiModelProperty(value = "密码", name = "password", example = "123456", required = true)
    private String password;
    @ApiModelProperty(value = "确认密码", name = "confirmPassword", example = "123456", required = false)
    private String confirmPassword;


}
