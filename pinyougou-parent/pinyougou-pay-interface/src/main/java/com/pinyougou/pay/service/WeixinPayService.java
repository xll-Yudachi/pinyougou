package com.pinyougou.pay.service;

import java.util.Map;

public interface WeixinPayService {

	/**
	 * Title:createNative
	 * Description: 生成二维码
	 * @param out_trade_no
	 * @param total_fee
	 * @return
	 */
	public Map createNative(String out_trade_no, String total_fee);
	
	/**
	 * Title:queryPayStatus
	 * Description: 查询订单交易情况
	 * @param out_trade_no
	 * @return
	 */
	public Map queryPayStatus(String out_trade_no);
	
	/**
	 * Title:closePay
	 * Description: 关闭订单
	 * @param out_trade_no
	 * @return
	 */
	public Map closePay(String out_trade_no);
}
