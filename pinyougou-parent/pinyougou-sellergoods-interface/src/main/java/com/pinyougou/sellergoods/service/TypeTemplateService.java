package com.pinyougou.sellergoods.service;
import java.util.List;
import java.util.Map;

import com.pinyougou.entity.PageResult;
import com.pinyougou.pojo.TbTypeTemplate;

/**
 * 
 * Title:TypeTemplateService.java
 * Description: 商品类型模板管理接口
 * @author xll
 * @date 2019年2月1日 下午9:05:39
 * @version 1.0
 *
 */
public interface TypeTemplateService {

	/**
	 * 返回全部列表
	 * @return
	 */
	public List<TbTypeTemplate> findAll();
	
	
	/**
	 * 返回分页列表
	 * @return
	 */
	public PageResult findPage(int pageNum,int pageSize);
	
	
	/**
	 * 增加
	*/
	public void add(TbTypeTemplate typeTemplate);
	
	
	/**
	 * 修改
	 */
	public void update(TbTypeTemplate typeTemplate);
	

	/**
	 * 根据ID获取实体
	 * @param id
	 * @return
	 */
	public TbTypeTemplate findOne(Long id);
	
	
	/**
	 * 批量删除
	 * @param ids
	 */
	public void delete(Long [] ids);

	/**
	 * 分页
	 * @param pageNum 当前页 码
	 * @param pageSize 每页记录数
	 * @return
	 */
	public PageResult findPage(TbTypeTemplate typeTemplate, int pageNum,int pageSize);
	
	/**
	 * Title:selectOptionList
	 * Description: 返回类型模板列表
	 * @return
	 */
	public List<Map> selectOptionList();
	
	/**
	 * Title:findSpecList
	 * Description: 根据id查询规格列表
	 * @param id
	 * @return
	 */
	public List<Map> findSpecList(Long id);
}
