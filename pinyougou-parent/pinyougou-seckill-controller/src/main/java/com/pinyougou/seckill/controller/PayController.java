package com.pinyougou.seckill.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.entity.Result;
import com.pinyougou.pay.service.WeixinPayService;
import com.pinyougou.pojo.TbPayLog;
import com.pinyougou.pojo.TbSeckillOrder;
import com.pinyougou.seckill.service.SeckillOrderService;

import util.IdWorker;

@RestController
@RequestMapping("/pay")
public class PayController {

	@Reference
	private WeixinPayService weixinPayService;
	@Reference
	private SeckillOrderService seckillOrderService;
	
	@RequestMapping("/createNative")
	public Map createNative() {
		//1.获取当前登录用户
		String userId = SecurityContextHolder.getContext().getAuthentication().getName();
		//2.从缓存中提取秒杀订单
		TbSeckillOrder seckillOrder = seckillOrderService.searchOrderFromRedisByUserId(userId);
		//3.调用微信支付接口
		if(seckillOrder!=null) {
			return weixinPayService.createNative(seckillOrder.getId()+"", (long)(seckillOrder.getMoney().doubleValue()*100) + "");
		}else {
			return new HashMap<>();
		}
	}
	
	@RequestMapping("/queryPayStatus")
	public Result queryPayStatus(String out_trade_no) {
		//获取当前登录用户
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		Result result = null;
		int count = 0;
		while(true) {
			//调用查询
			Map<String, String> map = weixinPayService.queryPayStatus(out_trade_no);
			if(map == null) {
				result = new Result(false, "支付发生错误");
				break;
			}
			if(map.get("trade_state").equals("SUCCESS")) {
				result = new Result(true, "支付成功");
				//保存订单
				seckillOrderService.saveOrderFromRedisToDb(username, Long.valueOf(out_trade_no), map.get("transaction_id"));
				break;
			}
			
			try {
				Thread.sleep(3000);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			count++;
			
			if(count >= 100) {
				result = new Result(false, "二维码超时");
				
				//关闭支付
				Map<String, String> payResult = weixinPayService.closePay(out_trade_no);
				if(payResult != null && "FAIL".equals(payResult.get("return_code"))) {
					if("ORDERPAID".equals(payResult.get("err_code"))) {
						result = new Result(true, "支付成功");
					}
				}
				
				//删除订单
				if(result.isSuccess() == false) {
					seckillOrderService.deleteOrderFromRedis(username, Long.valueOf(out_trade_no));
				}
				
				break;
			}
		}
		return result;
	}
}
