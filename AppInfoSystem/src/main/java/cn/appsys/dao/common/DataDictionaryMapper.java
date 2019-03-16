package cn.appsys.dao.common;

import java.util.List;

import cn.appsys.pojo.DataDictionary;

public interface DataDictionaryMapper {
	public List<DataDictionary> getAppStatus();//app状态列表
	public List<DataDictionary> getAppflatform();//所属平台
}
