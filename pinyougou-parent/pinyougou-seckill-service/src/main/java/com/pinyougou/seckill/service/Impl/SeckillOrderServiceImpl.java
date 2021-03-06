package com.pinyougou.seckill.service.Impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import com.alibaba.dubbo.config.annotation.Service;
import com.pinyougou.mapper.TbSeckillGoodsMapper;
import com.pinyougou.mapper.TbSeckillOrderMapper;
import com.pinyougou.pojo.TbSeckillGoods;
import com.pinyougou.pojo.TbSeckillOrder;
import com.pinyougou.seckill.service.SeckillOrderService;

import util.IdWorker;

/**
 * Title:SeckillOrderServiceImpl.java
 * Description: 秒杀订单实现类
 * @author xll
 * @date 2019年3月7日 下午9:09:02
 * @version 1.0
 */
@Service(timeout=100000)
public class SeckillOrderServiceImpl implements SeckillOrderService{
	
	@Autowired
	private RedisTemplate redisTemplate;
	@Autowired
	private TbSeckillOrderMapper seckillOrderMapper;
	@Autowired
	private TbSeckillGoodsMapper seckillGoodsMapper;
	@Autowired
	private IdWorker idWorker;
	
	@Override
	public void submitOrder(Long seckillId, String userId) {
		
		//1.查询缓存中的商品
		TbSeckillGoods seckillGoods = (TbSeckillGoods)redisTemplate.boundHashOps("seckillGoods").get(seckillId);
		if(seckillGoods == null) {
			throw new RuntimeException("商品不存在");
		}
		if(seckillGoods.getStockCount() <= 0) {
			throw new RuntimeException("商品已经被抢光");
		}
		
		//2.减少库存
		seckillGoods.setStockCount(seckillGoods.getStockCount()-1);
		redisTemplate.boundHashOps("seckillGoods").put(seckillId, seckillGoods);
		if(seckillGoods.getStockCount() == 0) {
			//更新数据库
			seckillGoodsMapper.updateByPrimaryKey(seckillGoods);
			//删除对应的redis缓存
			redisTemplate.boundHashOps("seckillGoods").delete(seckillId);
		}
		
		//3.存储秒杀订单(只向缓存中存入  支付时写入数据库)
		TbSeckillOrder seckillOrder = new TbSeckillOrder();
		seckillOrder.setId(idWorker.nextId());
		seckillOrder.setSeckillId(seckillId);
		seckillOrder.setMoney(seckillGoods.getCostPrice());
		seckillOrder.setUserId(userId);
		seckillOrder.setSellerId(seckillGoods.getSellerId());
		seckillOrder.setCreateTime(new Date());
		seckillOrder.setStatus("0");
		
		redisTemplate.boundHashOps("seckillOrder").put(userId, seckillOrder);
	}
	
	@Override
	public TbSeckillOrder searchOrderFromRedisByUserId(String userId) {
		return (TbSeckillOrder)redisTemplate.boundHashOps("seckillOrder").get(userId);
	}
	
	@Override
	public void saveOrderFromRedisToDb(String userId, Long orderId, String transactionId) {
		//1.从缓存中提取订单数据
		TbSeckillOrder seckillOrder = searchOrderFromRedisByUserId(userId);
		if(seckillOrder == null) {
			throw new RuntimeException("不存在此订单");
		}
		if(seckillOrder.getId().longValue() != orderId.longValue()) {
			throw new RuntimeException("订单号不一致");
		}
		
		//2.修改订单实体的属性
		seckillOrder.setPayTime(new Date());
		seckillOrder.setStatus("1");
		seckillOrder.setTransactionId(transactionId);
		
		//3.将订单存入数据库
		seckillOrderMapper.insert(seckillOrder);
		
		//4.清除缓存中的订单
		redisTemplate.boundHashOps("seckillOrder").delete(userId);
	}
	
	@Override
	public void deleteOrderFromRedis(String userId, Long orderId) {
		//1.查询出缓存中的订单
		TbSeckillOrder seckillOrder = searchOrderFromRedisByUserId(userId);
		
		if(seckillOrder != null) {
			//2.删除缓存中的订单
			redisTemplate.boundHashOps("seckillOrder").delete(userId);
			
			//3.库存回退
			TbSeckillGoods seckillGoods = (TbSeckillGoods)redisTemplate.boundHashOps("seckillGoods").get(seckillOrder.getSeckillId());
			if(seckillGoods != null) {
				seckillGoods.setStockCount(seckillGoods.getStockCount()+1);
				redisTemplate.boundHashOps("seckillGoods").put(seckillOrder.getSeckillId(), seckillGoods);
			}else {
				seckillGoods = new TbSeckillGoods();
				seckillGoods.setId(seckillOrder.getSeckillId());
				seckillGoods.setStockCount(1);
				redisTemplate.boundHashOps("seckillGoods").put(seckillOrder.getSeckillId(), seckillGoods);
			}
		}
	}
}
