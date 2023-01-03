package com.codeshu.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * @author CodeShu
 * @date 2023/1/3 11:00
 */
@Data
public class SysUserEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	private Integer id;
	private String userName;
}
