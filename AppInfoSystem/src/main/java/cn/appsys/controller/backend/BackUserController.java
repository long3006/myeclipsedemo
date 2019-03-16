package cn.appsys.controller.backend;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import jxl.Workbook;
import jxl.format.UnderlineStyle;
import jxl.write.Colour;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.appsys.pojo.AppCategory;
import cn.appsys.pojo.AppInfo;
import cn.appsys.pojo.AppVersion;
import cn.appsys.pojo.BackendUser;
import cn.appsys.pojo.DataDictionary;
import cn.appsys.service.backenduser.BackendUserService;
import cn.appsys.service.common.AppCategoryService;
import cn.appsys.service.common.DataDictionaryService;
import cn.appsys.service.devuser.AppInfoService;
import cn.appsys.service.devuser.AppVersionService;
import cn.appsys.tools.Constants;
import cn.appsys.tools.Page;

@Controller
@RequestMapping(value="/backuser")
public class BackUserController {
	@Resource
	private BackendUserService bckUser;
	@Resource
	private AppInfoService appS;
	@Resource
	private DataDictionaryService dds;
	@Resource
	private AppCategoryService acs;
	@Resource
	private AppVersionService avs;
	//实现跳转到登录页
	@RequestMapping(value="/backLogin")
	public String backLogin(){
		return "backendlogin";
	}
	
	//登录验证
	@RequestMapping(value="/checkLogin",method=RequestMethod.POST)
	public String getLogin(@RequestParam("userCode")String userCode,
							@RequestParam("userPassword")String userPassword,
							HttpServletRequest request){
		//调用service方法，进行用户匹配
		BackendUser backuser = bckUser.backLogin(userCode,userPassword);
		if (backuser!=null) {//登录成功
			//页面跳转(主页面)
			request.getSession().setAttribute(Constants.BAKCUSER_SESSION,backuser);
			return "backend/main";
		}
		//页面跳转(登录页面)
		request.setAttribute("error", "用户名或密码不正确");
		return "backendlogin";
	}
	
	//注销
	@RequestMapping(value="/loginout")
	public String toLoginOut(HttpSession session){
		session.invalidate();
		return "backendlogin";
	}
	
	
	//跳转到app审核页面
	/*@RequestMapping(value="/appList")
	public String appList(){
		return "backend/applist";
	}*/
	
