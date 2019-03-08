package com.pinyougou.manager.controller;
import java.util.Arrays;
import java.util.List;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.pinyougou.entity.PageResult;
import com.pinyougou.entity.Result;
import com.pinyougou.pojo.TbGoods;
import com.pinyougou.pojo.TbItem;
import com.pinyougou.pojogroup.Goods;
import com.pinyougou.sellergoods.service.GoodsService;

/**
 * 
 * Title:GoodsController.java
 * Description: 运营商商品管理Controller
 * @author xll
 * @date 2019年2月11日 下午5:34:37
 * @version 1.0
 *
 */
@RestController
@RequestMapping("/goods")
public class GoodsController {

	@Reference
	private GoodsService goodsService;
	
	//@Reference(timeout=100000)
	//private ItemSearchService itemSearchService;
	
	//利用ActiveMQ解耦
	@Autowired
	private JmsTemplate jmsTemplate;
	//用于导入solr索引库的消息目标(点对点方式)
	@Autowired
	private Destination queueSolrDestination;
	//用于删除solr索引库的消息目标(点对点方式)
	@Autowired 
	private Destination queueSolrDeleteDestination;
	//用于生成商品详细页的消息目标(发布订阅的方式)
	@Autowired
	private Destination topicPageDestination;
	//用于删除商品详细页的消息目标(发布订阅的方式)
	@Autowired
	private Destination topicPageDeleteDestination;
	
	
	/*@Reference(timeout=400000)
	private ItemPageService itemPageService;*/
	
	/**
	 * 返回全部列表
	 * @return
	 */
	@RequestMapping("/findAll")
	public List<TbGoods> findAll(){			
		return goodsService.findAll();
	}
	
	
	/**
	 * 返回全部列表
	 * @return
	 */
	@RequestMapping("/findPage")
	public PageResult  findPage(int page,int rows){			
		return goodsService.findPage(page, rows);
	}
	
	/**
	 * 修改
	 * @param goods
	 * @return
	 */
	@RequestMapping("/update")
	public Result update(@RequestBody Goods goods){
		try {
			goodsService.update(goods);
			return new Result(true, "修改成功");
		} catch (Exception e) {
			e.printStackTrace();
			return new Result(false, "修改失败");
		}
	}	
	
	/**
	 * 获取实体
	 * @param id
	 * @return
	 */
	@RequestMapping("/findOne")
	public Goods findOne(Long id){
		return goodsService.findOne(id);		
	}
	
	/**
	 * 批量删除
	 * @param ids
	 * @return
	 */
	@RequestMapping("/delete")
	public Result delete(final Long [] ids){
		try {
			goodsService.delete(ids);
			//从索引库中删除商品列表
			//itemSearchService.deleteByGoodsIds(Arrays.asList(ids));
			jmsTemplate.send(queueSolrDeleteDestination, new MessageCreator() {
	
				@Override
				public Message createMessage(Session session) throws JMSException {
				
					return session.createObjectMessage(ids);
				
				}
			});
			
			//删除每个服务器上的商品详细页
			jmsTemplate.send(topicPageDeleteDestination, new MessageCreator() {
				
				@Override
				public Message createMessage(Session session) throws JMSException {

					return session.createObjectMessage(ids);
				
				}
			
			});
			
			
			return new Result(true, "删除成功"); 
		} catch (Exception e) {
			e.printStackTrace();
			return new Result(false, "删除失败");
		}
	}
	
		/**
	 * 查询+分页
	 * @param brand
	 * @param page
	 * @param rows
	 * @return
	 */
	@RequestMapping("/search")
	public PageResult search(@RequestBody TbGoods goods, int page, int rows  ){
		return goodsService.findPage(goods, page, rows);		
	}
	
	/**
	 * Title:updateStatus
	 * Description: 修改商品审核状态
	 * @param ids
	 * @param status
	 * @return
	 */
	@RequestMapping("/updateStatus")
	public Result updateStatus(Long[] ids, String status) { 
		try {
			goodsService.updateStatus(ids, status);
			
			if("1".equals(status)) {
				//如果审核通过
				//得到需要导入的SKU列表
				List<TbItem> itemList = goodsService.findItemListByGoodsIdListAndStatus(ids, status);
				
				//导入到solr索引库中
				//itemSearchService.importList(itemList);
				
				//利用ActiveMQ将商品导入到solr索引库中(实现解耦合)
				//转化为JSON字符串进行传输
				final String jsonString = JSON.toJSONString(itemList);
				jmsTemplate.send(queueSolrDestination,new MessageCreator() {
					
					@Override
					public Message createMessage(Session session) throws JMSException {
						return session.createTextMessage(jsonString);
					
					}
				});
				
				//生成商品详情页
				for(final Long goodsId : ids) {
					jmsTemplate.send(topicPageDestination,new MessageCreator() {
						
						@Override
						public Message createMessage(Session session) throws JMSException {
							
							return session.createTextMessage(goodsId + "");
						
						}
					});
				}
			}
			
			return new Result(true, "审核完成");
		} catch (Exception e) {
			return new Result(false, "操作失败");
		}
	}
	
	/**
	 * Title:genHtml
	 * Description: 在商品审核通过以后 生成对应的商品详情页
	 * @param goodsId
	 */
	/*
	@RequestMapping("/genHtml")
	public void genHtml(Long goodsId) {
		itemPageService.genItemHtml(goodsId);
	}*/
}
