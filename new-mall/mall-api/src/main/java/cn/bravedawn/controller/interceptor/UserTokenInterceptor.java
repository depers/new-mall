package cn.bravedawn.controller.interceptor;

import cn.bravedawn.dto.JsonResult;
import cn.bravedawn.utils.JsonUtils;
import cn.bravedawn.utils.RedisOperator;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.io.OutputStream;

@Slf4j
public class UserTokenInterceptor implements HandlerInterceptor {


    public static final String REDIS_USER_TOKEN = "redis_user_token";

    @Autowired
    private RedisOperator redisOperator;


    /**
     * 拦截请求，在访问controller调用之前
     *
     * @param request
     * @param response
     * @param handler
     * @return false: 请求被拦截，被驳回，验证出现问题
     * true: 请求在经过验证校验以后，是ok的，是可以放行的
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String usersId = request.getHeader("headerUserId");
        String userToken = request.getHeader("headerUserToken");

        if (StringUtils.isNoneBlank(usersId) && StringUtils.isNotBlank(userToken)) {
            String uniqueToken = redisOperator.get(REDIS_USER_TOKEN + ":" + usersId);

            if (StringUtils.isNotBlank(uniqueToken)) {
                returnErrorResponse(response, JsonResult.errorMsg("请登录..."));
                return false;
            } else {
                if (!uniqueToken.equals(userToken)) {
                    returnErrorResponse(response, JsonResult.errorMsg("账号在异地登录..."));
                    return false;
                }
            }
        } else {
            returnErrorResponse(response, JsonResult.errorMsg("请登录..."));
            return false;
        }
        return true;
    }

    /**
     * 返回错误的信息
     * @param response
     * @param result
     */
    public void returnErrorResponse(HttpServletResponse response,
                                    JsonResult result){
        OutputStream out = null;
        try {
            response.setCharacterEncoding("utf-8");
            response.setContentType("text/json");
            out = response.getOutputStream();
            out.write(JsonUtils.objectToJson(result).getBytes("utf-8"));
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null){
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 请求访问controller之后，渲染视图之前
     *
     * @param request
     * @param response
     * @param handler
     * @param modelAndView
     * @throws Exception
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }


    /**
     * 请求controller之后，渲染视图之后
     *
     * @param request
     * @param response
     * @param handler
     * @param ex
     * @throws Exception
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
