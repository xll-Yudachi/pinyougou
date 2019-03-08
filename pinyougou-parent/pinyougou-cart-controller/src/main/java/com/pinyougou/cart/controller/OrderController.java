package com.pinyougou.cart.controller;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.entity.Result;
import com.pinyougou.order.service.OrderService;
import com.pinyougou.pojo.TbOrder;

@RestController
@RequestMapping("/order")
public class OrderController {

	@Reference
	private OrderService orderService;
	
	/**
	 * Title:add
	 * Description: 添加订单
	 * @param order
	 * @return
	 */
	@RequestMapping("/add")
	public Result add(@RequestBody TbOrder order) {
		
		//获取当前登录人账号
		String userName = SecurityContextHolder.getContext().getAuthentication().getName();
		order.setUserId(userName);
		//订单来源 PC
		order.setSourceType("2");
		
		try {
			orderService.add(order);
			return new Result(true, "订单添加成功");
		} catch (Exception e) {
			return new Result(false, "订单添加失败");
		}
		
	}
}
