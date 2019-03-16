package cn.appsys.service.backenduser;

import org.apache.ibatis.annotations.Param;

import cn.appsys.pojo.BackendUser;

public interface BackendUserService {
	public BackendUser backLogin(@Param("userCode") String userCode,@Param("userPassword") String userPassword);//登录操作

}
