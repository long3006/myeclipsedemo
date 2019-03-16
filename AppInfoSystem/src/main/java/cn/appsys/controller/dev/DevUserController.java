package cn.appsys.controller.dev;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSONArray;

import cn.appsys.pojo.AppCategory;
import cn.appsys.pojo.AppInfo;
import cn.appsys.pojo.AppVersion;
import cn.appsys.pojo.DataDictionary;
import cn.appsys.pojo.DevUser;
import cn.appsys.service.common.AppCategoryService;
import cn.appsys.service.common.DataDictionaryService;
import cn.appsys.service.devuser.AppInfoService;
import cn.appsys.service.devuser.AppVersionService;
import cn.appsys.service.devuser.DevUserService;
import cn.appsys.tools.Constants;
import cn.appsys.tools.Page;

@Controller
@RequestMapping("/dev")
public class DevUserController {
	private Logger logger=Logger.getLogger(DevUserController.class);
	
	@Resource
	private DevUserService devUS;
	@Resource
	private AppInfoService appS;
	@Resource
	private DataDictionaryService dds;
	@Resource
	private AppCategoryService acs;
	@Resource
	private AppVersionService avs;
	
	@RequestMapping(value="/devLogin")
	public String devLogin(){
		return "devlogin";
	}
	
	//登录验证
	@RequestMapping(value="/checkLogin",method=RequestMethod.POST)
	public String getLogin(@RequestParam("devCode")String devCode,
							@RequestParam("devPassword")String devPassword,
							HttpServletRequest request){
		//调用service方法，进行用户匹配
		DevUser duser = devUS.devLogin(devCode,devPassword);
		if (duser!=null) {//登录成功
			//页面跳转(主页面)
			request.getSession().setAttribute(Constants.DEVUDER_SESSION,duser);
			return "developer/main";
		}
		//页面跳转(登录页面)
		request.setAttribute("error", "用户名或密码不正确");
		return "devlogin";
	}
	
	//注销
	@RequestMapping(value="/loginout")
	public String toLoginOut(HttpSession session){
		session.invalidate();
		return "devlogin";
	}
	
	//跳转到app审核页面
	@RequestMapping(value="/appList")
	public String appList(@RequestParam(value="querySoftwareName",required=false)String querySoftwareName,
						  @RequestParam(value="queryCategoryLevel1",required=false)String queryCategoryLevel1,
					      @RequestParam(value="queryCategoryLevel2",required=false)String queryCategoryLevel2,
						  @RequestParam(value="queryCategoryLevel3",required=false)String queryCategoryLevel3,
						  @RequestParam(value="queryStatus",required=false)String queryStatus,
						  @RequestParam(value="queryFlatformId",required=false)String queryFlatformId,
						  @RequestParam(value="pageIndex",required=false)String pageIndex,
								HttpServletRequest request,
								HttpSession session,
								Model model){
		int cLevel1=0;
		int cLevel2=0;
		int cLevel3=0;
		int cate=-1;
		if (queryCategoryLevel1==null||queryCategoryLevel1=="") {
			cLevel1=-1;
		} else{
			cLevel1=Integer.parseInt(queryCategoryLevel1);
		}
		if(queryCategoryLevel2==null||queryCategoryLevel2==""){
			cLevel2=-1;
		}else{
			cLevel2=Integer.parseInt(queryCategoryLevel2);
		}
		if(queryCategoryLevel3==null||queryCategoryLevel3==""){
			cLevel3=-1;
		}else{
			cLevel3=Integer.parseInt(queryCategoryLevel3);
		}
		if(cLevel1>0){
			cate=cLevel1;
			if (cLevel2>0) {
				cate=cLevel2;
				if (cLevel3>0) {
					cate=cLevel3;
				}
			}
		}
		
		int status=0;
		if (queryStatus==null||queryStatus=="") {
			status=0;
		} else {
			status=Integer.parseInt(queryStatus);
		}
		int flatformId=0;
		if (queryFlatformId==null||queryFlatformId=="") {
			flatformId=0;
		} else {
			flatformId=Integer.parseInt(queryFlatformId);
		}
		
		int index=0;
		int pageSize=5;
		if (pageIndex==null||pageIndex=="") {
			index=1;
		}else{
			index=Integer.parseInt(pageIndex);
		}
		
		int total=appS.count(querySoftwareName, cate, status, flatformId);
		
		Page page = new Page();
		page.setPageSize(pageSize);
		page.setCurrentPageNo(index);
		page.setTotalCount(total);
		
		List<AppInfo> list = appS.queryAppInfoList(querySoftwareName,cate,status,flatformId,(index-1)*pageSize,pageSize);
		List<DataDictionary> appflatform = dds.getAppflatform();
		List<DataDictionary> appStatus = dds.getAppStatus();
		List<AppCategory> categorylevel1 = acs.getcategorylevel1();
		List<AppCategory> categorylevel2 = acs.getcategorylevel2(cLevel1);
		List<AppCategory> categorylevel3 = acs.getcategorylevel2(cLevel2);
		
		
		model.addAttribute("appInfoList", list);
		model.addAttribute("flatFormList", appflatform);
		model.addAttribute("statusList", appStatus);
		model.addAttribute("categoryLevel1List", categorylevel1);                                             
		model.addAttribute("queryStatus", queryStatus);
		model.addAttribute("queryFlatformId", queryFlatformId);
		model.addAttribute("querySoftwareName", querySoftwareName);
		model.addAttribute("queryCategoryLevel1", cLevel1);
		model.addAttribute("queryCategoryLevel2", cLevel2);
		model.addAttribute("queryCategoryLevel3", cLevel3);
		model.addAttribute("categoryLevel2List", categorylevel2);
		model.addAttribute("categoryLevel3List", categorylevel3);
		model.addAttribute("page", page);
		return "developer/appinfolist";
	}
	
