package com.pinyougou.user.service.Impl;

import java.util.Date;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.Session;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;

import com.alibaba.dubbo.config.annotation.Service;
import com.pinyougou.entity.Result;
import com.pinyougou.mapper.TbUserMapper;
import com.pinyougou.pojo.TbUser;
import com.pinyougou.user.service.UserService;

/**
 * Title:UserServiceImpl.java
 * Description: 用户操作服务层
 * @author xll
 * @date 2019年3月2日 下午4:05:29
 * @version 1.0
 */
@Service(timeout=100000)
public class UserServiceImpl implements UserService{ 

	@Autowired
	private TbUserMapper userMapper;
	
	@Autowired
	private RedisTemplate redisTemplate;
	
	@Autowired
	private JmsTemplate jmsTemplate;
	
	@Autowired
	private Destination smsDestination;
	
	//参数注入
	@Value("${templateId}")
	private int templateId;
	@Value("${smsSign}")
	private String smsSign;
	
	/**
	 * 添加用户
	 */
	@Override
	public void add(TbUser user) {
			//用户注册时间
			user.setCreated(new Date());
			//用户修改资料时间
			user.setUpdated(new Date());
			//用户注册来源
			user.setSourceType("1");
			//将密码进行MD5加密
			user.setPassword(DigestUtils.md5Hex(user.getPassword()));
			
			userMapper.insert(user);
	}
	
	@Override
	public void creatSmsCode(final String phone) {
		//1.生成一个6位随机数
		final String smscode = (long)(Math.random()*1000000) + "";
		//2.将验证码放入Redis缓存
		redisTemplate.boundHashOps("smscode").put(phone, smscode);
		//3.将短信内容发送给activeMQ
		jmsTemplate.send(smsDestination, new MessageCreator() {
			
			@Override
			public Message createMessage(Session session) throws JMSException {
				MapMessage message = session.createMapMessage();
				//手机号
				message.setString("phoneNumber", phone);
				//模板ID
				message.setInt("templateId", templateId);
				//签名
				message.setString("smsSign", smsSign);
				//模板参数
				message.setString("params", smscode);
				
				return message;
			
			}
		});
	}
	
	@Override
	public boolean checkSmsCode(String phone, String code) {
		String sysCode = (String)redisTemplate.boundHashOps("smscode").get(phone);
		
		if(sysCode == null) {
			return false;
		}
		if(!sysCode.equals(code)) {
			return false;
		}
		
		return true;
		
	}
}
