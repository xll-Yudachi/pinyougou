package com.pinyougou.shop.controller;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.entity.PageResult;
import com.pinyougou.entity.Result;
import com.pinyougou.pojo.TbGoods;
import com.pinyougou.pojogroup.Goods;
import com.pinyougou.sellergoods.service.GoodsService;

/**
 * 
 * Title:GoodsController.java
 * Description: 商家商品管理controller
 * @author xll
 * @date 2019年2月9日 下午4:01:54
 * @version 1.0
 *
 */
@RestController
@RequestMapping("/goods")
public class GoodsController {
	
	@Reference
	private GoodsService goodsService;
	
	/**
	 * Title:add
	 * Description:添加商品
	 * @param goods
	 * @return
	 */
	@RequestMapping("/add")
	public Result add(@RequestBody Goods goods) {
		 //获取商家ID
		 String sellerId = SecurityContextHolder.getContext().getAuthentication().getName();
		 goods.getGoods().setSellerId(sellerId);
		 
		 try {
			goodsService.add(goods);
			return new Result(true, "添加成功");
		} catch (Exception e) {
			return new Result(false, "添加失败");
		}
	}
	
	/**
	 * 
	 * Title:search
	 * Description: 分页条件查询
	 * @param goods
	 * @param page
	 * @param rows
	 * @return
	 */
	@RequestMapping("/search")
	public PageResult search(@RequestBody TbGoods goods, int page, int rows) {
		//获取商家ID
		String sellerId = SecurityContextHolder.getContext().getAuthentication().getName();
		//设置商家ID
		goods.setSellerId(sellerId);
		
		return goodsService.findPage(goods, page, rows);
	}
	
	/**
	 * Title:findOne
	 * Description:获取实体
	 * @param id
	 * @return
	 */
	@RequestMapping("/findOne")
	public Goods findOne(Long id) {
		return goodsService.findOne(id);
	}
	
	/**
	 * Title:update
	 * Description: 修改商品
	 * @param goods
	 * @return
	 */
	@RequestMapping("/update")
	public Result update(@RequestBody Goods goods) {
		//当前商家ID
		String sellerId = SecurityContextHolder.getContext().getAuthentication().getName();
		
		//判断商品是否是该商家的商品
		Goods temp_goods = goodsService.findOne(goods.getGoods().getId());
		
		if(!temp_goods.getGoods().getSellerId().equals(sellerId) || !goods.getGoods().getSellerId().equals(sellerId)) {
			return new Result(false, "非法操作");
		}
		
		try {
			goodsService.update(goods);
			return new Result(true, "修改成功");
		} catch (Exception e) {
			return new Result(false, "修改失败");
		}
	}
	
	/**
	 * Title:updateIsMarketable
	 * Description: 更新商品的上下架状态
	 * @param isMarketable
	 * @return
	 */
	@RequestMapping("/updateIsMarketable")
	public Result updateIsMarketable(Long id,String isMarketable) {
		try {
			goodsService.updateIsMarketable(id,isMarketable);
			if(isMarketable.equals("1")) {
				return new Result(true, "商品下架成功");
			}else {
				return new Result(true, "商品上架成功");
			}
		} catch (Exception e) {
			return new Result(false, "操作失败");
		}
	}
}
