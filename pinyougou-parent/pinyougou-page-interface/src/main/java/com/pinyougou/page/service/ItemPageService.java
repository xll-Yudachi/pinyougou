package com.pinyougou.page.service;

/**
 * Title:ItemPageService.java
 * Description: 商品详情页生成服务
 * @author xll
 * @date 2019年2月27日 下午8:03:19
 * @version 1.0
 */
public interface ItemPageService {

	/**
	 * Title:genItemHtml
	 * Description: 生成商品详情页
	 * @param goodsId
	 * @return
	 */
	public boolean genItemHtml(Long goodsId);
	
	/**
	 * Title:deleteItemHtml
	 * Description: 删除商品的详细页
	 * @param goodsIds
	 * @return
	 */
	public boolean deleteItemHtml(Long[] goodsIds);
}
