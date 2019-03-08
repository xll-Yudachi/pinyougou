package com.pinyougou.sellergoods.service;

import java.util.List;
import java.util.Map;

import com.pinyougou.entity.PageResult;
import com.pinyougou.pojo.TbBrand;

/**
 * 
 * Title:BrandService.java
 * Description: 品牌接口
 * @author xll
 * @date 2019年1月25日 下午6:33:26
 * @version 1.0
 *
 */
public interface BrandService {

	//查询所有品牌
	public List<TbBrand> findAll();
	
	/**
	 * 
	 * Title:findPage
	 * Description: 分页品牌查询
	 * @param pageNum 当前页数
	 * @param pageSize 每页记录数
	 * @return 分页对象
	 */
	public PageResult findPage(int pageNum,int pageSize);
	
	/**
	 * 增加商品品牌
	 */
	public void addBrand(TbBrand brand);
	
	/**
	 * Title:findBrandById
	 * Description:根据id查询商品品牌信息
	 * @param id 商品品牌id
	 * @return 商品品牌对象
	 */
	public TbBrand findBrandById(Long id);

	/**
	 * Title:updateBrand
	 * Description: 修改商品品牌信息
	 * @param brand 商品品牌对象
	 */
	public void updateBrand(TbBrand brand);
	
	/**
	 * Title:delete
	 * Description: 根据id删除商品品牌
	 * @param ids
	 */
	public void deleteBrandById(Long[] ids);
	
	/**
	 * Title:findPage
	 * Description: 根据条件分页查询商品品牌
	 * @param brand	条件
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	public PageResult findPage(TbBrand brand, int pageNum, int pageSize);
	
	/**
	 * Title:selectOptionList
	 * Description: 返回下拉列表
	 * @return
	 */
	public List<Map> selectOptionList();
}
