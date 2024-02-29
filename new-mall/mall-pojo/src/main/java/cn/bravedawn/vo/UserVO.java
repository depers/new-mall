package cn.bravedawn.vo;

import lombok.Data;

/**
 * @Author 冯晓
 * @Date 2020/6/23 22:59
 */
@Data
public class UserVO {

    /**
     * 用户id
     */
    private String id;

    /**
     * 用户名
     */
    private String username;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 头像
     */
    private String face;

    /**
     * 性别： 1-男，0-女，2-保密
     */
    private String sex;

    /**
     * 用户会话
     */
    private String userUniqueToken;



}
