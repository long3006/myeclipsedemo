package cn.appsys.service.devuser.impl;

import java.io.File;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.appsys.dao.devuser.AppInfoMapper;
import cn.appsys.dao.devuser.AppVersionMapper;
import cn.appsys.pojo.AppInfo;
import cn.appsys.pojo.AppVersion;
import cn.appsys.service.devuser.AppInfoService;

@Service
public class AppInfoServiceImpl implements AppInfoService {
	
	@Resource
	private AppInfoMapper Aim;
	@Resource
	private AppVersionMapper avm;
	
	@Transactional(readOnly = true)
	@Override
	public List<AppInfo> queryAppInfoList(String softwareName,int categoryLevel, int queryStatus,
			int queryFlatformId,int index,int pageSize) {
		return Aim.queryAppInfoList(softwareName,categoryLevel,
				queryStatus, queryFlatformId,index,pageSize);
	}

	@Override
	public int count(String softwareName, int categoryLevel, int queryStatus,
			int queryFlatformId) {
		return Aim.count(softwareName, categoryLevel,
				queryStatus, queryFlatformId);
	}

	@Override
	public int addVersion(AppInfo appinfo,AppVersion appversion) {
		int result= avm.addAppVersion(appversion);
		if (result>0) {
			appinfo.setVersionId(appversion.getId());
			appinfo.setStatus(1);
		}else{
			throw new RuntimeException();
		}
		return Aim.addVersion(appinfo);
	}

	@Override
	public int modifyVersion(AppInfo appinfo) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int modifyAppInfo(AppInfo appinfo) {
		return Aim.modifyAppInfo(appinfo);
	}

	@Override
	public AppInfo viewApp(int id) {
		return Aim.viewApp(id);
	}

	@Override
	public int deleteApp(int appId) {
		int resulta=0;
		List<AppVersion> appVersionById = avm.getAppVersionById(appId);
		if (appVersionById.size()>0) {//判断是否有版本信息
			for (AppVersion appVersion : appVersionById) {//循环删除
				if (appVersion.getApkLocPath()!=""||appVersion.getApkLocPath()!=null) {
					//删除apk文件操作
					File file=new File(appVersion.getApkLocPath());
					if (!file.delete()) {
						throw new RuntimeException();
					}
				}
				//删除版本信息
				resulta=avm.delAppVersion(appVersion.getId());
			}
		}
		if (resulta>0) {
			resulta=Aim.deleteApp(appId);
			if (resulta<0) {
				throw new RuntimeException();
			}
		}
		return resulta;
	}

	@Override
	public int checkAppName(String APKName) {
		return Aim.checkAppName(APKName);
	}

	@Override
	public int addAppInfo(AppInfo appinfo) {
		return Aim.addAppInfo(appinfo);
	}

	@Override
	public int delAppLogo(int id) {
		int result=0;
		AppInfo viewApp = this.viewApp(id);
		if (viewApp!=null) {
			File file=new File(viewApp.getLogoLocPath());
			if (file.delete()) {
				result=Aim.delAppLogo(id);
			}else{
				//异常回滚
				throw new RuntimeException();
			}
		}
		return result;
	}

	@Override
	public int modifyStatus(int id, String status) {
		// TODO Auto-generated method stub
		return Aim.modifyStatus(id, status);
	}

}