	//跳转到app审核页面
	@RequestMapping(value="/appList")
	public String appList(@RequestParam(value="querySoftwareName",required=false)String querySoftwareName,
						  @RequestParam(value="queryCategoryLevel1",required=false)String queryCategoryLevel1,
					      @RequestParam(value="queryCategoryLevel2",required=false)String queryCategoryLevel2,
						  @RequestParam(value="queryCategoryLevel3",required=false)String queryCategoryLevel3,
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
		
		int status=0;//后台无APP状态查询
		
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
		List<AppInfo> list1 = appS.queryAppInfoList(querySoftwareName,cate,status,flatformId,1,10000);
		List<DataDictionary> appflatform = dds.getAppflatform();
		List<AppCategory> categorylevel1 = acs.getcategorylevel1();
		List<AppCategory> categorylevel2 = acs.getcategorylevel2(cLevel1);
		List<AppCategory> categorylevel3 = acs.getcategorylevel2(cLevel2);
		
		session.setAttribute("outdateListnow", list);//按条件当前数据（导出）
		session.setAttribute("outdateListall", list1);//按条件全部数据（导出）
		model.addAttribute("appInfoList", list);
		model.addAttribute("flatFormList", appflatform);
		model.addAttribute("categoryLevel1List", categorylevel1);                                             
		model.addAttribute("queryFlatformId", queryFlatformId);
		model.addAttribute("querySoftwareName", querySoftwareName);
		model.addAttribute("queryCategoryLevel1", cLevel1);
		model.addAttribute("queryCategoryLevel2", cLevel2);
		model.addAttribute("queryCategoryLevel3", cLevel3);
		model.addAttribute("categoryLevel2List", categorylevel2);
		model.addAttribute("categoryLevel3List", categorylevel3);
		model.addAttribute("page", page);
		return "backend/applist";
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
	
	
	//跳转到审核
	@RequestMapping(value="/check")
	public String toCheck(@RequestParam(value="aid")String aid,
						@RequestParam(value="vid")String vid,
						Model model){
		AppInfo viewApp = appS.viewApp(Integer.parseInt(aid));
		AppVersion appVersion = avs.getAppVersion(Integer.parseInt(vid));
		model.addAttribute("appInfo", viewApp);
		model.addAttribute("appVersion", appVersion);
		return "backend/appcheck";
	}
	
	//审核APP状态
	@RequestMapping(value="/modifyStatus")
	public String modifyStatus(AppInfo appInfo){
		int result=0;
		result=appS.modifyStatus(appInfo.getId(),appInfo.getStatus().toString());
		if (result>0) {
			return "redirect:/backuser/appList";
		}
		return "backend/appcheck";
	}
	
	//导出数据
	@RequestMapping(value="/toExcel")
	public void toExcel(HttpSession session,
			  HttpServletResponse response,@RequestParam(value="opt")String opt) throws RowsExceededException, WriteException, IOException{
		//start getlist
		List<AppInfo> list =null;
		if ("now".equals(opt)) {
			list = (List<AppInfo>)session.getAttribute("outdateListnow");
		}else if("all".equals(opt)){
			list = (List<AppInfo>)session.getAttribute("outdateListall");
		}
		
		//end getlist
		
		//start导出操作
		
		//获取输出流 
        OutputStream os = response.getOutputStream();// 取得输出流
        response.reset();// 清空输出流
        response.setHeader("Content-disposition", "attachment; filename=allApplist.xls");// 设定输出文件头
        response.setContentType("application/msexcel");// 定义输出类型
		
	    // 建立excel文件
	    WritableWorkbook wbook = Workbook.createWorkbook(os); 
	    // sheet名称
	    WritableSheet wsheet = wbook.createSheet("sheet1", 0); 
	    // 设置excel标题
        WritableFont wfont = new WritableFont(WritableFont.ARIAL, 16, WritableFont.BOLD, false,
                                              UnderlineStyle.NO_UNDERLINE, Colour.BLACK);
        WritableCellFormat wcfFC = new WritableCellFormat(wfont);
        wcfFC.setBackground(Colour.AQUA);
        wfont = new jxl.write.WritableFont(WritableFont.ARIAL, 14, WritableFont.BOLD, false,
        UnderlineStyle.NO_UNDERLINE, Colour.BLACK);
        wcfFC = new WritableCellFormat(wfont);
        wsheet.addCell(new Label(0, 0, "APP列表信息 ",wcfFC));
        // 开始生成主体内容
	    wsheet.addCell(new Label(0, 1, "软件名称 "));
        wsheet.addCell(new Label(1, 1, "APK名称"));
        wsheet.addCell(new Label(2, 1, "软件大小(单位:M)"));
        wsheet.addCell(new Label(3, 1, "所属平台"));
        wsheet.addCell(new Label(4, 1, "所属分类(一级分类、二级分类、三级分类)"));
        wsheet.addCell(new Label(5, 1, "状态"));
        wsheet.addCell(new Label(6, 1, "下载次数"));
        wsheet.addCell(new Label(7, 1, "最新版本号"));
        for (int i = 0; i < list.size(); i++) {
	    	AppInfo appInfo = list.get(i);
	    	for (int j = 0; j < 8; j++) {
	    		wsheet.addCell(new Label(0, i+2, appInfo.getSoftwareName()));
		        wsheet.addCell(new Label(1, i+2, appInfo.getAPKName()));
		        wsheet.addCell(new Label(2, i+2, ""+appInfo.getSoftwareSize()));
		        wsheet.addCell(new Label(3, i+2, appInfo.getFlatformName()));
		        wsheet.addCell(new Label(4, i+2, appInfo.getCategoryLevel1Name()+appInfo.getCategoryLevel2Name()+appInfo.getCategoryLevel3Name()));
		        wsheet.addCell(new Label(5, i+2, appInfo.getStatusName()));
		        wsheet.addCell(new Label(6, i+2, ""+appInfo.getDownloads()));
		        wsheet.addCell(new Label(7, i+2, appInfo.getVersionNo()));
			}
		}
        
        // 主体内容生成结束
        wbook.write(); // 写入文件
        wbook.close();
        os.close(); // 关闭流
        
        //end 导出结束
	}
}
