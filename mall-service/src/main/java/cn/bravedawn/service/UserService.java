package cn.bravedawn.service;

import cn.bravedawn.bo.UserBO;
import cn.bravedawn.pojo.Users;

/**
 * @Author : fengx9
 * @Project : new-mall
 * @Date : Created in 2024-01-08 10:23
 */
public interface UserService {


    /**
     * 判断用户名是否存在
     */
    public boolean queryUsernameIsExist(String username);

    /**
     * 创建用户
     */
    public Users createUser(UserBO userBO);

    /**
     * 检索用户名和密码是否匹配，用于登录
     */
    public Users queryUserForLogin(String username, String password);
}
