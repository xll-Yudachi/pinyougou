package com.pinyougou.page.service.Impl;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.pinyougou.page.service.ItemPageService;

/**
 * 
 * Title:PageListener.java
 * Description: 监听类 用于生成商品静态详情页
 * @author xll
 * @date 2019年3月1日 下午2:29:14
 * @version 1.0
 *
 */

@Component
public class PageListener implements MessageListener{

	@Autowired
	private ItemPageService itemPageService;
	
	@Override
	public void onMessage(Message message) {
		TextMessage textMessage = (TextMessage) message;
		try {
			String text = textMessage.getText();
			boolean success = itemPageService.genItemHtml(Long.parseLong(text));
			if(success) {
				System.out.println("商品静态详情页生成完毕！");
			}else {
				System.out.println("商品静态详情页生成失败！");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
}
