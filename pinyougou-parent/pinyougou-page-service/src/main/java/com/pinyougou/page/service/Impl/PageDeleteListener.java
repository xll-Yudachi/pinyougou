package com.pinyougou.page.service.Impl;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.pinyougou.page.service.ItemPageService;

/**
 * Title:PageDeleteListener.java
 * Description: 监听器 用于删除商品详情页
 * @author xll
 * @date 2019年3月1日 下午2:50:48
 * @version 1.0
 */
@Component
public class PageDeleteListener implements MessageListener{

	@Autowired
	private ItemPageService itemPageService;
	
	@Override
	public void onMessage(Message message) {
		ObjectMessage objectMessage = (ObjectMessage) message;
		try {
			Long[] goodsIds = (Long[]) objectMessage.getObject();
			boolean success = itemPageService.deleteItemHtml(goodsIds);
			if(success) {
				System.out.println("商品详情页删除成功！");
			}else {
				System.out.println("商品详情页删除失败！");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
}
