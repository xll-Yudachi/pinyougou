package com.pinyougou.order.service.Impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.dubbo.config.annotation.Service;
import com.pinyougou.mapper.TbOrderItemMapper;
import com.pinyougou.mapper.TbOrderMapper;
import com.pinyougou.mapper.TbPayLogMapper;
import com.pinyougou.order.service.OrderService;
import com.pinyougou.pojo.TbOrder;
import com.pinyougou.pojo.TbOrderItem;
import com.pinyougou.pojo.TbPayLog;
import com.pinyougou.pojogroup.Cart;

import util.IdWorker;

/**
 * Title:OrderServiceImpl.java
 * Description: 订单服务实现类
 * @author xll
 * @date 2019年3月5日 下午8:27:56
 * @version 1.0
 *
 */
@Service(timeout=100000)
@Transactional
public class OrderServiceImpl implements OrderService{

	@Autowired
	private RedisTemplate redisTemplate;
	@Autowired
	private IdWorker idWorker;
	@Autowired
	private TbOrderMapper orderMapper;
	@Autowired
	private TbOrderItemMapper orderItemMapper;
	@Autowired
	private TbPayLogMapper payLogMapper;
	
	@Override
	public void add(TbOrder order) {
		
		//订单总金额
		double total_money = 0;
		//订单号集合
		List<String> orderIdList = new ArrayList<>();
		
 		//1.从redis中读取购物车列表
		List<Cart> cartList = (List<Cart>) redisTemplate.boundHashOps("cartList").get(order.getUserId());
		
		//2.循环购物车列表 添加订单
		for(Cart cart : cartList) {
			//填充订单对象
			TbOrder tbOrder = new TbOrder();
			//获取订单编号ID
			long orderId = idWorker.nextId();
			tbOrder.setOrderId(orderId);
			//支付类型
			tbOrder.setPaymentType(order.getPaymentType());
			//未付款
			tbOrder.setStatus("1");
			//下单时间
			tbOrder.setCreateTime(new Date());
			//更新时间
			tbOrder.setUpdateTime(new Date());
			//当前用户
			tbOrder.setUserId(order.getUserId());
			//收货人地址
			tbOrder.setReceiverAreaName(order.getReceiverAreaName());
			//收货人电话
			tbOrder.setReceiverMobile(order.getReceiverMobile());
			//收货人
			tbOrder.setReceiver(order.getReceiver());
			//订单来源
			tbOrder.setSourceType(order.getSourceType());
			//商家ID
			tbOrder.setSellerId(cart.getSellerId());
			
			//订单总金额
			double money = 0 ;
			//循环购物车中的明细记录
			for(TbOrderItem orderItem : cart.getOrderItemList()) {
				//主键
				orderItem.setId(idWorker.nextId());
				//订单编号
				orderItem.setOrderId(orderId);
				//商家ID
				orderItem.setSellerId(cart.getSellerId());
				//添加订单明细
				orderItemMapper.insert(orderItem);
				money += orderItem.getTotalFee().doubleValue();
			}
			
			//合计
			tbOrder.setPayment(new BigDecimal(money));
			
			//添加订单对象
			orderMapper.insert(tbOrder);
			
			orderIdList.add(orderId + "");
			
			total_money += money;
		}
		
		//添加支付日志
		if("1".equals(order.getPaymentType())) {
		
			TbPayLog payLog = new TbPayLog();
			payLog.setOutTradeNo(idWorker.nextId() + "");
			payLog.setCreateTime(new Date());
			payLog.setUserId(order.getUserId());
			payLog.setOrderList(orderIdList.toString().replace("[", "").replace("]", ""));
			payLog.setTotalFee((long)(total_money * 100));
			//交易状态
			payLog.setTradeState("0");
			payLog.setPayType("1");
			
			payLogMapper.insert(payLog);
			
			redisTemplate.boundHashOps("payLog").put(order.getUserId(), payLog);
		}
		
		//3.清除redis中的购物车
		redisTemplate.boundHashOps("cartList").delete(order.getUserId());
	
	}
	
	@Override
	public TbPayLog searchPayLogFromRedis(String userId) {
		return (TbPayLog)redisTemplate.boundHashOps("payLog").get(userId);
	}
	
	@Override
	public void updateOrderStatus(String out_trade_no, String transaction_id) {
		//1.修改支付日志的状态以及相关字段
		TbPayLog payLog = payLogMapper.selectByPrimaryKey(out_trade_no);
		//支付时间
		payLog.setPayTime(new Date());
		//交易成功
		payLog.setTradeState("1");
		//微信的交易流水号
		payLog.setTransactionId(transaction_id);
		//2.修改订单的状态
		String orderList = payLog.getOrderList();
		String[] orderIds = orderList.split(",");
		for(String orderId : orderIds) {
			TbOrder order = orderMapper.selectByPrimaryKey(Long.valueOf(orderId));
			//已付款状态
			order.setStatus("2");
			order.setPaymentTime(new Date());
			
			orderMapper.updateByPrimaryKey(order);
	
		}
		//3.清除缓存中的payLog
		redisTemplate.boundHashOps("payLog").delete(payLog.getUserId());
	}
}
