package cn.appsys.service.common.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.appsys.dao.common.AppCategoryMapper;
import cn.appsys.pojo.AppCategory;
import cn.appsys.service.common.AppCategoryService;

@Service
public class AppCategoryServiceImpl implements AppCategoryService {
	
	@Resource
	private AppCategoryMapper acm;
	@Override
	public List<AppCategory> getcategorylevel1() {
		return acm.getcategorylevel1();
	}

	@Override
	public List<AppCategory> getcategorylevel2(int parentId) {
		return acm.getcategorylevel2(parentId);
	}

}
