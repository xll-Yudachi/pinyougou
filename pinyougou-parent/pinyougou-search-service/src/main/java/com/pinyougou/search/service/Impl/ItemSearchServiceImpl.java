package com.pinyougou.search.service.Impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.Criteria;
import org.springframework.data.solr.core.query.FilterQuery;
import org.springframework.data.solr.core.query.GroupOptions;
import org.springframework.data.solr.core.query.HighlightOptions;
import org.springframework.data.solr.core.query.HighlightQuery;
import org.springframework.data.solr.core.query.Query;
import org.springframework.data.solr.core.query.SimpleFilterQuery;
import org.springframework.data.solr.core.query.SimpleHighlightQuery;
import org.springframework.data.solr.core.query.SimpleQuery;
import org.springframework.data.solr.core.query.result.GroupEntry;
import org.springframework.data.solr.core.query.result.GroupPage;
import org.springframework.data.solr.core.query.result.GroupResult;
import org.springframework.data.solr.core.query.result.HighlightEntry;
import org.springframework.data.solr.core.query.result.HighlightEntry.Highlight;
import org.springframework.data.solr.core.query.result.HighlightPage;

import com.alibaba.dubbo.config.annotation.Service;
import com.pinyougou.pojo.TbItem;
import com.pinyougou.search.service.ItemSearchService;

/**
 * Title:ItemSearchServiceImpl.java Description: 商品搜索服务层
 * 
 * @author xll
 * @date 2019年2月22日 下午5:43:23
 * @version 1.0
 *
 */
@Service(timeout = 5000)
public class ItemSearchServiceImpl implements ItemSearchService {

	@Autowired
	private SolrTemplate solrTemplate;
	@Autowired
	private RedisTemplate redisTemplate;

	@Override
	public Map search(Map searchMap) {
		// 普通搜索
		/*
		 * Map map = new HashMap<>();
		 * 
		 * Query query = new SimpleQuery("*:*"); Criteria criteria = new
		 * Criteria("item_keywords").is(searchMap.get("keywords"));
		 * query.addCriteria(criteria);
		 * 
		 * ScoredPage<TbItem> page = solrTemplate.queryForPage(query, TbItem.class);
		 * 
		 * map.put("rows", page.getContent());
		 */
		Map map = new HashMap<>();

		//空格处理(实现多关键字查询)
		String keywords = (String)searchMap.get("keywords");
		searchMap.put("keywords", keywords.replace(" ", ""));
		
		// 1.查询列表
		map.putAll(searchList(searchMap));
		// 2.分组查询商品分类列表
		List<String> categoryList = SearchCategoryList(searchMap);
		map.put("categoryList", categoryList);
		// 3.查询品牌和规格列表
		String category = (String) searchMap.get("category");
		if (!category.equals("")) {
			map.putAll(searchBrandAndSpecList(category));
		} else {
			if (categoryList.size() > 0) {
				map.putAll(searchBrandAndSpecList(categoryList.get(0)));
			}
		}

		return map;
	}

