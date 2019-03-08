package com.pinyougou.user.service;

import com.pinyougou.entity.Result;
import com.pinyougou.pojo.TbUser;

/**
 * Title:UserService.java
 * Description: 用户操作接口
 * @author xll
 * @date 2019年3月2日 下午3:56:16
 * @version 1.0
 *
 */
public interface UserService {

	/**
	 * Title:add
	 * Description: 添加用户
	 * @param user
	 */
	public void add(TbUser user);
	
	/**
	 * Title:creatSmsCode
	 * Description: 发送短信验证码
	 * @param phone
	 */
	public void creatSmsCode(String phone);
	
	/**
	 * Title:checkSmsCode
	 * Description: 校验验证码
	 * @param phone
	 * @param code
	 * @return
	 */
	public boolean checkSmsCode(String phone,String code);
	
}
