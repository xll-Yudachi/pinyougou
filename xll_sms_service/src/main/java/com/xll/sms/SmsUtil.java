package com.xll.sms;

import java.io.IOException;

import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import com.github.qcloudsms.SmsSingleSender;
import com.github.qcloudsms.SmsSingleSenderResult;
import com.github.qcloudsms.httpclient.HTTPException;

@Component
public class SmsUtil {

	@Autowired
	private Environment environment;
	
	public void SendSms(String phoneNumber,Integer templateId,String[] params,String smsSign) {
		
		Integer appid = Integer.valueOf(environment.getProperty("appid"));
		String appkey = environment.getProperty("appkey");
		System.out.println(appid);
		System.out.println(appkey);
		
		//String checkCode = new Double((Math.random()*9+1)*1000).toString();
		//phoneNumbers[0] 
		//templateId
		//
		try {
		    SmsSingleSender ssender = new SmsSingleSender(appid, appkey);
		    SmsSingleSenderResult result = ssender.sendWithParam("86", phoneNumber,
		        templateId, params, smsSign, "", "");  // 签名参数未提供或者为空时，会使用默认签名发送短信
		    System.out.println(result);
		} catch (HTTPException e) {
		    // HTTP响应码错误
		    e.printStackTrace();
		} catch (JSONException e) {
		    // json解析错误
		    e.printStackTrace();
		} catch (IOException e) {
		    // 网络IO错误
		    e.printStackTrace();
		}
	}
}