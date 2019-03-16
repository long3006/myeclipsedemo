package cn.appsys.service.devuser.impl;

import javax.annotation.Resource;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

import cn.appsys.dao.devuser.DevUserMapper;
import cn.appsys.pojo.DevUser;
import cn.appsys.service.devuser.DevUserService;

@Service
public class DevUserServiceImpl implements DevUserService {

	@Resource
	private DevUserMapper devUserMapper;
	@Override
	public DevUser devLogin(String devCode,@Param("devPassword") String devPassword) {
		DevUser duser = devUserMapper.devLogin(devCode);
		//查找用户
		if (duser!=null) {
			//验证密码
			if (!devPassword.equals(duser.getDevPassword())) {
				duser=null;
			}
		}
		return duser;
	}

}
