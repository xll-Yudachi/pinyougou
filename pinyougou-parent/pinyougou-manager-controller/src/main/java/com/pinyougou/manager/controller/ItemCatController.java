package com.pinyougou.manager.controller;

import java.util.List;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.entity.Result;
import com.pinyougou.pojo.TbItemCat;
import com.pinyougou.sellergoods.service.ItemCatService;

/**
 * 
 * Title:ItemCatController.java
 * Description: 运营商商品分类Controller层
 * @author xll
 * @date 2019年2月8日 下午3:04:47
 * @version 1.0
 *
 */
@RestController
@RequestMapping("/itemCat")
public class ItemCatController {

	@Reference
	private ItemCatService itemCatService;
	
	/**
	 * Title:findByParentId
	 * Description: 根据父级id查询商品分类
	 * @param parentId
	 * @return
	 */
	@RequestMapping("/findByParentId")
	public List<TbItemCat> findByParentId(Long parentId){
		
		return itemCatService.findByParentId(parentId);
	
	}
	
	@RequestMapping("/add")
	public Result add(@RequestBody TbItemCat itemCat) {
		try {
			itemCatService.add(itemCat);
			return new Result(true, "添加成功");
		} catch (Exception e) {
			return new Result(false, "添加失败");
		}
	}
	
	@RequestMapping("/findOne")
	public TbItemCat findOne(Long id) {
		return itemCatService.findOne(id);
	}
	
	@RequestMapping("/update")
	public Result update(@RequestBody TbItemCat itemCat) {
		try {
			itemCatService.update(itemCat);
			return new Result(true, "修改成功");
		} catch (Exception e) {
			return new Result(false, "修改失败");
		}
	}
	
	@RequestMapping("/delete")
	public Result delete(Long[] ids) {
		try {
			boolean isDelete = itemCatService.delete(ids);
			if(isDelete) {
				return new Result(true, "删除成功");
			}else {
				return new Result(false, "请先删除下级菜单");
			}
			
		} catch (Exception e) {
			return new Result(false, "发生异常删除失败");
		}
	}
	
	@RequestMapping("/findAll")
	public List<TbItemCat> findAll(){
		return itemCatService.findAll();
	}
}
