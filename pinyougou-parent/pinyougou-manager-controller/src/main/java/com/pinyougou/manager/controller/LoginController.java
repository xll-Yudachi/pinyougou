package com.pinyougou.manager.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 
 * Title:LoginController.java
 * Description: 运营商登录Controller
 * @author xll
 * @date 2019年2月11日 下午5:35:11
 * @version 1.0
 *
 */
@RestController
@RequestMapping("/login")
public class LoginController {
	
	@RequestMapping("/getName")
	public Map getName() {
		String name = SecurityContextHolder.getContext().getAuthentication().getName();
		Map map = new HashMap<>();
		map.put("loginName", name);
		return map;
	}
}
