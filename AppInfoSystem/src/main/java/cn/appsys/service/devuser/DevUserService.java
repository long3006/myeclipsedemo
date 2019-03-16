package cn.appsys.service.devuser;

import org.apache.ibatis.annotations.Param;

import cn.appsys.pojo.DevUser;

public interface DevUserService {
	public DevUser devLogin(@Param("devCode") String devCode,@Param("devPassword") String devPassword);//登录操作

}
