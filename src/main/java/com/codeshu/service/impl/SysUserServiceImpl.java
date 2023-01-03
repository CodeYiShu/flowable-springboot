package com.codeshu.service.impl;

import com.codeshu.dao.SysUserDao;
import com.codeshu.entity.SysUserEntity;
import com.codeshu.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author CodeShu
 * @date 2023/1/3 11:06
 */
@Service
public class SysUserServiceImpl implements SysUserService {
	@Autowired
	private SysUserDao userDao;

	@Override
	public SysUserEntity selectById(Integer id) {
		return userDao.selectById(id);
	}
}
