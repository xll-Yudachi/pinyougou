package com.pinyougou.content.service.Impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.pinyougou.content.service.ContentCategoryService;
import com.pinyougou.entity.PageResult;
import com.pinyougou.mapper.TbContentCategoryMapper;
import com.pinyougou.pojo.TbContentCategory;
import com.pinyougou.pojo.TbContentCategoryExample;
import com.pinyougou.pojo.TbContentCategoryExample.Criteria;

@Service
@Transactional
public class ContentCategoryServiceImpl implements ContentCategoryService{

	@Autowired
	private TbContentCategoryMapper contentCategoryMapper;
	
	@Override
	public PageResult findPage(int pageNum, int pageSize) {
		
		PageHelper.startPage(pageNum, pageSize);
		
		Page<TbContentCategory> page = (Page<TbContentCategory>) contentCategoryMapper.selectByExample(null);
		
		PageResult pageResult = new PageResult(page.getTotal(), page.getResult());
		
		return pageResult;
	}
	
	@Override
	public PageResult findPage(TbContentCategory contentCategory, int pageNum, int pageSize) {
		
		PageHelper.startPage(pageNum, pageSize);
		
		TbContentCategoryExample example = new TbContentCategoryExample();
		
		Criteria criteria = example.createCriteria();
		
		if(contentCategory != null) {
			if(contentCategory.getName()!=null && contentCategory.getName().length() > 0) {
				criteria.andNameLike("%" + contentCategory.getName() + "%");
			}
		}
		
		Page<TbContentCategory> page = (Page<TbContentCategory>) contentCategoryMapper.selectByExample(example);
		
		PageResult pageResult = new PageResult(page.getTotal(), page.getResult());
		
		return pageResult;
	}

	@Override
	public void add(TbContentCategory category) {
		contentCategoryMapper.insert(category);
	}

	@Override
	public void delete(Long[] ids) {
		for(Long id : ids) {
			contentCategoryMapper.deleteByPrimaryKey(id);
		}
	}

	@Override
	public TbContentCategory findOne(Long id) {
		return contentCategoryMapper.selectByPrimaryKey(id);
	}
	
	@Override
	public void update(TbContentCategory contentCategory) {

		contentCategoryMapper.updateByPrimaryKey(contentCategory);
	
	}
	
	@Override
	public List<TbContentCategory> findAll() {
		
		return contentCategoryMapper.selectByExample(null);
		
	}
}
