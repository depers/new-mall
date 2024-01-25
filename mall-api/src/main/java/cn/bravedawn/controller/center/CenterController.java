package cn.bravedawn.controller.center;

import cn.bravedawn.dto.JsonResult;
import cn.bravedawn.pojo.Users;
import cn.bravedawn.service.center.CenterUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "用户中心-公共", description = "用户中心展示的相关接口")
@RestController
@RequestMapping("center")
public class CenterController {

    @Autowired
    private CenterUserService centerUserService;

    @Operation(summary = "获取用户信息", description = "获取用户信息")
    @GetMapping("userInfo")
    public JsonResult userInfo(
            @Parameter(description = "用户id", required = true)
            @RequestParam String userId) {

        Users user = centerUserService.queryUserInfo(userId);
        return JsonResult.ok(user);
    }


}
