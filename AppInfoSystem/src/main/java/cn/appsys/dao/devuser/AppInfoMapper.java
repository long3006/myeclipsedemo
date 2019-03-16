package cn.appsys.dao.devuser;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.appsys.pojo.AppInfo;

public interface AppInfoMapper {
	
	/**
	 * 根据条件查询App信息
	 * @param softwareName
	 * @param categoryLevel
	 * @param queryStatus
	 * @param queryFlatformId
	 * @param index
	 * @param pageSize
	 * @return
	 */
	public List<AppInfo> queryAppInfoList(@Param("softwareName") String softwareName,
										@Param("categoryLevel") int categoryLevel,
										@Param("queryStatus") int queryStatus,
										@Param("queryFlatformId")int queryFlatformId,
										@Param("index")int index,
										@Param("pageSize")int pageSize);
	/**
	 * 根据条件查询App信息 条数
	 * @param softwareName
	 * @param categoryLevel
	 * @param queryStatus
	 * @param queryFlatformId
	 * @return
	 */
	public int count(@Param("softwareName") String softwareName,
			@Param("categoryLevel") int categoryLevel,
			@Param("queryStatus") int queryStatus,
			@Param("queryFlatformId")int queryFlatformId);
	/**
	 * 新增版本
	 * @param appinfo
	 * @return
	 */
	public int addVersion(AppInfo appinfo);
	/**
	 * 修改版本
	 * @param appinfo
	 * @return
	 */
	public int modifyVersion(AppInfo appinfo);
	/**
	 * 新增app
	 * @param appinfo
	 * @return
	 */
	public int addAppInfo(AppInfo appinfo);
	/**
	 * 修改信息
	 * @param appinfo
	 * @return
	 */
	public int modifyAppInfo(AppInfo appinfo);
	/**
	 * 查看
	 * @param id
	 * @return
	 */
	public AppInfo viewApp(@Param("id")int id);
	/**
	 * 删除apk的logo图片
	 * @param id
	 * @return
	 */
	public int delAppLogo(@Param("id")int id);
	/**
	 * 删除
	 * @param id
	 * @return
	 */
	public int deleteApp(@Param("id")int id);
	/**
	 * APKName重名验证
	 * @param APKName
	 * @return
	 */
	public int checkAppName(@Param("APKName")String APKName);
	/**
	 * 上下架操作
	 * @param id
	 * @param status
	 * @return
	 */
	public int modifyStatus(@Param("id")int id,@Param("status")String status);
}
