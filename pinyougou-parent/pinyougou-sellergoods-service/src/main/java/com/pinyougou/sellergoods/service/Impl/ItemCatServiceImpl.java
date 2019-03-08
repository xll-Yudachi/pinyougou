package com.pinyougou.sellergoods.service.Impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.dubbo.config.annotation.Service;
import com.pinyougou.mapper.TbItemCatMapper;
import com.pinyougou.pojo.TbItemCat;
import com.pinyougou.pojo.TbItemCatExample;
import com.pinyougou.pojo.TbItemCatExample.Criteria;
import com.pinyougou.pojo.TbTypeTemplate;
import com.pinyougou.sellergoods.service.ItemCatService;


/**
 * 
 * Title:ItemCatServiceImpl.java
 * Description: 商品分类服务层
 * @author xll
 * @date 2019年2月8日 下午2:55:07
 * @version 1.0
 *
 */
@Service(timeout=5000)
@Transactional
public class ItemCatServiceImpl implements ItemCatService {

	@Autowired
	private TbItemCatMapper itemCatMapper;
	
	@Autowired
	private RedisTemplate redisTemplate;
	
	@Override
	public List<TbItemCat> findByParentId(Long parentId) {

		TbItemCatExample example = new TbItemCatExample();
		Criteria criteria = example.createCriteria();
		criteria.andParentIdEqualTo(parentId);
		
		//将模板ID放入缓存中(以商品分类名称)
		List<TbItemCat> itemCatList = findAll();
		for(TbItemCat itemCat : itemCatList) {
			redisTemplate.boundHashOps("itemCat").put(itemCat.getName(), itemCat.getTypeId());
		}
		
		return itemCatMapper.selectByExample(example);
	
	}
	
	@Override
	public void add(TbItemCat itemCat) {
		itemCatMapper.insert(itemCat);
	}
	
	@Override
	public TbItemCat findOne(Long id) {
		return itemCatMapper.selectByPrimaryKey(id);
	}
	
	@Override
	public void update(TbItemCat itemCat) {
		itemCatMapper.updateByPrimaryKey(itemCat);
	}
	
	@Override
	public boolean delete(Long[] ids) {
		Boolean flag = null;
		
		for(Long id : ids) {
			flag = true;
			TbItemCatExample example = new TbItemCatExample();
			Criteria criteria = example.createCriteria();
			criteria.andParentIdEqualTo(id);
			List<TbItemCat> result = itemCatMapper.selectByExample(example);

			if(result.size()>0) {
				flag = false;
				break;
			}
		}
		
		if(flag) {
			for(Long id : ids) {
				itemCatMapper.deleteByPrimaryKey(id);
			}
		}
		
		return flag;
	}
	
	@Override
	public List<TbItemCat> findAll() {
		return itemCatMapper.selectByExample(null);
	}

}
