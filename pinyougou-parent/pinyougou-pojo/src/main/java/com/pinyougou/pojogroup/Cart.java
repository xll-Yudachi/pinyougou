package com.pinyougou.pojogroup;

import java.io.Serializable;
import java.util.List;

import com.pinyougou.pojo.TbOrderItem;

/**
 * Title:Cart.java
 * Description: 购物车实体类
 * @author xll
 * @date 2019年3月4日 下午1:24:46
 * @version 1.0
 */
public class Cart implements Serializable{

	//商家ID
	private String sellerId;
	//商家名称
	private String sellerName;
	//购物车明细集合
	private List<TbOrderItem> orderItemList;
	
	public String getSellerId() {
		return sellerId;
	}
	public void setSellerId(String sellerId) {
		this.sellerId = sellerId;
	}
	public String getSellerName() {
		return sellerName;
	}
	public void setSellerName(String sellerName) {
		this.sellerName = sellerName;
	}
	public List<TbOrderItem> getOrderItemList() {
		return orderItemList;
	}
	public void setOrderItemList(List<TbOrderItem> orderItemList) {
		this.orderItemList = orderItemList;
	}
	
	
}
