package cn.appsys.service.backenduser.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.appsys.dao.backenduser.BackendUserMapper;
import cn.appsys.pojo.BackendUser;
import cn.appsys.service.backenduser.BackendUserService;

@Service
public class BackendUserServiceImpl implements BackendUserService {
	
	@Resource
	private BackendUserMapper backendusermapper;
	
	
	/* (non-Javadoc)
	 * @see cn.appsys.service.backenduser.BackendUserService#backLogin(java.lang.String)
	 * 后台登录操作
	 */
	@Override
	public BackendUser backLogin(String userCode,String userPassword) {
		BackendUser buser = backendusermapper.backLogin(userCode);
		//查找用户
		if (buser!=null) {
			//验证密码
			if (!userPassword.equals(buser.getUserPassword())) {
				buser=null;
			}
		}
		return buser;
	}

}