	//动态显示分类
	@RequestMapping(value="/categorylevellist",method=RequestMethod.GET)
	@ResponseBody
	public Object categorylevelList(@RequestParam(value="pid",required=false)String pid){
		List<AppCategory> list =null;
		if (pid==null||pid=="") {
			list = acs.getcategorylevel1();
		}else{
			list =acs.getcategorylevel2(Integer.parseInt(pid));
		}
		return list;
	}
	
	//跳转新增版本页面
	@RequestMapping(value="/toaddappversion")
	public String appversionadd(@RequestParam("id")String id,Model model){
		
		List<AppVersion> historyVersion = avs.getAppVersionById(Integer.parseInt(id));
		model.addAttribute("appversionId", id);
		model.addAttribute("appVersionList", historyVersion);
		return "developer/appversionadd";
	}
	//新增版本操作
	@RequestMapping(value="/addappversion.html",method=RequestMethod.POST)
	public String addAppVersion(AppVersion appversion,
								HttpServletRequest request,
								HttpSession session,
								@RequestParam(value="a_downloadLink",required=false) MultipartFile[] attachs){
		String companyLicPicPath=null;
		String orgCodePicPath=null;
		String errorInfo=null;
		String oldFileName="";
		boolean flag=true;
		String Locpath=request.getSession().getServletContext().getRealPath("statics"+File.separator+"uploadfiles");
		logger.info("uploadFile path===========>"+Locpath);
		for (int i = 0; i < attachs.length; i++) {
			MultipartFile attach = attachs[i];
			//判断文件是否为空
			if (!attach.isEmpty()) {
				if (i==0) {
					errorInfo="uploadFileError";//第一个文件的错误提示
				} else if (i==1){
					errorInfo="uploadWpError";//第二个文件的错误提示
				}
				//定义上传目标路径
				oldFileName=attach.getOriginalFilename();//原文件名
				logger.info("uploadFile oldFileName===========>"+oldFileName);
				String prefix=FilenameUtils.getExtension(oldFileName);//原文件后缀
				logger.debug("uploadFile prefix===========>"+prefix);
				int filesize=1024*1024*100;
				logger.debug("uploadFile filesize===========>"+attach.getSize());
				if (attach.getSize()>filesize) {//上传大小不得超过100MB
					request.setAttribute(errorInfo,"*上传大小不得超过100MB");
					flag=false;
				}else if(prefix.equalsIgnoreCase("apk")){//上传文件格式不正确
					File targetFile=new File(Locpath,oldFileName);
					if (!targetFile.exists()) {
						targetFile.mkdirs();//自动创建文件夹
					}
					//保存
					try {
						//把MultipartFile中文件流的数据输出至目标文件中
						attach.transferTo(targetFile);
					} catch (Exception e) {
						e.printStackTrace();
						request.setAttribute(errorInfo, "*上传失败");
						flag=false;
					}
					if (i==0) {
						companyLicPicPath=Locpath+File.separator+oldFileName;
					} else if (i==1){
						orgCodePicPath=Locpath+File.separator+oldFileName;
					}
					logger.debug("idPicPath========"+companyLicPicPath);
					logger.debug("workPicPath========"+orgCodePicPath);
				}else{
					request.setAttribute(errorInfo, "*上传文件格式不正确");
					flag=false;
				}
			}else{
				flag=false;
			}
		}
		
		if (flag) {
			DevUser duser=(DevUser)session.getAttribute(Constants.DEVUDER_SESSION);
			appversion.setCreatedBy(duser.getId());
			appversion.setDownloadLink("/AppInfoSystem/"+"statics/"+"uploadfiles/"+oldFileName);
			appversion.setApkLocPath(companyLicPicPath);//地址
			appversion.setApkFileName(oldFileName);//文件名
			appversion.setCreationDate(new Date());//创建时间
			appversion.setPublishStatus(3);//待发布
			//新增（两张表操作，主键回填无事务）
			AppInfo viewApp = appS.viewApp(appversion.getAppId());
			int result=0;
			result=appS.addVersion(viewApp, appversion);
			if (result>0) {
				return "redirect:/dev/appList";
			}
		}
		return "developer/appversionadd";
	}
	
