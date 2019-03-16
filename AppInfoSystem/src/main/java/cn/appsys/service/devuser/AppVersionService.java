package cn.appsys.service.devuser;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.appsys.pojo.AppVersion;

public interface AppVersionService {
	/**
	 * 根据Id查找历史版本
	 * @return
	 */
	public List<AppVersion> getAppVersionById(@Param("id")int id);
	/**
	 * 根据id查找版本信息
	 * @param id
	 * @return
	 */
	public AppVersion getAppVersion(@Param("id")int id);
	/**
	 * 新增版本
	 * @param appversion
	 * @return
	 */
	public int addAppVersion(AppVersion appversion);
	/**
	 * 根据ID删除apk文件
	 * @param id
	 * @return
	 */
	public int deldownLink(@Param("id")int id);
	/**
	 * 修改版本
	 * @param appversion
	 * @return
	 */
	public int modifyAppVersion(AppVersion appversion);
	/**
	 * 根据app的appid删除版本
	 * @param versionId
	 * @return
	 */
	public int delAppVersion(@Param("appId")int appId);
}
