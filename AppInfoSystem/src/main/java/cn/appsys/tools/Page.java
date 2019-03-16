package cn.appsys.tools;

public class Page {
	private int totalPageCount=0;//总页数
	private int pageSize=0;//页面大小，即每页显示记录
	private int totalCount=0;//记录总数
	private int currentPageNo=1;//当前页码
	public int getTotalPageCount() {
		return totalPageCount;
	}
	public void setTotalPageCount(int totalPageCount) {
		this.totalPageCount = totalPageCount;
	}
	public int getPageSize() {
		return pageSize;
	}
	public void setPageSize(int pageSize) {
		if(pageSize>0)
		this.pageSize = pageSize;
	}
	public int getTotalCount() {
		return totalCount;
	}
	public void setTotalCount(int totalCount) {
		if(totalCount>0){
			this.totalCount = totalCount;
			//计算总页数
			totalPageCount=this.totalCount%pageSize==0?
					(this.totalCount/pageSize):(this.totalCount/pageSize+1);
		}
		
	}
	public int getCurrentPageNo() {
		if(totalPageCount==0)
			return 0;
		return currentPageNo;
	}
	public void setCurrentPageNo(int currentPageNo) {
		if(currentPageNo>0)
		this.currentPageNo = currentPageNo;
	}
}
