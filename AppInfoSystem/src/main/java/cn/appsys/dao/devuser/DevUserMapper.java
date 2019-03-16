package cn.appsys.dao.devuser;

import org.apache.ibatis.annotations.Param;

import cn.appsys.pojo.DevUser;

/**
 * @author Administrator
 *	开发者平台
 */
public interface DevUserMapper {
	public DevUser devLogin(@Param("devCode") String devCode);//登录操作
}
