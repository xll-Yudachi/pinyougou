package com.pinyougou.page.service.Impl;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import com.pinyougou.mapper.TbGoodsDescMapper;
import com.pinyougou.mapper.TbGoodsMapper;
import com.pinyougou.mapper.TbItemCatMapper;
import com.pinyougou.mapper.TbItemMapper;
import com.pinyougou.page.service.ItemPageService;
import com.pinyougou.pojo.TbGoods;
import com.pinyougou.pojo.TbGoodsDesc;
import com.pinyougou.pojo.TbItem;
import com.pinyougou.pojo.TbItemExample;
import com.pinyougou.pojo.TbItemExample.Criteria;

import freemarker.template.Configuration;
import freemarker.template.Template;

@Service
public class ItemPageServiceImpl implements ItemPageService{

	@Autowired
	private FreeMarkerConfigurer freeMarkerConfigurer;
	
	@Value("${pagedir}")
	private String pagedir;
	
	@Autowired
	private TbGoodsMapper goodsMapper;
	
	@Autowired
	private TbGoodsDescMapper goodsDescMapper;
	
	@Autowired
	private TbItemCatMapper itemCatMapper;
	
	@Autowired
	private TbItemMapper itemMapper;
	
	@Override
	public boolean genItemHtml(Long goodsId) {
		//得到配置对象
		Configuration configuration = freeMarkerConfigurer.getConfiguration();
		try {
			//得到模板对象
			Template template = configuration.getTemplate("item.ftl");
			//创建数据模型
			Map dataModel = new HashMap<>();
			
			//1.商品主表数据(填充数据模型)
			TbGoods goods = goodsMapper.selectByPrimaryKey(goodsId);
			dataModel.put("goods", goods);
			
			//2.商品扩展表数据(填充数据模型)
			TbGoodsDesc goodsDesc = goodsDescMapper.selectByPrimaryKey(goodsId);
			dataModel.put("goodsDesc", goodsDesc);
			
			//3.读取商品分类(填充数据模型)
			String itemCat_1 = itemCatMapper.selectByPrimaryKey(goods.getCategory1Id()).getName();
			String itemCat_2 = itemCatMapper.selectByPrimaryKey(goods.getCategory2Id()).getName();
			String itemCat_3 = itemCatMapper.selectByPrimaryKey(goods.getCategory3Id()).getName();
			dataModel.put("itemCat_1", itemCat_1);
			dataModel.put("itemCat_2", itemCat_2);
			dataModel.put("itemCat_3", itemCat_3);
			
			//4.读取SKU列表(填充数据模型)
			TbItemExample example = new TbItemExample();
			Criteria criteria = example.createCriteria();
			criteria.andGoodsIdEqualTo(goodsId);
			criteria.andStatusEqualTo("1");
			
			//按是否默认字段进行降序排序，目的是返回的结果第一条为默认SKU
			example.setOrderByClause("is_default desc");
			
			List<TbItem> itemList = itemMapper.selectByExample(example);
			dataModel.put("itemList", itemList);
			
			//创建输出流
			//用此种方法可能会出现生成界面的中文乱码问题
			//Writer out = new FileWriter(pagedir + goodsId + ".html");
			//改用这种方法可以解决中文乱码问题
			Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(pagedir+goodsId+".html")),"UTF-8"));
			
			//进行页面的输出
			template.process(dataModel, out);
			//关闭输出流
			out.close();
			
			return true;
		
		} catch (Exception e) {
			
			return false;
		
		}
	}
	
	@Override
	public boolean deleteItemHtml(Long[] goodsIds) {
		try {
			for(Long goodsId :goodsIds) {
				new File(pagedir + goodsId + ".html").delete();
			}
			return true;
		} catch (Exception e) {
			return false;
		}
	}
}
