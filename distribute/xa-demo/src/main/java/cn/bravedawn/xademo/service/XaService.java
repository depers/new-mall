package cn.bravedawn.xademo.service;

import cn.bravedawn.xademo.dao.db139.UserMapper139;
import cn.bravedawn.xademo.dao.db140.UserMapper140;
import cn.bravedawn.xademo.model.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * @author : depers
 * @program : xa-demo
 * @description: xaservice
 * @date : Created in 2021/9/10 22:26
 */
@Service
public class XaService {

    @Resource
    private UserMapper139 usersMapper139;

    @Resource
    private UserMapper140 usersMapper140;


    @Transactional(transactionManager = "xaTransaction")
    public void testXa() throws InterruptedException {
        User user = new User();
        user.setUsername("张三");
        usersMapper139.insertSelective(user);
        // 除零来测试回滚，测试正常数据可以将这句测试语句删除
        int i = 1 / 0;
        usersMapper140.insertSelective(user);
    }

}
