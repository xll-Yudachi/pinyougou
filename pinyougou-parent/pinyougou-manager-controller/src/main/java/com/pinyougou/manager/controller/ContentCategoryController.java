package com.pinyougou.manager.controller;

import java.util.List;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.content.service.ContentCategoryService;
import com.pinyougou.entity.PageResult;
import com.pinyougou.entity.Result;
import com.pinyougou.pojo.TbContentCategory;

@RestController
@RequestMapping("/contentCategory")
public class ContentCategoryController {

	@Reference
	private ContentCategoryService contentCategoryService;
	
	@RequestMapping("/search")
	public PageResult findPage(@RequestBody TbContentCategory contentCategory,int page,int rows) {
		return contentCategoryService.findPage(contentCategory,page, rows);
	}

	@RequestMapping("/add")
	public Result add(@RequestBody TbContentCategory contentCategory) {
		try {
			contentCategoryService.add(contentCategory);
			return new Result(true, "添加成功");
		} catch (Exception e) {
			return new Result(false, "添加失败");
		}
	}
	
	@RequestMapping("/delete")
	public Result delete(Long[] ids) {
		try {
			contentCategoryService.delete(ids);
			return new Result(true, "删除成功");
		} catch (Exception e) {
			return new Result(false, "删除失败");
		}
	}
	
	@RequestMapping("/findOne")
	public TbContentCategory findOne(Long id) {
		return contentCategoryService.findOne(id);
	}
	
	@RequestMapping("/update")
	public Result update(@RequestBody TbContentCategory contentCategory) {
		try {
			contentCategoryService.update(contentCategory);
			return new Result(true, "修改成功");
		} catch (Exception e) {
			return new Result(false, "修改失败");
		}
	}
	
	@RequestMapping("/findAll")
	public List<TbContentCategory> findAll(){
		
		return contentCategoryService.findAll();
	
	}
}
