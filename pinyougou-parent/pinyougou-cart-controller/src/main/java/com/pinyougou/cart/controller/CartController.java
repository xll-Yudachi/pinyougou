package com.pinyougou.cart.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.core.sym.Name;
import com.pinyougou.cart.service.CartService;
import com.pinyougou.entity.Result;
import com.pinyougou.pojogroup.Cart;

import util.CookieUtil;

@RestController
@RequestMapping("/cart")
public class CartController {

	@Reference
	private CartService cartService;
	
	@Autowired
	private HttpServletRequest request;
	@Autowired
	private HttpServletResponse response;
	
	@RequestMapping("/findCartList")
	public List<Cart> findCartList(){
		//当前登录人账号
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		System.out.println("当前登录人账号：" + username);
		
		//如果未登录 从cookie中提取购物车 (cookie中只能提取出字符串)
		String cartListString = CookieUtil.getCookieValue(request, "cartList", "UTF-8" );
		if(cartListString == null || cartListString.equals("")) {
			cartListString = "[]";
		}
		List<Cart> cartList_cookie = JSON.parseArray(cartListString, Cart.class);
		
		if(username.equals("anonymousUser")) {
			//从cookie中获取购物车
			return cartList_cookie;
		}else {
			//如果已经登录
			//获取redis购物车
			List<Cart> cartList_redis = cartService.findCartListFromRedis(username);
			if(cartList_cookie.size() > 0) {	//当cookie购物车中有数据时执行  提高效率
				//得到合并后的购物车
				List<Cart> cartList = cartService.mergeCartList(cartList_cookie, cartList_redis);
				//将合并后的购物车存入redis
				cartService.saveCartListToRedis(username, cartList);
				//清除cookie中的购物车
				CookieUtil.deleteCookie(request, response, "cartList");
				
				return cartList;
			}
			
			return cartList_redis;
		}
	}
	
	@RequestMapping("/addGoodsToCartList")
	@CrossOrigin(origins="http://localhost:9105")
	public Result addGoodsToCartList(Long itemId, Integer num) {
		
		//设置允许跨域的头信息(4.2版本后的SpringMVC可以用注解@CrossOrigin的方式来替代这两句话)
		//可以访问的域(此方法不需要操作cookie)(如果所有的域都可以访问可以在第二个参数上设置 * )
		//response.setHeader("Access-Control-Allow-Origin", "http://localhost:9105");
		//如果操作cookie，必须加上这句话
		//response.setHeader("Access-Control-Allow-Credentials", "true");
		
		//当前登录人账号
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		
		try {
			//提取购物车
			List<Cart> cartList = findCartList();
			//调用服务方法操作购物车
			cartList = cartService.addGoodsToCartList(cartList, itemId, num);
			if(username.equals("anonymousUser")) {
				//如果未登录  将新的购物车存入cookie
				String cartListString = JSON.toJSONString(cartList);
				CookieUtil.setCookie(request, response, "cartList", cartListString, 3600*24, "UTF-8");
			}else {
				//如果登录 将新的购物车存入redis
				cartService.saveCartListToRedis(username, cartList);
			}
			
			return new Result(true, "购物车添加成功");
		} catch (Exception e) {
		
			return new Result(false, "购物车添加失败");
		
		}
	}
}
