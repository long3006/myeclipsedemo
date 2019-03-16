package cn.appsys.service.common.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.appsys.dao.common.DataDictionaryMapper;
import cn.appsys.pojo.DataDictionary;
import cn.appsys.service.common.DataDictionaryService;

@Service
public class DataDictionaryServiceImpl implements DataDictionaryService {
	
	@Resource
	private DataDictionaryMapper ddm;
	
	@Override
	public List<DataDictionary> getAppStatus() {
		return ddm.getAppStatus();
	}

	@Override
	public List<DataDictionary> getAppflatform() {
		return ddm.getAppflatform();
	}

}
