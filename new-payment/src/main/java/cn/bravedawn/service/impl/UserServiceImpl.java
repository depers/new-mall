package cn.bravedawn.service.impl;

import cn.bravedawn.mapper.UsersMapper;
import cn.bravedawn.pojo.Users;
import cn.bravedawn.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UsersMapper usersMapper;

	@Transactional(propagation=Propagation.SUPPORTS)
	@Override
	public Users queryUserInfo(String userId, String pwd) {

		Users user = new Users();
		user.setImoocUserId(userId);
		user.setPassword(pwd);

		return usersMapper.selectOne(user);
	}

}