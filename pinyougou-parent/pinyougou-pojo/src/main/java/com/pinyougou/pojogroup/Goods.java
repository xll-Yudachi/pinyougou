package com.pinyougou.pojogroup;

import java.io.Serializable;
import java.util.List;

import com.pinyougou.pojo.TbGoods;
import com.pinyougou.pojo.TbGoodsDesc;
import com.pinyougou.pojo.TbItem;

/**
 * 
 * Title:Goods.java
 * Description:商品组合实体类
 * @author xll
 * @date 2019年2月9日 下午3:47:30
 * @version 1.0
 *
 */
public class Goods implements Serializable{
	
	private TbGoods goods;	//商品基本信息   SPU
	private TbGoodsDesc goodsDesc;	//商品扩展信息
	private List<TbItem> itemList;	//SKU列表
	public TbGoods getGoods() {
		return goods;
	}
	public void setGoods(TbGoods goods) {
		this.goods = goods;
	}
	public TbGoodsDesc getGoodsDesc() {
		return goodsDesc;
	}
	public void setGoodsDesc(TbGoodsDesc goodsDesc) {
		this.goodsDesc = goodsDesc;
	}
	public List<TbItem> getItemList() {
		return itemList;
	}
	public void setItemList(List<TbItem> itemList) {
		this.itemList = itemList;
	}
}
