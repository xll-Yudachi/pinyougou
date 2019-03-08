package com.pinyougou.solr.util;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.Query;
import org.springframework.data.solr.core.query.SimpleQuery;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.pinyougou.mapper.TbItemMapper;
import com.pinyougou.pojo.TbItem;
import com.pinyougou.pojo.TbItemExample;
import com.pinyougou.pojo.TbItemExample.Criteria;

@Component
public class SolrUtil {

	@Autowired
	private TbItemMapper itemMapper;
	@Autowired
	private SolrTemplate solrTemplate;
	
	/**
	 * Title:importItemData
	 * Description: 导入全部索引库
	 */
	public void importItemData() {
		
		TbItemExample example = new TbItemExample();
		Criteria criteria = example.createCriteria();
		//审核通过的才进行导入
		criteria.andStatusEqualTo("1");
		List<TbItem> itemList = itemMapper.selectByExample(example);
	
		for(TbItem item : itemList) {
			//从数据库中提取规格JSON字符串转换为map
			Map specMap = JSON.parseObject(item.getSpec(),Map.class);
			item.setSpecMap(specMap);
		}
		
		solrTemplate.saveBeans(itemList);
		solrTemplate.commit();
	
		System.out.println("索引库导入完成    ღ( ´･ᴗ･` )比心");
	}
	
	/**
	 * Title:deleteAllItem
	 * Description: 删除全部索引库
	 */
	public void deleteAllItem() {
		Query query = new SimpleQuery("*:*");
		solrTemplate.delete(query);
		solrTemplate.commit();
		System.out.println("索引库删除成功   ღ( ´･ᴗ･` )比心");
	}
	
	public static void main(String[] args) {
		
		ApplicationContext context = new ClassPathXmlApplicationContext("classpath*:spring/applicationContext*.xml");
		SolrUtil solrUtil = (SolrUtil) context.getBean("solrUtil");
		//导入
		solrUtil.importItemData();
		//删除
		//solrUtil.deleteAllItem();
	}
}
