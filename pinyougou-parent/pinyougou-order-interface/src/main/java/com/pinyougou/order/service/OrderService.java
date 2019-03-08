package com.pinyougou.order.service;

import com.pinyougou.pojo.TbOrder;
import com.pinyougou.pojo.TbPayLog;

/**
 * Title:OrderService.java
 * Description: 订单服务接口
 * @author xll
 * @date 2019年3月5日 下午8:27:00
 * @version 1.0
 *
 */
public interface OrderService {

	/**
	 * Title:add
	 * Description: 添加订单信息
	 * @param order
	 */
	public void add(TbOrder order);
	
	/**
	 * Title:searchPayLogFromRedis
	 * Description: 根据用户ID获取 支付日志
	 * @param userId
	 * @return
	 */
	public TbPayLog searchPayLogFromRedis(String userId);
	
	/**
	 * Title:updateOrderStatus
	 * Description: 修改订单状态
	 */
	public void updateOrderStatus(String out_trade_no, String transaction_id);
}
