package cn.appsys.dao.common;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.appsys.pojo.AppCategory;

public interface AppCategoryMapper {
	/**
	 * 一级分类
	 * @return
	 */
	public List<AppCategory> getcategorylevel1();
	/**
	 * 二级分类
	 * 三级分类
	 * @param parentId
	 * @return
	 */
	public List<AppCategory> getcategorylevel2(@Param("parentId")int parentId);
}
