package com.pinyougou.sellergoods.service;
import java.util.List;
import com.pinyougou.pojo.TbItemCat;

/**
 * 
 * Title:ItemCatService.java
 * Description: 商品分类服务接口
 * @author xll
 * @date 2019年2月8日 下午2:48:29
 * @version 1.0
 *
 */
public interface ItemCatService {

	 /**
	  * Title:findByParentId
	  * Description: 根据父级id查询商品分类
	  * @param parentId
	  * @return
	  */
	 public List<TbItemCat> findByParentId(Long parentId);
	 
	 /**
	  * Title:add
	  * Description: 添加商品分类
	  * @param itemCat
	  */
	 public void add(TbItemCat itemCat);
	 
	 /**
	  * Title:findOne
	  * Description: 根据Id查询商品分类实例
	  * @param id
	  * @return
	  */
	 public TbItemCat findOne(Long id);
	 
	 /**
	  * Title:update
	  * Description:修改商品分类信息
	  * @param itemCat
	  */
	 public void update(TbItemCat itemCat);
	 
	 /**
	  * Title:delete
	  * Description:判断能否删除 能则删除返回true 否则不删除返回false
	  * @param ids
	  * @return
	  */
	 public boolean delete(Long[] ids);
	 
	 /**
	  * Title:findAll
	  * Description: 查询所有商品分类
	  * @return
	  */
	 public List<TbItemCat> findAll();
}
