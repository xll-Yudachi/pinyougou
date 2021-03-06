package com.pinyougou.seckill.controller;

import java.util.List;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.pojo.TbSeckillGoods;
import com.pinyougou.seckill.service.SeckillGoodsService;

/**
 * Title:SeckillGoodsController.java
 * Description: 秒杀商品服务控制层
 * @author xll
 * @date 2019年3月7日 下午5:29:26
 * @version 1.0
 */
@RestController
@RequestMapping("/seckillGoods")
public class SeckillGoodsController {

	@Reference
	private SeckillGoodsService seckillGoodsService;
	
	@RequestMapping("/findList")
	public List<TbSeckillGoods> findList(){
		return seckillGoodsService.findList();
	}
	
	@RequestMapping("/findOneFromRedis")
	public TbSeckillGoods findOneFromRedis(Long id) {
		return seckillGoodsService.findOneFromRedis(id);
	}
}
