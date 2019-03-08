package com.pinyougou.search.controller;

import java.util.Map;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.search.service.ItemSearchService;

/**
 * Title:ItemSearchController.java
 * Description: 商品搜索控制层
 * @author xll
 * @date 2019年2月22日 下午5:42:59
 * @version 1.0
 *
 */
@RestController
@RequestMapping("/itemsearch")
public class ItemSearchController {

	@Reference
	private ItemSearchService itemSearchService;
	
	@RequestMapping("/search")
	public Map search(@RequestBody Map searchMap) {
		
		return itemSearchService.search(searchMap);
	
	}
}
