package com.pinyougou.user.controller;

import java.util.Date;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.entity.Result;
import com.pinyougou.pojo.TbUser;
import com.pinyougou.user.service.UserService;

import util.PhoneFormatCheckUtils;

/**
 * Title:UserController.java
 * Description: 用户操作控制层
 * @author xll
 * @date 2019年3月2日 下午4:05:12
 * @version 1.0
 */
@RestController
@RequestMapping("/user")
public class UserController {

	@Reference
	private UserService userService;
	
	/**
	 * Title:add
	 * Description: 添加用户
	 * @param user
	 */
	@RequestMapping("/add")
	public Result add(@RequestBody TbUser user,String smscode) {
		
		if(!userService.checkSmsCode(user.getPhone(), smscode)) {
			return new Result(false, "验证码错误");
		}
		
		try {
			userService.add(user);
			return new Result(true, "用户注册成功");
		} catch (Exception e) {
			return new Result(false, "用户注册失败");
		}
	}
	
	@RequestMapping("/sendCode")
	public Result sendCode(String phone) {
		
		if(!PhoneFormatCheckUtils.isPhoneLegal(phone)) {
			return new Result(false, "手机号格式不正确");
		}
		
		try {
			userService.creatSmsCode(phone);
			return new Result(true, "验证码发送成功");
		} catch (Exception e) {
			return new Result(false, "验证码发送失败");
		}
	}
}
