package com.pinyougou.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.pinyougou.pojo.TbSeller;
import com.pinyougou.sellergoods.service.SellerService;

/**
 * 
 * Title:UserDetailsServiceImpl.java
 * Description: 认证类
 * @author xll
 * @date 2019年2月7日 下午4:58:11
 * @version 1.0
 *
 */
public class UserDetailsServiceImpl implements UserDetailsService{

	private SellerService sellerService;
	
	public void setSellerService(SellerService sellerService) {
		this.sellerService = sellerService;
	}
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		//构建角色列表
		List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
		grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_SELLER"));
		
		//得到商家对象
		TbSeller seller = sellerService.findOne(username);
		
		//如果有此商家
		if(seller != null) {
			//如果此商家审核通过
			if(seller.getStatus().equals("1")) {
				//返回验证对象
				return new User(username, seller.getPassword(), grantedAuthorities);
			}else {
				//审核不通过则验证不通过
				return null;	
			}
		}else {
			//没有此商家则验证不通过
			return null;	
		}
	}

}
