package com.pinyougou.search.service.Impl;

import java.util.List;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.pinyougou.pojo.TbItem;
import com.pinyougou.search.service.ItemSearchService;

/**
 * 
 * Title:ItemSearchListener.java
 * Description: 用于添加索引库
 * @author xll
 * @date 2019年3月1日 下午12:36:09
 * @version 1.0
 *
 */
@Component
public class ItemSearchListener implements MessageListener{

	@Autowired
	private ItemSearchService itemSearchService;
	
	@Override
	public void onMessage(Message message) {
		
		TextMessage textMessage = (TextMessage)message;
		try {
			//获取传输过来的JSON字符串
			String text = textMessage.getText();
			List<TbItem> itemList = JSON.parseArray(text, TbItem.class);
			itemSearchService.importList(itemList);
			System.out.println("solr索引库导入完成");
		} catch (Exception e) {
			e.printStackTrace();
		}
	
	}
}
