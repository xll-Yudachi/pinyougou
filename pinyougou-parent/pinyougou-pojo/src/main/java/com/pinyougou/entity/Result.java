package com.pinyougou.entity;

import java.io.Serializable;

/**
 * 
 * Title:Result.java
 * Description:返回结果
 * @author xll
 * @date 2019年1月29日 下午3:58:23
 * @version 1.0
 *
 */
public class Result implements Serializable{
	
	private boolean success;	//是否成功
	private String message;	 //返回信息
	public boolean isSuccess() {
		return success;
	}
	public void setSuccess(boolean success) {
		this.success = success;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public Result(boolean success, String message) {
		this.success = success;
		this.message = message;
	}
	
}
