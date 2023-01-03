package com.codeshu.request;

import lombok.Data;

/**
 * @author CodeShu
 * @date 2023/1/3 10:47
 */
@Data
public class StartProcessInstanceRequest {
	/**
	 * 流程定义key
	 */
	private String processDefinitionKey;

	/**
	 * 启动流程人id
	 */
	private Integer startUserId;
}
