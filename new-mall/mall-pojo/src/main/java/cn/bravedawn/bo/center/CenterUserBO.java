package cn.bravedawn.bo.center;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.util.Date;

/**
 * @Author : fengx9
 * @Project : new-mall-main
 * @Date : Created in 2024-01-09 14:59
 */
@Schema(description="从客户端，由用户传入的数据封装在此entity中")
@Data
public class CenterUserBO {

    @Schema(description ="用户名", example="json", required = false)
    private String username;

    @Schema(description="密码", example="123456", required = false)
    private String password;

    @Schema(description="确认密码", example="123456", required = false)
    private String confirmPassword;

    @NotBlank(message = "用户昵称不能为空")
    @Length(max = 12, message = "用户昵称不能超过12位")
    @Schema(description="用户昵称", example="杰森", required = false)
    private String nickname;

    @Length(max = 12, message = "用户真实姓名不能超过12位")
    @Schema(description="真实姓名", example="杰森", required = false)
    private String realname;

    @Pattern(regexp = "^(((13[0-9]{1})|(15[0-9]{1})|(18[0-9]{1}))+\\d{8})$", message = "手机号格式不正确")
    @Schema(description="手机号", example="13999999999", required = false)
    private String mobile;

    @Email
    @Schema(description="邮箱地址", example="imooc@imooc.com", required = false)
    private String email;

    @Min(value = 0, message = "性别选择不正确")
    @Max(value = 2, message = "性别选择不正确")
    @Schema(description="性别", example="0:女 1:男 2:保密", required = false)
    private Integer sex;


    @Schema(description="生日", example="1900-01-01", required = false)
    private Date birthday;


}
