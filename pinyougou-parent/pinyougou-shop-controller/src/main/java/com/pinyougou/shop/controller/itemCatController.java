package com.pinyougou.shop.controller;

import java.util.List;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.pojo.TbItemCat;
import com.pinyougou.sellergoods.service.ItemCatService;

/**
 * 
 * Title:itemCatController.java
 * Description: 商家商品分类Controller层
 * @author xll
 * @date 2019年2月11日 下午5:30:18
 * @version 1.0
 *
 */

@RestController
@RequestMapping("/itemCat")
public class itemCatController {

	@Reference
	private ItemCatService itemCatService;
	
	/**
	 * Title:findByParentId
	 * Description: 根据父级Id查询
	 * @param parentId
	 * @return
	 */
	@RequestMapping("/findByParentId")
	public List<TbItemCat> findByParentId(Long parentId){
		return itemCatService.findByParentId(parentId);
	
	}
	
	/**
	 * Title:findOne
	 * Description:根据Id查询
	 * @param id
	 * @return
	 */
	@RequestMapping("/findOne")
	public TbItemCat findOne(Long id) {
		return itemCatService.findOne(id);
	}
	
	/**
	 * Title:findAll
	 * Description: 查询所有商品分类
	 * @return
	 */
	@RequestMapping("/findAll")
	public List<TbItemCat> findAll(){
		return itemCatService.findAll();
	}
}
