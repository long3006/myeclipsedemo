package cn.appsys.test;

import static org.junit.Assert.*;

import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import cn.appsys.dao.common.AppCategoryMapper;
import cn.appsys.dao.common.DataDictionaryMapper;
import cn.appsys.dao.devuser.AppInfoMapper;
import cn.appsys.dao.devuser.AppVersionMapper;
import cn.appsys.pojo.AppCategory;
import cn.appsys.pojo.AppInfo;
import cn.appsys.pojo.AppVersion;
import cn.appsys.pojo.DataDictionary;
import cn.appsys.service.devuser.AppVersionService;

public class TestAppInfo {

	@Test
	public void test() {
		ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext-mybatis.xml");
		AppInfoMapper bean = context.getBean(AppInfoMapper.class);
		/*List<AppInfo> list=bean.queryAppInfoList("玩",-1,0,0);
		for (AppInfo appInfo : list) {
			System.out.println(appInfo);
		}
		System.out.println(list.size());*/
		System.out.println(bean.count("",-1,0,0));
		
	}
	
	@Test
	public void test1() {
		ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext-mybatis.xml");
		DataDictionaryMapper bean = context.getBean(DataDictionaryMapper.class);
		List<DataDictionary> list=bean.getAppflatform();
		List<DataDictionary> list1=bean.getAppStatus();
		for (DataDictionary dataDictionary : list) {
			System.out.println(dataDictionary);
		}
		for (DataDictionary dataDictionary : list1) {
			System.out.println(dataDictionary);
		}
	}
	
	@Test
	public void test2() {
		ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext-mybatis.xml");
		AppCategoryMapper bean = context.getBean(AppCategoryMapper.class);
		List<AppCategory> list = bean.getcategorylevel2(19);
		for (AppCategory appCategory : list) {
			System.out.println(appCategory);
		}
	}
	/*版本相关测试*/
	@Test
	public void test3() {
		ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext-mybatis.xml");
		AppInfoMapper ai = context.getBean(AppInfoMapper.class);
		AppVersionMapper av = context.getBean(AppVersionMapper.class);
		AppVersion appVersion = new AppVersion();
		appVersion.setAppId(54);
		appVersion.setVersionNo("v1.5.1");
		appVersion.setVersionInfo("优秀优秀");
		appVersion.setPublishStatus(3);
		appVersion.setDownloadLink("www.test.com");
		appVersion.setVersionSize(6.00);
		appVersion.setCreatedBy(1);
		appVersion.setCreationDate(new Date());
		appVersion.setApkLocPath("/test/table");
		appVersion.setAppName("test.apk");
		av.addAppVersion(appVersion);
		System.out.println(appVersion.getId());
	}
	
	@Test
	public void test4(){
		ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext-mybatis.xml");
		AppVersionService bean = context.getBean(AppVersionService.class);
		AppVersion appVersion = bean.getAppVersion(48);
		System.out.println(appVersion);
	}
	
	
	@Test
	public void test5(){
		
	}

}
