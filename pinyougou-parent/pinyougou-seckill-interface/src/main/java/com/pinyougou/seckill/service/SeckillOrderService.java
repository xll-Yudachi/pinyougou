package com.pinyougou.seckill.service;

import java.util.List;

import com.pinyougou.pojo.TbSeckillOrder;

/**
 * Title:SeckillOrderService.java
 * Description: 秒杀订单服务
 * @author xll
 * @date 2019年3月7日 下午9:08:02
 * @version 1.0
 *
 */
public interface SeckillOrderService {

	/**
	 * Title:submitOrder
	 * Description: 秒杀商品下单
	 * @param seckillId
	 * @param userId
	 */
	public void submitOrder(Long seckillId, String userId);
	
	/**
	 * Title:searchOrderFromRedisByUserId
	 * Description: 从缓存中提取订单
	 * @param userId
	 * @return
	 */
	public TbSeckillOrder searchOrderFromRedisByUserId(String userId);
	
	/**
	 * Title:saveOrderFromRedisToDb
	 * Description: 保存订单到数据库
	 * @param userId
	 * @param orderId
	 * @param transactionId
	 */
	public void saveOrderFromRedisToDb(String userId,Long orderId,String transactionId);
	
	/**
	 * Title:deleteOrderFromRedis
	 * Description: 删除缓存中的订单
	 * @param userId
	 * @param orderId
	 */
	public void deleteOrderFromRedis(String userId, Long orderId);
}
