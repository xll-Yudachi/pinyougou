package com.pinyougou.seckill.service.Impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import com.alibaba.dubbo.config.annotation.Service;
import com.pinyougou.mapper.TbSeckillGoodsMapper;
import com.pinyougou.pojo.TbSeckillGoods;
import com.pinyougou.pojo.TbSeckillGoodsExample;
import com.pinyougou.pojo.TbSeckillGoodsExample.Criteria;
import com.pinyougou.seckill.service.SeckillGoodsService;

@Service(timeout=100000)
public class SeckillGoodsServiceImpl implements SeckillGoodsService{

	@Autowired
	private TbSeckillGoodsMapper seckillGoodsMapper;
	@Autowired
	private RedisTemplate redisTemplate;
	
	@Override
	public List<TbSeckillGoods> findList() {
		
		List<TbSeckillGoods> seckillGoodsList = redisTemplate.boundHashOps("seckillGoods").values();
		
		if(seckillGoodsList == null || seckillGoodsList.size() == 0) {
			
			
		}
		
		return seckillGoodsList;
	}
	
	@Override
	public TbSeckillGoods findOneFromRedis(Long id) {
	    return (TbSeckillGoods)redisTemplate.boundHashOps("seckillGoods").get(id);
	}
}
