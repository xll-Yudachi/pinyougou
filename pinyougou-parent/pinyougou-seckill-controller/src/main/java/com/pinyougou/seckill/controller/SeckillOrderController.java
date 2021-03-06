package com.pinyougou.seckill.controller;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.entity.Result;
import com.pinyougou.seckill.service.SeckillOrderService;

@RestController
@RequestMapping("/seckillOrder")
public class SeckillOrderController {

	@Reference
	private SeckillOrderService seckillOrderService;
	
	@RequestMapping("/submitOrder")
	public Result submitOrder(Long seckillId) {
		//提取当前用户
		String userName = SecurityContextHolder.getContext().getAuthentication().getName();
		if("anonymousUser".equals(userName)) {
			return new Result(false, "当前用户未登录");
		}
		try {
			seckillOrderService.submitOrder(seckillId, userName);
			return new Result(true, "提交订单成功");
		}catch (RuntimeException e) {
			return new Result(false, e.getMessage());
		} catch (Exception e) {
			return new Result(false, "提交订单失败");
		}
	}
}
