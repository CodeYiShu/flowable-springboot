package com.codeshu.service;

import com.codeshu.entity.SysUserEntity;

/**
 * @author CodeShu
 * @date 2023/1/3 11:06
 */
public interface SysUserService {
	SysUserEntity selectById(Integer id);
}
