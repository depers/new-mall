package cn.bravedawn.controller;

import cn.bravedawn.dto.JsonResult;
import cn.bravedawn.pojo.Users;
import cn.bravedawn.service.UserService;
import cn.bravedawn.utils.JsonUtils;
import cn.bravedawn.utils.MD5Utils;
import cn.bravedawn.utils.RedisOperator;
import cn.bravedawn.vo.UserVO;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.UUID;

/**
 * @Author 冯晓
 * @Date 2019/12/26 20:18
 */
@Controller
public class SSOController {

    @Autowired
    private UserService userService;

    @Autowired
    private RedisOperator redisOperator;

    public static final String REDIS_USER_TOKEN = "redis_user_token";
    public static final String REDIS_USER_TICKET = "redis_user_ticket";
    public static final String REDIS_TMP_TICKET = "redis_tmp_token";

    public static final String COOKIE_USER_TICKET = "cookie_user_ticket";

    @GetMapping("/login")
    public String login(String returnUrl,
                        Model model,
                        HttpServletRequest request,
                        HttpServletResponse response){

        model.addAttribute("returnUrl", returnUrl);

        // 1. 获取userTicket门票，如果cookie中能够获取到，证明用户登录过，此时签发一个一次性的临时票据并且回跳
        String userTicket = getCookie(request, COOKIE_USER_TICKET);

        boolean isVerified = verifyUserTicket(userTicket);
        if (isVerified){
            String tmpTicket = createTmpTicket();
            return "redirect:" + returnUrl + "?tmpTicket=" + tmpTicket;
        }

        // 2. 用户从未登录过，第一次进入则跳转到CAS的统一登录页面
        return "login";
    }

    /**
     * 验证CAS全局用户门票
     * @param userTicket
     * @return
     */
    private boolean verifyUserTicket(String userTicket){

        // 1. 验证CAS门票不能为空
        if (StringUtils.isBlank(userTicket)){
            return false;
        }

        // 2. 验证CAS门票是否有效
        String userId = redisOperator.get(REDIS_USER_TICKET + ":" + userTicket);
        if (StringUtils.isBlank(userId)){
            return false;
        }

        // 3. 验证门票对应的user会话是否存在
        String userRedis = redisOperator.get(REDIS_USER_TOKEN + ":" + userId);
        if (StringUtils.isBlank(userRedis)){
            return false;
        }

        return true;
    }


    /**
     * CAS的统一登录接口
     *       目的：
     *           1. 登录后创建用户的全局会话                 -> uniqueToken
     *           2. 创建用户全局门票，用以表示在CAS端是否登录   -> userTicket
     *           3. 创建用户的临时票据，用于回跳回传           -> tmpTicket
     *
     *       userTicket: 用于表示用户在CAS端的一个登录状态：已经登录
     *       tmpTicket: 用于颁发给用户进行一次性的验证的票据，有时效性
     *
     *       举例：
     *          我们去动物园玩耍，大门口买了一张统一的门票，这个就是CAS系统的全局门票和用户全局会话；
     *          动物园里有一些小的景点，需要凭你的门票去领取一次性的票据，有了这张票据以后就能去一些小的站点游玩了；
     *          这样的一个个小的景点其实就是我们这里所对应的一个个站点；
     *          当我们使用完毕这张临时票据以后，就需要销毁。
     */
    @PostMapping("doLogin")
    public String doLogin(String username,
                          String password,
                          String returnUrl,
                          Model model,
                          HttpServletRequest request,
                          HttpServletResponse response) throws Exception{

        model.addAttribute("returnUrl", returnUrl);

        // 判断用户名和密码必须不为空
        if (StringUtils.isBlank(username) ||
                StringUtils.isBlank(password)) {
            model.addAttribute("errmsg", "用户名或密码不能为空");
            return "login";
        }

        // 1. 实现登录
        Users userResult = userService.queryUserForLogin(username,
                MD5Utils.getMD5Str(password));
        if (userResult == null) {
            model.addAttribute("errmsg", "用户名或密码不正确");
            return "login";
        }

        // 2. 生成用户token，存入redis会话
        String uniqueToken = UUID.randomUUID().toString().trim();
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(userResult, userVO);
        userVO.setUserUniqueToken(uniqueToken);
        redisOperator.set(REDIS_USER_TOKEN + ":" + userResult.getId(), JsonUtils.objectToJson(userVO));

        // 3. 生成ticket门票，全局门票，代表用户在CAS端登录过
        String userTicket = UUID.randomUUID().toString().trim();

         // 3.1 用户全局门票需要放入CAS端的cookie中
        setCookie(COOKIE_USER_TICKET, userTicket, response);

        // 4. userTicket关联用户id，并且放入到redis中，代表这个用户有门票了，可以在各个景区游玩
        redisOperator.set(REDIS_USER_TICKET + ":" + userTicket, userResult.getId());

        // 5. 生成临时票据，会跳到调用端网站，是由CAS端所签发的一个一次性的临时ticket
        String tmpTicket = createTmpTicket();

        // return "login";
        return "redirect:" + returnUrl + "?tmpTicket=" + tmpTicket;
    }