	//跳转到版本修改页面
	@RequestMapping(value="/tomodifyAppVersion")
	public String tomodifyAppVersion(
						@RequestParam(value="vid",required=false)String vid,
						@RequestParam(value="aid",required=false)String aid,
						Model model){
		AppVersion appVersion = avs.getAppVersion(Integer.parseInt(vid));
		List<AppVersion> historyVersion = avs.getAppVersionById(Integer.parseInt(aid));
		model.addAttribute("appVersionList", historyVersion);
		model.addAttribute("appVersion", appVersion);
		return "developer/appversionmodify";
	}
	
	//删除apk文件
	//修改APP信息删除图片
	@RequestMapping(value="/delfile")
	@ResponseBody
	public Object delApkfile(@RequestParam(value="id",required=false)String id,
			@RequestParam(value="flag",required=false)String flag){
		Map<String, Object> hashMap = new HashMap<String,Object>();
		int vid=Integer.parseInt(id);
		int delresult=0;
		if ("apk".equals(flag)) {
			delresult=avs.deldownLink(vid);
		} else {
			delresult=appS.delAppLogo(vid);
		}
		if (delresult>0) {
			hashMap.put("result", "success");
		} else {
			hashMap.put("result", "failed");
		}
		return JSONArray.toJSONString(hashMap);
	}
	
