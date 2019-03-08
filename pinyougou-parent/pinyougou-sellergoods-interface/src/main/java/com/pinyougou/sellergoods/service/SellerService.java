package com.pinyougou.sellergoods.service;
import java.util.List;
import com.pinyougou.entity.PageResult;
import com.pinyougou.pojo.TbSeller;

/**
 * 
 * Title:SellerService.java
 * Description: 商家入驻接口
 * @author xll
 * @date 2019年2月6日 下午5:11:35
 * @version 1.0
 *
 */
public interface SellerService {

	/**
	 * Title:add
	 * Description: 商家入驻添加
	 * @param seller
	 */
	public void add(TbSeller seller);
	
	/**
	 * Title:findPage
	 * Description: 根据条件分页查询
	 * @param seller
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	public PageResult findPage(TbSeller seller, int pageNum,int pageSize);
	
	/**
	 * Title:findOne
	 * Description: 根据id查询
	 * @param sellerId
	 * @return
	 */
	public TbSeller findOne(String sellerId);
	
	/**
	 * Title:updateStatus
	 * Description: 根据id修改状态
	 * @param sellerId
	 * @param status
	 */
	public void updateStatus(String sellerId,String status);
	
}
