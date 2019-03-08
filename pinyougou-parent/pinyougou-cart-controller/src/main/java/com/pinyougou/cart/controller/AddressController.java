package com.pinyougou.cart.controller;

import java.util.List;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.entity.Result;
import com.pinyougou.pojo.TbAddress;
import com.pinyougou.user.service.AddressService;

@RestController
@RequestMapping("/address")
public class AddressController {

	@Reference
	private AddressService addressService;
	
	@RequestMapping("/findListByLoginUser")
	public List<TbAddress> findListByLoginUser(){
		//获取用户登录名
		String userName = SecurityContextHolder.getContext().getAuthentication().getName();
		return addressService.findListByUserId(userName);
	}
	
	@RequestMapping("/addAddress")
	public Result addAddress(@RequestBody TbAddress address) {
		try {
			//获取用户登录名
			String userId = SecurityContextHolder.getContext().getAuthentication().getName();
			addressService.addAddress(address, userId);
			
			return new Result(true, "收货地址添加成功");
		} catch (Exception e) {
			return new Result(false, "收货地址添加失败");
		}
	}
}
