package com.pinyougou.manager.controller;
import java.util.List;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.entity.PageResult;
import com.pinyougou.entity.Result;
import com.pinyougou.pojo.TbSeller;
import com.pinyougou.sellergoods.service.SellerService;
 
/**
 * 
 * Title:SellerController.java
 * Description: 运营商 商家管理controller层
 * @author xll
 * @date 2019年2月6日 下午7:03:45
 * @version 1.0
 *
 */
@RestController
@RequestMapping("/seller")
public class SellerController {

	@Reference
	private SellerService sellerService;
	
	@RequestMapping("/search")
	public PageResult search(@RequestBody TbSeller seller, int page, int rows){
		return sellerService.findPage(seller, page, rows);		
	}
	
	@RequestMapping("/findOne")
	public TbSeller findOne(String id) {
		return sellerService.findOne(id);
	}
	
	@RequestMapping("/updateStatus")
	public Result updateStatus(String sellerId,String status) {
		try {
			sellerService.updateStatus(sellerId, status);
			return new Result(true, "状态修改成功");
		} catch (Exception e) {
			return new Result(false, "状态修改失败");
		}
	}
}
