package com.codeshu.dao;

import com.codeshu.entity.SysUserEntity;

/**
 * @author CodeShu
 * @date 2023/1/3 11:00
 */
public interface SysUserDao {
	SysUserEntity selectById(Integer id);
}