	/**
	 * Title:searchList Description: 高亮显示搜索
	 * 
	 * @param searchMap
	 * @return
	 */
	private Map searchList(Map searchMap) {
		// 高亮搜索显示
		Map map = new HashMap<>();
		// 构建高亮查询
		HighlightQuery query = new SimpleHighlightQuery();
		// 构建高亮选项对象
		HighlightOptions highlightOptions = new HighlightOptions().addField("item_title");
		// 设置前缀
		highlightOptions.setSimplePrefix("<em style='color:red'>");
		// 设置后缀
		highlightOptions.setSimplePostfix("</em>");
		// 为查询对象设置高亮选项
		query.setHighlightOptions(highlightOptions);

		// 1.1 关键字查询
		Criteria criteria = new Criteria("item_keywords").is(searchMap.get("keywords"));
		query.addCriteria(criteria);

		// 1.2 按商品分类过滤
		if (!"".equals(searchMap.get("category"))) {
			// 如果用户选择了分类
			FilterQuery filterQuery = new SimpleFilterQuery();
			Criteria filterCriteria = new Criteria("item_category").is(searchMap.get("category"));
			filterQuery.addCriteria(filterCriteria);
			query.addFilterQuery(filterQuery);

		}

		// 1.3 按商品品牌过滤
		if (!"".equals(searchMap.get("brand"))) {
			// 如果用户选择了分类
			FilterQuery filterQuery = new SimpleFilterQuery();
			Criteria filterCriteria = new Criteria("item_brand").is(searchMap.get("brand"));
			filterQuery.addCriteria(filterCriteria);
			query.addFilterQuery(filterQuery);

		}

		// 1.4 按规格过滤
		if (searchMap.get("spec") != null) {

			Map<String, String> specMap = (Map<String, String>) searchMap.get("spec");
			for (String key : specMap.keySet()) {

				FilterQuery filterQuery = new SimpleFilterQuery();
				Criteria filterCriteria = new Criteria("item_spec_" + key).is(specMap.get(key));
				filterQuery.addCriteria(filterCriteria);
				query.addFilterQuery(filterQuery);

			}
		}

		// 1.5 按照价格区间过滤(0-500  1000-1500  3000-*)
		if (!"".equals(searchMap.get("price"))) {

			String[] price = ((String)searchMap.get("price")).split("-");
			if(!price[0].equals("0")) {
				//如果最低价格不等于0
				FilterQuery filterQuery = new SimpleFilterQuery();
				Criteria filterCriteria = new Criteria("item_price").greaterThanEqual(price[0]);
				filterQuery.addCriteria(filterCriteria);
				query.addFilterQuery(filterQuery);
			}
			if(!price[1].equals("*")) {
				//如果最高价格不等于*
				FilterQuery filterQuery = new SimpleFilterQuery();
				Criteria filterCriteria = new Criteria("item_price").lessThanEqual(price[1]);
				filterQuery.addCriteria(filterCriteria);
				query.addFilterQuery(filterQuery);
			}
			
		}

		//1.6 分页
		//获取页码
		Integer pageNum = (Integer) searchMap.get("pageNum");
		if(pageNum == null) {
			pageNum = 1;
		}
		Integer pageSize = (Integer) searchMap.get("pageSize");
		if(pageSize == null) {
			pageSize = 20;
		}
		
		//1.7 排序查询
		//升序ASC 降序DESC
		String sortValue = (String)searchMap.get("sort");
		//排序字段
		String sortFeild = (String)searchMap.get("sortField");
		
		if(sortValue != null && !"".equals(sortValue)) {
			if(sortValue.equals("ASC")) {
				Sort sort = new Sort(Sort.Direction.ASC, "item_" + sortFeild);
				query.addSort(sort);
			}
			if(sortValue.equals("DESC")) {
				Sort sort = new Sort(Sort.Direction.DESC, "item_" + sortFeild);
				query.addSort(sort);
			}
		}
		
		
		//设置起始索引
		query.setOffset((pageNum-1)*pageSize);
		//设置每页记录数
		query.setRows(pageSize);
		
		// 高亮页对象
		HighlightPage<TbItem> page = solrTemplate.queryForHighlightPage(query, TbItem.class);

		// 高亮入口集合(每条记录的高亮入口)
		List<HighlightEntry<TbItem>> entryList = page.getHighlighted();

		for (HighlightEntry<TbItem> entry : entryList) {
			// 获取高亮列表(高亮域的个数)
			List<Highlight> highlightList = entry.getHighlights();
			if (highlightList.size() > 0 && highlightList.get(0).getSnipplets().size() > 0) {
				TbItem item = entry.getEntity();
				item.setTitle(highlightList.get(0).getSnipplets().get(0));
			}
		}

		map.put("rows", page.getContent());
		//总页数
		map.put("totalPages", page.getTotalPages());
		//总记录数
		map.put("total", page.getTotalElements());
		
		return map;

	}

	/**
	 * Title:SearchCategoryList Description: 分组查询 SQL语句的实现类似于 SELECT 'category' FROM
	 * tb_item WHERE 条件 GROUP BY category
	 * 
	 * @param searchMap
	 * @return
	 */
	private List SearchCategoryList(Map searchMap) {

		List<String> list = new ArrayList<>();

		// 查询对象
		Query query = new SimpleQuery();
		// 根据关键字查询(相当于SQL中的where...)
		Criteria criteria = new Criteria("item_keywords").is(searchMap.get("keywords"));
		query.addCriteria(criteria);
		// 设置分组选项(相当于SQL中的group by)
		GroupOptions groupOptions = new GroupOptions().addGroupByField("item_category");
		query.setGroupOptions(groupOptions);
		// 获取分组页
		GroupPage<TbItem> page = solrTemplate.queryForGroupPage(query, TbItem.class);
		// 获取分组结果对象
		GroupResult<TbItem> groupResult = page.getGroupResult("item_category");
		// 获取分组入口页
		Page<GroupEntry<TbItem>> groupEntries = groupResult.getGroupEntries();
		// 获取分组入口集合
		List<GroupEntry<TbItem>> entryList = groupEntries.getContent();

		for (GroupEntry<TbItem> entry : entryList) {
			list.add(entry.getGroupValue());
		}

		return list;
	}

	/**
	 * Title:searchBrandAndSpecList Description: 根据商品分类名称查询品牌和规格列表
	 * 
	 * @param category
	 * @return
	 */
	private Map searchBrandAndSpecList(String category) {
		Map map = new HashMap<>();
		// 1.根据商品分类名称得到模板ID
		Long templateId = (Long) redisTemplate.boundHashOps("itemCat").get(category);
		if (templateId != null) {
			// 2.根据模板ID获取品牌列表
			List brandList = (List) redisTemplate.boundHashOps("brandList").get(templateId);
			// 3.根据模板ID获取规格列表
			List specList = (List) redisTemplate.boundHashOps("specList").get(templateId);
			map.put("brandList", brandList);
			map.put("specList", specList);
		}

		return map;
	}
	
	@Override
	public void importList(List list) {
		solrTemplate.saveBeans(list);
		solrTemplate.commit();
	}
	
	@Override
	public void deleteByGoodsIds(List goodsIds) {
		
		Query query = new SimpleQuery("*:*");
		Criteria criteria = new Criteria("item_goodsid").in(goodsIds);
		query.addCriteria(criteria);
		solrTemplate.delete(query);
		solrTemplate.commit();
	
	}
}
