package cn.appsys.dao.backenduser;

import org.apache.ibatis.annotations.Param;

import cn.appsys.pojo.BackendUser;

/**
 * @author Administrator
 *	后台管理员
 */
public interface BackendUserMapper {
	public BackendUser backLogin(@Param("userCode") String userCode);//登录操作
}
