package com.pinyougou.manager.controller;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.content.service.ContentService;
import com.pinyougou.entity.PageResult;
import com.pinyougou.entity.Result;
import com.pinyougou.pojo.TbContent;

@RestController
@RequestMapping("/content")
public class ContentController {

	@Reference
	private ContentService contentService;
	
	@RequestMapping("/search")
	public PageResult search(@RequestBody TbContent content,int page,int rows) {
		return contentService.search(content, page, rows);
	}
	
	@RequestMapping("/add")
	public Result add(@RequestBody TbContent content) {
		try {
			contentService.add(content);
			return new Result(true, "添加成功");
		} catch (Exception e) {
			return new Result(false, "添加失败");
		}
	}
	
	@RequestMapping("/findOne")
	public TbContent findOne(Long id) {
		return contentService.findOne(id);
	}
	
	@RequestMapping("/delete")
	public Result delete(Long[] ids) {
		try {
			contentService.delete(ids);
			return new Result(true, "删除成功");
		} catch (Exception e) {
			return new Result(false, "删除失败");
		}
	}
	
	@RequestMapping("/update")
	public Result update(@RequestBody TbContent content) {
		try {
			contentService.update(content);
			return new Result(true, "修改成功");
		} catch (Exception e) {
			return new Result(false, "修改失败");
		}
	}
	
}
