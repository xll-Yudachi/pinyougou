package com.pinyougou.content.service.Impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.pinyougou.content.service.ContentService;
import com.pinyougou.entity.PageResult;
import com.pinyougou.mapper.TbContentMapper;
import com.pinyougou.pojo.TbContent;
import com.pinyougou.pojo.TbContentExample;
import com.pinyougou.pojo.TbContentExample.Criteria;

@Service
@Transactional
public class ContentServiceImpl implements ContentService{

	@Autowired
	private TbContentMapper contentMapper;
	@Autowired
	private RedisTemplate redisTemplate;
	
	@Override
	public PageResult search(TbContent content, int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);
		
		TbContentExample example = new TbContentExample();
		
		Criteria criteria = example.createCriteria();
		
		if(content != null) {
			if(content.getTitle() != null && content.getTitle().length() > 0) {
				criteria.andTitleLike("%" + content.getTitle() + "%");
			}
		}
		
		Page<TbContent> page = (Page<TbContent>) contentMapper.selectByExample(example);
		
		PageResult pageResult = new PageResult(page.getTotal(), page.getResult());
		
		return pageResult;
	}

	@Override
	public void add(TbContent content) {

		contentMapper.insert(content);
		//清除缓存
		redisTemplate.boundHashOps("content").delete(content.getCategoryId());
	}

	@Override
	public TbContent findOne(Long id) {
		
		return contentMapper.selectByPrimaryKey(id);
	
	}

	@Override
	public void delete(Long[] ids) {

		for(Long id : ids) {
			//清除缓存
			Long categoryId = contentMapper.selectByPrimaryKey(id).getCategoryId();
			redisTemplate.boundHashOps("content").delete(categoryId);
			//删除广告
			contentMapper.deleteByPrimaryKey(id);
		}
		
	}

	@Override
	public void update(TbContent content) {
		//查询原来的分组ID
		Long categoryId = contentMapper.selectByPrimaryKey(content.getId()).getCategoryId();
		//清除缓存
		redisTemplate.boundHashOps("content").delete(categoryId);
		
		contentMapper.updateByPrimaryKey(content);

		//清除更新后可能存在的categoryId不同造成的缓存问题
		if(categoryId.longValue() != content.getCategoryId().longValue()) {
			redisTemplate.boundHashOps("content").delete(content.getCategoryId());
		}
	}

	@Override
	public List<TbContent> findByCategoryId(Long categoryId) {
		
		List<TbContent> list = (List<TbContent>) redisTemplate.boundHashOps("content").get(categoryId);
		
		if(list == null) {
			TbContentExample example = new TbContentExample();
			
			Criteria criteria = example.createCriteria();
			
			criteria.andCategoryIdEqualTo(categoryId);
			criteria.andStatusEqualTo("1");
			example.setOrderByClause("sort_order");
			
			list = contentMapper.selectByExample(example);
			redisTemplate.boundHashOps("content").put(categoryId, list);
		}
		
		return list;
	}
}
