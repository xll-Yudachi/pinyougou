package com.pinyougou.user.service.Impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.pinyougou.mapper.TbAddressMapper;
import com.pinyougou.pojo.TbAddress;
import com.pinyougou.pojo.TbAddressExample;
import com.pinyougou.pojo.TbAddressExample.Criteria;
import com.pinyougou.user.service.AddressService;

@Service(timeout=100000)
public class AddressServiceImpl implements AddressService{

	@Autowired
	private TbAddressMapper addressMapper;
	
	@Override
	public List<TbAddress> findListByUserId(String userId) {
		
		TbAddressExample example = new TbAddressExample();
		Criteria criteria = example.createCriteria();
		criteria.andUserIdEqualTo(userId);
		
		return addressMapper.selectByExample(example);
		
	}
	
	@Override
	public void addAddress(TbAddress address, String userId) {
		//填充地址信息
		address.setUserId(userId);
		address.setIsDefault("0");
		address.setCreateDate(new Date());
		
		addressMapper.insert(address);
	
	}
	
}