    @PostMapping("/verifyTmpTicket")
    @ResponseBody
    public JsonResult verifyTmpTicket(String tmpTicket,
                        HttpServletRequest request,
                        HttpServletResponse response) throws Exception{

        // 使用一次性临时票据验证用户是否登录，如果登录过，把用户会话信息返回给站点
        // 使用完毕之后，需要销毁临时票据
        String tmpTicketValue = redisOperator.get(REDIS_TMP_TICKET + ":" + tmpTicket);
        if (StringUtils.isBlank(tmpTicketValue)){
            return JsonResult.errorUserTicket("用户票据异常");
        }

        // 1. 如果临时票据ok，则需要销毁，并且拿到CAS端cookie中的全局userTicket，以此再获取用户会话
        if (!tmpTicketValue.equals(MD5Utils.getMD5Str(tmpTicket))){
            return JsonResult.errorUserTicket("用户票据异常");
        }else {
            // 销毁临时票据
            redisOperator.del(REDIS_TMP_TICKET + ":" + tmpTicket);
        }

        // 2. 验证并且获取用户的userTicket
        String userTicket = getCookie(request, COOKIE_USER_TICKET);
        String userId = redisOperator.get(REDIS_USER_TICKET + ":" + userTicket);
        if (StringUtils.isBlank(userId)){
            return JsonResult.errorUserTicket("用户票据异常");
        }

        // 3. 验证门票对应的user会话是否存在
        String userRedis = redisOperator.get(REDIS_USER_TOKEN + ":" + userId);
        if (StringUtils.isBlank(userRedis)){
            return JsonResult.errorUserTicket("用户票据异常");
        }

        // 4. 验证成功，返回用户会话信息
        return JsonResult.ok(JsonUtils.jsonToPojo(userRedis, UserVO.class));
    }


    /**
     * 用户退出登录
     * @param userId
     * @param request
     * @param response
     * @return
     */
    @PostMapping("logout")
    @ResponseBody
    public JsonResult logout(String userId, HttpServletRequest request, HttpServletResponse response){
        // 1. 获取CAS中的用户门票
        String userTicket = getCookie(request, COOKIE_USER_TICKET);

        // 2. 清除userTicket票据，redis/cookie
        delCookie(COOKIE_USER_TICKET, response);
        redisOperator.del(REDIS_USER_TICKET + ":" + userTicket);

        // 3. 清除用户全局会话(分布式会话)
        redisOperator.del(REDIS_USER_TOKEN + ":" + userId);

        return JsonResult.ok();
    }

    /**
     * 创建临时票据
     * @return
     */
    private String createTmpTicket(){
        String tmpTicket = UUID.randomUUID().toString().trim();

        try {
            redisOperator.set(REDIS_TMP_TICKET + ":" + tmpTicket,
                    MD5Utils.getMD5Str(tmpTicket), 600);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tmpTicket;
    }


    /**
     * 设置cookie
     * @param key
     * @param val
     * @param response
     */
    private void setCookie(String key, String val, HttpServletResponse response){
        Cookie cookie = new Cookie(key, val);
        cookie.setDomain("sso.com");
        cookie.setPath("/");
        response.addCookie(cookie);
    }


    /**
     * 获取cookie
     * @param request
     * @param key
     * @return
     */
    private String getCookie(HttpServletRequest request, String key){
        Cookie[] cookieList = request.getCookies();
        if (cookieList == null || StringUtils.isBlank(key)){
            return null;
        }

        String cookieValue = null;
        for (int i = 0; i < cookieList.length; i++){
            if (cookieList[i].getName().equals(key)){
                cookieValue = cookieList[i].getValue();
                break;
            }
        }

        return cookieValue;
    }

    /**
     * 删除cookie
     * @param key
     * @param response
     */
    private void delCookie(String key, HttpServletResponse response){
        Cookie cookie = new Cookie(key, null);
        cookie.setDomain("sso.com");
        cookie.setPath("/");
        cookie.setMaxAge(-1);
        response.addCookie(cookie);
    }

}
