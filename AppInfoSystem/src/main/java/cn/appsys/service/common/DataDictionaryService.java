package cn.appsys.service.common;

import java.util.List;

import cn.appsys.pojo.DataDictionary;

public interface DataDictionaryService {
	public List<DataDictionary> getAppStatus();//app状态列表
	public List<DataDictionary> getAppflatform();//所属平台
}