	//修改版本信息
	@RequestMapping(value="/modifyappversion",method=RequestMethod.POST)
	public String modifyAppVersion(AppVersion appversion,
			HttpServletRequest request,
			HttpSession session,
			@RequestParam(value="attach",required=false) MultipartFile[] attachs){
		String companyLicPicPath=null;
		String orgCodePicPath=null;
		String errorInfo=null;
		String oldFileName="";
		boolean flag=true;
		String Locpath=request.getSession().getServletContext().getRealPath("statics"+File.separator+"uploadfiles");
		logger.info("uploadFile path===========>"+Locpath);
		for (int i = 0; i < attachs.length; i++) {
			MultipartFile attach = attachs[i];
			//判断文件是否为空
			if (!attach.isEmpty()) {
				if (i==0) {
					errorInfo="uploadFileError";//第一个文件的错误提示
				} else if (i==1){
					errorInfo="uploadWpError";//第二个文件的错误提示
				}
				//定义上传目标路径
				oldFileName=attach.getOriginalFilename();//原文件名
				logger.info("uploadFile oldFileName===========>"+oldFileName);
				String prefix=FilenameUtils.getExtension(oldFileName);//原文件后缀
				logger.debug("uploadFile prefix===========>"+prefix);
				int filesize=1024*1024*100;
				logger.debug("uploadFile filesize===========>"+attach.getSize());
				if (attach.getSize()>filesize) {//上传大小不得超过100MB
					request.setAttribute(errorInfo,"*上传大小不得超过100MB");
					flag=false;
					return "developer/appversionmodify";
				}else if(prefix.equalsIgnoreCase("apk")){//上传文件格式不正确
					File targetFile=new File(Locpath,oldFileName);
					if (!targetFile.exists()) {
						targetFile.mkdirs();//自动创建文件夹
					}
					//保存
					try {
						//把MultipartFile中文件流的数据输出至目标文件中
						attach.transferTo(targetFile);
					} catch (Exception e) {
						e.printStackTrace();
						request.setAttribute(errorInfo, "*上传失败");
						flag=false;
						return "developer/appversionmodify";
					}
					if (i==0) {
						companyLicPicPath=Locpath+File.separator+oldFileName;
					} else if (i==1){
						orgCodePicPath=Locpath+File.separator+oldFileName;
					}
					logger.debug("idPicPath========"+companyLicPicPath);
					logger.debug("workPicPath========"+orgCodePicPath);
				}else{
					request.setAttribute(errorInfo, "*上传文件格式不正确");
					flag=false;
					return "developer/appversionmodify";
				}
			}else{
				flag=false;
			}
		}
			//判断上传
			if (flag) {
				appversion.setDownloadLink("/AppInfoSystem/"+"statics/"+"uploadfiles/"+oldFileName);//下载地址
				appversion.setApkLocPath(companyLicPicPath);//地址
				appversion.setApkFileName(oldFileName);//文件名
			}
			
			
			DevUser duser=(DevUser)session.getAttribute(Constants.DEVUDER_SESSION);
			appversion.setModifyBy(duser.getId());//修改人
			appversion.setModifyDate(new Date());//修改时间
			appversion.setPublishStatus(3);//待发布
			//修改
			int result=0;
			result=avs.modifyAppVersion(appversion);
			if (result>0) {
				return "redirect:/dev/appList";
			}
		
		return "developer/appversionmodify";/*数据怎么回显*/
	}
	
	
	//跳转到新增页面
	@RequestMapping(value="/toappinfoadd")
	public String toappinfoadd(){
		return "developer/appinfoadd";
	}
	
	//动态加载所属平台列表
	@RequestMapping(value="/datadictionarylist")
	@ResponseBody
	public Object getDataDictionarylist(Model model){
		List<DataDictionary> appflatform = dds.getAppflatform();
		return JSONArray.toJSONString(appflatform);
	}
	
	//ajax后台验证--APKName是否已存在
	@RequestMapping(value="/apkexist")
	@ResponseBody
	public Object getapkexist(@RequestParam(value="APKName",required=false)String APKName){
		Map<String, Object> hashMap = new HashMap<String,Object>();
		if (APKName==null||APKName=="") {
			hashMap.put("APKName", "empty");
		}else{
			if (appS.checkAppName(APKName)>0) {
				hashMap.put("APKName", "exist");
			}else{
				hashMap.put("APKName", "noexist");
			}
		}
		return JSONArray.toJSONString(hashMap);
	}
	
