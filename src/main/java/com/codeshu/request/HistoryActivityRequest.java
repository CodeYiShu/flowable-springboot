package com.codeshu.request;

import lombok.Data;

/**
 * @author CodeShu
 * @date 2023/1/3 13:54
 */
@Data
public class HistoryActivityRequest {
	/**
	 * 流程实例ID
	 */
	private String processInstanceId;

	/**
	 * 是否只获取用户任务
	 */
	private Boolean onlyUserTask;
}
