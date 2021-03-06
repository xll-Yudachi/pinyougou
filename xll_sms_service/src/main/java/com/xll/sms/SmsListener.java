package com.xll.sms;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
public class SmsListener {

	@Autowired
	private SmsUtil smsUtil;
	
	@JmsListener(destination="sms")
	public void sendSms(Map map) {
		
		String[] paramsList = {(String)map.get("params")};
		
		smsUtil.SendSms((String)map.get("phoneNumber"), 
						(Integer)map.get("templateId"),
						paramsList,
						(String)map.get("smsSign")
						);
	
	}
	
}