	//新增APP基础信息
	@RequestMapping(value="/appinfoadd",method=RequestMethod.POST)
	public String getappinfoadd(AppInfo appInfo,
			HttpServletRequest request,
			HttpSession session,
			@RequestParam(value="a_logoPicPath",required=false) MultipartFile attach){
		String companyLicPicPath=null;
		String errorInfo=null;
		String oldFileName="";
		boolean flag=true;
		//获取上传文件地址
		String Locpath=request.getSession().getServletContext().getRealPath("statics"+File.separator+"uploadfiles");
		logger.info("uploadFile path===========>"+Locpath);
		
		//判断文件是否为空
		if (!attach.isEmpty()) {
			errorInfo="uploadFileError";//第一个文件的错误提示
			//定义上传目标路径
			oldFileName=attach.getOriginalFilename();//原文件名
			logger.info("uploadFile oldFileName===========>"+oldFileName);
			String prefix=FilenameUtils.getExtension(oldFileName);//原文件后缀
			logger.debug("uploadFile prefix===========>"+prefix);
			int filesize=1024*1024*100;
			logger.debug("uploadFile filesize===========>"+attach.getSize());
			if (attach.getSize()>filesize) {//上传大小不得超过100MB
				request.setAttribute(errorInfo,"*上传大小不得超过100MB");
				flag=false;
			}else if(prefix.equalsIgnoreCase("jpg")
					||prefix.equalsIgnoreCase("png")
					||prefix.equalsIgnoreCase("jpeg")
					||prefix.equalsIgnoreCase("pneg")){//上传图片格式不正确
				File targetFile=new File(Locpath,oldFileName);
				if (!targetFile.exists()) {
					targetFile.mkdirs();//自动创建文件夹
				}
				//保存
				try {
					//把MultipartFile中文件流的数据输出至目标文件中
					attach.transferTo(targetFile);
				} catch (Exception e) {
					e.printStackTrace();
					request.setAttribute(errorInfo, "*上传失败");
					flag=false;
				}
				//上传文件存放地址
				companyLicPicPath=Locpath+File.separator+oldFileName;
				logger.debug("idPicPath========"+companyLicPicPath);
			}else{
				request.setAttribute(errorInfo, "*上传图片格式不正确");
				flag=false;
			}
		}
		if (flag) {
			DevUser duser=(DevUser)session.getAttribute(Constants.DEVUDER_SESSION);
			appInfo.setCreatedBy(duser.getId());
			appInfo.setCreationDate(new Date());//创建时间
			appInfo.setDevId(duser.getId());
			appInfo.setLogoPicPath("/AppInfoSystem/"+"statics/"+"uploadfiles/"+oldFileName);
			appInfo.setLogoLocPath(companyLicPicPath);//地址
			appInfo.setFlatformId(3);//待发布
			//新增（两张表操作，主键回填无事务）
			int result=0;
			result=appS.addAppInfo(appInfo);
			if (result>0) {
				return "redirect:/dev/appList";
			}
		}
		return "developer/appinfoadd";
	}
	
	//跳转修改APP基础信息
	@RequestMapping(value="/tomodifyAppInfo")
	public String tomodifyAppInfo(@RequestParam(value="id")String id,Model model){
		AppInfo viewApp = appS.viewApp(Integer.parseInt(id));
		model.addAttribute("appInfo", viewApp);
		return "developer/appinfomodify";
	}
	
