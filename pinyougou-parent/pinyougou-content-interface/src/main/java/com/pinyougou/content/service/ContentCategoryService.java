package com.pinyougou.content.service;

import java.util.List;

import com.pinyougou.entity.PageResult;
import com.pinyougou.pojo.TbContentCategory;

public interface ContentCategoryService {

	/**
	 * Title:findPage
	 * Description: 分页查询广告类别
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	public PageResult findPage(int pageNum,int pageSize);
	
	/**
	 * Title:findPage
	 * Description: 根据条件分页查询
	 * @param contentCategory
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	public PageResult findPage(TbContentCategory contentCategory, int pageNum, int pageSize);
	
	/**
	 * Title:add
	 * Description: 添加广告类别
	 * @param category
	 * @return
	 */
	public void add(TbContentCategory category);
	
	/**
	 * Title:delete
	 * Description: 删除广告类别
	 * @param ids
	 */
	public void delete(Long[] ids);
	
	/**
	 * Title:findOne
	 * Description: 根据ID查询实体
	 * @param id
	 * @return
	 */
	public TbContentCategory findOne(Long id);
	
	/**
	 * Title:update
	 * Description: 更新广告类别
	 * @param contentCategory
	 */
	public void update(TbContentCategory contentCategory);
	
	/**
	 * Title:findAll
	 * Description: 查询所有广告列表
	 * @return
	 */
	public List<TbContentCategory> findAll();
}
