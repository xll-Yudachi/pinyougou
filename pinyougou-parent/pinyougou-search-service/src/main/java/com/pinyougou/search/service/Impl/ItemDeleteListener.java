package com.pinyougou.search.service.Impl;

import java.util.Arrays;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.pinyougou.search.service.ItemSearchService;

/**
 * Title:ItemDeleteListener.java
 * Description: 用于删除索引库
 * @author xll
 * @date 2019年3月1日 下午12:35:49
 * @version 1.0
 */
@Component
public class ItemDeleteListener implements MessageListener{

	@Autowired
	private ItemSearchService itemSearchService;
	
	@Override
	public void onMessage(Message message) {

		ObjectMessage objectMessage = (ObjectMessage) message;
		try {
			Long[] goodsIds = (Long[])objectMessage.getObject();
			for(Long id : goodsIds) {
				System.out.println(id.longValue());
			}
			itemSearchService.deleteByGoodsIds(Arrays.asList(goodsIds));
			System.out.println("solr索引库数据删除完成");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
