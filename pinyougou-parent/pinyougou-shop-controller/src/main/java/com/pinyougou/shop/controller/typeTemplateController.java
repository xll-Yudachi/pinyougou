package com.pinyougou.shop.controller;

import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.pojo.TbTypeTemplate;
import com.pinyougou.sellergoods.service.TypeTemplateService;

/**
 * 
 * Title:typeTemplateController.java
 * Description: 运营商商品模板Controller层
 * @author xll
 * @date 2019年2月11日 下午9:13:49
 * @version 1.0
 *
 */
@RestController
@RequestMapping("/typeTemplate")
public class typeTemplateController {

	@Reference
	private TypeTemplateService typeTemplateService;
	
	@RequestMapping("/findOne")
	public TbTypeTemplate findOne(Long id) {
		return typeTemplateService.findOne(id);
	}
	
	@RequestMapping("/findSpecList")
	public List<Map> findSpecList(Long id){
		return typeTemplateService.findSpecList(id);
	}
	
}
