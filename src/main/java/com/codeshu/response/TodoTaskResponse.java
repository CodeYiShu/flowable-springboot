package com.codeshu.response;

import lombok.Data;

/**
 * @author CodeShu
 * @date 2023/1/3 11:53
 */
@Data
public class TodoTaskResponse {
	/**
	 * 待办任务ID
	 */
	private String taskId;
	/**
	 * 待办任务名称
	 */
	private String taskName;
	/**
	 * 流程实例ID
	 */
	private String processInstanceId;
	/**
	 * 流程定义ID
	 */
	private String processDefinitionId;
	/**
	 * 流程定义Key
	 */
	private String processDefinitionKey;
}
