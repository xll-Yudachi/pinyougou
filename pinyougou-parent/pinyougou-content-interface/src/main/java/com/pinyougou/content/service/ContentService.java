package com.pinyougou.content.service;

import java.util.List;

import com.pinyougou.entity.PageResult;
import com.pinyougou.pojo.TbContent;

public interface ContentService {

	/**
	 * Title:search
	 * Description: 根据条件查找
	 * @param content
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	public PageResult search(TbContent content,int pageNum, int pageSize);

	/**
	 * Title:add
	 * Description: 添加广告
	 * @param content
	 */
	public void add(TbContent content);

	/**
	 * Title:findOne
	 * Description: 根据ID查询广告
	 * @param id
	 * @return
	 */
	public TbContent findOne(Long id);

	/**
	 * Title:delete
	 * Description: 根据ID删除广告
	 * @param ids
	 */
	public void delete(Long[] ids);
	
	/**
	 * Title:update
	 * Description: 修改广告
	 * @param content
	 */
	public void update(TbContent content);
	
	/**
	 * Title:findByCategoryId
	 * Description: 根据分类ID查询广告列表
	 * @param categoryId
	 * @return
	 */
	public List<TbContent> findByCategoryId(Long categoryId);
}
