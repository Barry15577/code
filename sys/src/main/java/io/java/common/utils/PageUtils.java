package io.java.common.utils;

import com.baomidou.mybatisplus.core.metadata.IPage;

import java.io.Serializable;
import java.util.List;

/**
 * Paging Tools
 *
 * @author Jiasi Lu
 */
public class PageUtils implements Serializable {
	private static final long serialVersionUID = 1L;
	/**
	 * Total records
	 */
	private int totalCount;
	/**
	 * Number of records per page
	 */
	private int pageSize;
	/**
	 * Total Pages
	 */
	private int totalPage;
	/**
	 * Current Page
	 */
	private int currPage;
	/**
	 * List Data
	 */
	private List<?> list;
	
	/**
	 * @param list        list data
	 * @param totalCount  total count
	 * @param pageSize    Number of records per page
	 * @param currPage    Current Page
	 */
	public PageUtils(List<?> list, int totalCount, int pageSize, int currPage) {
		this.list = list;
		this.totalCount = totalCount;
		this.pageSize = pageSize;
		this.currPage = currPage;
		this.totalPage = (int)Math.ceil((double)totalCount/pageSize);
	}

	/**
	 * Pagination
	 */
	public PageUtils(IPage<?> page) {
		this.list = page.getRecords();
		this.totalCount = (int)page.getTotal();
		this.pageSize = (int)page.getSize();
		this.currPage = (int)page.getCurrent();
		this.totalPage = (int)page.getPages();
	}

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getTotalPage() {
		return totalPage;
	}

	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage;
	}

	public int getCurrPage() {
		return currPage;
	}

	public void setCurrPage(int currPage) {
		this.currPage = currPage;
	}

	public List<?> getList() {
		return list;
	}

	public void setList(List<?> list) {
		this.list = list;
	}
	
}