	//修改APP基础信息
	@RequestMapping(value="/modifyAppInfo",method=RequestMethod.POST)
	public String getmodifyAppInfo(AppInfo appInfo,
			HttpServletRequest request,
			HttpSession session,
			@RequestParam(value="attach",required=false) MultipartFile attach){
		String companyLicPicPath=null;
		String errorInfo=null;
		String oldFileName="";
		boolean flag=true;
		//获取上传文件地址
		String Locpath=request.getSession().getServletContext().getRealPath("statics"+File.separator+"uploadfiles");
		logger.info("uploadFile path===========>"+Locpath);
		
		//判断文件是否为空
		if (!attach.isEmpty()) {
			errorInfo="uploadFileError";//第一个文件的错误提示
			//定义上传目标路径
			oldFileName=attach.getOriginalFilename();//原文件名
			logger.info("uploadFile oldFileName===========>"+oldFileName);
			String prefix=FilenameUtils.getExtension(oldFileName);//原文件后缀
			logger.debug("uploadFile prefix===========>"+prefix);
			int filesize=1024*1024*100;
			logger.debug("uploadFile filesize===========>"+attach.getSize());
			if (attach.getSize()>filesize) {//上传大小不得超过100MB
				request.setAttribute(errorInfo,"*上传大小不得超过100MB");
				flag=false;
				return "developer/appinfomodify";
			}else if(prefix.equalsIgnoreCase("jpg")
					||prefix.equalsIgnoreCase("png")
					||prefix.equalsIgnoreCase("jpeg")
					||prefix.equalsIgnoreCase("pneg")){//上传图片格式不正确
				File targetFile=new File(Locpath,oldFileName);
				if (!targetFile.exists()) {
					targetFile.mkdirs();//自动创建文件夹
				}
				//保存
				try {
					//把MultipartFile中文件流的数据输出至目标文件中
					attach.transferTo(targetFile);
				} catch (Exception e) {
					e.printStackTrace();
					request.setAttribute(errorInfo, "*上传失败");
					flag=false;
					return "developer/appinfomodify";
				}
				//上传文件存放地址
				companyLicPicPath=Locpath+File.separator+oldFileName;
				logger.debug("idPicPath========"+companyLicPicPath);
			}else{
				request.setAttribute(errorInfo, "*上传图片格式不正确");
				flag=false;
				return "developer/appinfomodify";
			}
		}else{
			flag=false;
		}
		//判断是否上传文件
		if (flag) {
			appInfo.setLogoPicPath("/AppInfoSystem/"+"statics/"+"uploadfiles/"+oldFileName);
			appInfo.setLogoLocPath(companyLicPicPath);//地址
		}
		
		DevUser duser=(DevUser)session.getAttribute(Constants.DEVUDER_SESSION);
		appInfo.setModifyBy(duser.getId());
		appInfo.setModifyDate(new Date());//修改时间
		/*SimpleDateFormat st=new SimpleDateFormat("yyyy-MM-dd");
		Object date=st.format(new Date());
		appInfo.setUpdateDate((Date)date);*/
		appInfo.setDevId(duser.getId());
		if (appInfo.getStatus()==null||appInfo.getStatus()==0) {
			appInfo.setStatus(3);//审核未通过
		}
		//新增
		int result=0;
		result=appS.modifyAppInfo(appInfo);
		if (result>0) {
			return "redirect:/dev/appList";
		}
		return "developer/appinfomodify";
	}
	
	//查看APP信息
	@RequestMapping(value="/toappview/{id}")
	public String toappview(@PathVariable String id,Model model){
		AppInfo viewApp = appS.viewApp(Integer.parseInt(id));
		List<AppVersion> historyVersion = avs.getAppVersionById(Integer.parseInt(id));
		model.addAttribute("appVersionList", historyVersion);
		model.addAttribute("appInfo", viewApp);
		return "developer/appinfoview";
	}
	
	//删除APP
	@RequestMapping(value="/delapp")
	@ResponseBody
	public Object getDelapp(@RequestParam(value="id")String id){
		Map<String, Object> hashMap = new HashMap<String,Object>();
		AppInfo viewApp = appS.viewApp(Integer.parseInt(id));
		if (viewApp==null) {
			hashMap.put("delResult", "notexist");
		}else{
			int result=0;
			result=appS.deleteApp(Integer.parseInt(id));
			if (result>0) {
				hashMap.put("delResult", "true");
			}else{
				hashMap.put("delResult", "false");
			}
		}
		return JSONArray.toJSONString(hashMap);
	}
	
	
	//上下架操作
	@RequestMapping(value="{id}/sale")
	@ResponseBody
	public Object getSale(@PathVariable String id){
		Map<String, Object> hashMap = new HashMap<String,Object>();
		if (id==null||id=="") {
			hashMap.put("errorCode", "param000001");
		}else{
			try {
				AppInfo viewApp = appS.viewApp(Integer.parseInt(id));
				int result=0;
				if (viewApp.getStatus()==4) {//已上架
					result=appS.modifyStatus(Integer.parseInt(id), "5");
				}else if(viewApp.getStatus()==5){//已下架
					result=appS.modifyStatus(Integer.parseInt(id), "4");
				}
				if (result>0) {
					hashMap.put("errorCode", "0");
					hashMap.put("resultMsg", "success");
				}else{
					hashMap.put("resultMsg", "failed");
				}
			} catch (Exception e) {
				e.printStackTrace();
				hashMap.put("errorCode", "exception000001");
			}
		}
		return JSONArray.toJSONString(hashMap);
	}
}
