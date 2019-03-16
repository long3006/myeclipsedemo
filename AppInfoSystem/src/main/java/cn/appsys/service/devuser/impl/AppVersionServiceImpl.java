package cn.appsys.service.devuser.impl;

import java.io.File;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.appsys.dao.devuser.AppInfoMapper;
import cn.appsys.dao.devuser.AppVersionMapper;
import cn.appsys.pojo.AppVersion;
import cn.appsys.service.devuser.AppVersionService;

@Service
public class AppVersionServiceImpl implements AppVersionService {
	
	@Resource
	private AppVersionMapper avm;
	@Resource
	private AppInfoMapper Aim;
	
	@Override
	public List<AppVersion> getAppVersionById(int id) {
		return avm.getAppVersionById(id);
	}
	
	@Override
	public int addAppVersion(AppVersion appversion) {
		return avm.addAppVersion(appversion);
	}
	
	@Override
	public int deldownLink(int id) {
		int result=0;
		AppVersion appVersion = this.getAppVersion(id);
		if (appVersion!=null) {
			File file=new File(appVersion.getApkLocPath());
			if (file.delete()) {
				result=avm.deldownLink(id);
			}else{
				throw new RuntimeException();
			}
		}
		return result;
	}
	
	@Override
	public int modifyAppVersion(AppVersion appversion) {
		return avm.modifyAppVersion(appversion);
	}

	@Override
	public int delAppVersion(int versionId) {
		return avm.delAppVersion(versionId);
	}

	@Override
	public AppVersion getAppVersion(int id) {
		return avm.getAppVersion(id);
	}

	

}
