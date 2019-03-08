package com.pinyougou.manager.controller;

import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.entity.PageResult;
import com.pinyougou.entity.Result;
import com.pinyougou.pojo.TbBrand;
import com.pinyougou.sellergoods.service.BrandService;

/**
 * 
 * Title:BrandController.java
 * Description:运营商品牌管理Controller层
 * @author xll
 * @date 2019年2月11日 下午5:34:11
 * @version 1.0
 *
 */
@RestController
@RequestMapping("/brand")
public class BrandController {

	@Reference
	private BrandService brandService;
	
	@RequestMapping("/findAll")
	public List<TbBrand> findAll(){
		return brandService.findAll();
	}
	
	@RequestMapping("/findPage")
	public PageResult findPage(int page,int size) {
		return brandService.findPage(page, size);
	}
	
	@RequestMapping("/add")
	public Result add(@RequestBody TbBrand brand) {
		try {
			brandService.addBrand(brand);
			return new Result(true, "添加成功");
		} catch (Exception e) {
			return new Result(false, "添加失败" + e.getMessage());
		}
	}
	
	@RequestMapping("/findBrand")
	public TbBrand findBrand(Long id) {
		return brandService.findBrandById(id);
	}
	
	@RequestMapping("/update")
	public Result update(@RequestBody TbBrand brand) {
		try {
			brandService.updateBrand(brand);
			return new Result(true, "修改成功");
		} catch (Exception e) {
			return new Result(false, "修改失败" + e.getMessage());
		}
	}
	
	@RequestMapping("/delete")
	public Result delete(Long[] ids) {
		try {
			brandService.deleteBrandById(ids);
			return new Result(true, "删除成功");
		} catch (Exception e) {
			return new Result(false, "删除失败" + e.getMessage());
		}
	}
	
	@RequestMapping("/search")
	public PageResult search(@RequestBody TbBrand brand,int page,int size) {
		return brandService.findPage(brand, page, size);
	}
	
	@RequestMapping("/selectOptionList")
	public List<Map> selectOptionList(){
		return brandService.selectOptionList();
	}
}
