package cn.appsys.test;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import jxl.Workbook;
import jxl.format.Colour;
import jxl.format.UnderlineStyle;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import cn.appsys.dao.devuser.AppInfoMapper;
import cn.appsys.pojo.AppInfo;

public class ExcelTest {

	@Test
	public void test() throws IOException, RowsExceededException, WriteException {
		ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext-mybatis.xml");
		AppInfoMapper bean = context.getBean(AppInfoMapper.class);
		List<AppInfo> queryAppInfoList = bean.queryAppInfoList("",-1,0,0, 1, 1000);
		//获取输出流 
	    OutputStream os = new FileOutputStream("D:/abc.xls");
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
	    for (int i = 0; i < queryAppInfoList.size(); i++) {
	    	AppInfo appInfo = queryAppInfoList.get(i);
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
	}

}
