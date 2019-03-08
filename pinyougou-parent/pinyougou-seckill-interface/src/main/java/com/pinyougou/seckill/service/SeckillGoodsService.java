package com.pinyougou.seckill.service;

import java.util.List;

import com.pinyougou.pojo.TbSeckillGoods;

/*
 * 秒杀商品服务接口
 */
public interface SeckillGoodsService {

	/**
	 * Title:findList
	 * Description: 返回秒杀商品列表
	 * @return
	 */
	public List<TbSeckillGoods> findList();
	
	/**
	 * Title:findOneFromRedis
	 * Description: 从缓存中读取秒杀商品
	 * @param id
	 * @return
	 */
	public TbSeckillGoods findOneFromRedis(Long id);
}
