package com.pinyougou.entity;

import java.io.Serializable;
import java.util.List;

/**
 * 
 * Title:PageResult.java
 * Description:分页结果类
 * @author xll
 * @date 2019年1月26日 下午8:06:49
 * @version 1.0
 *
 */
public class PageResult implements Serializable{

	private long total;//总记录数
	private List rows;//当前页记录
	public long getTotal() {
		return total;
	}
	public void setTotal(long total) {
		this.total = total;
	}
	public List getRows() {
		return rows;
	}
	public void setRows(List rows) {
		this.rows = rows;
	}
	public PageResult(long total, List rows) {
		this.total = total;
		this.rows = rows;
	}
	
}
