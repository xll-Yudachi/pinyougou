package com.pinyougou.shop.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.entity.Result;
import com.pinyougou.pojo.TbSeller;
import com.pinyougou.sellergoods.service.SellerService;

/**
 * 
 * Title:sellerController.java
 * Description: 商家入驻Controller层
 * @author xll
 * @date 2019年2月6日 下午5:14:58
 * @version 1.0
 *
 */
@RestController
@RequestMapping("/seller")
public class sellerController {

	@Reference
	private SellerService sellerService;
	
	@RequestMapping("/add")
	public Result addSeller(@RequestBody TbSeller tbSeller) {
		
		//BCrypt密码加密
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		String password = passwordEncoder.encode(tbSeller.getPassword());
		tbSeller.setPassword(password);
		
		try {
			sellerService.add(tbSeller);
			return new Result(true, "商家入驻成功");
		} catch (Exception e) {
			return new Result(false, "商家入驻失败");
		}
	}
	
	@RequestMapping("/getLoginName")
	public Map getLoginName() {
		
		String loginName = SecurityContextHolder.getContext().getAuthentication().getName();
		Map map = new HashMap<>();
		map.put("loginName", loginName);
		return map;
	
	}
}
