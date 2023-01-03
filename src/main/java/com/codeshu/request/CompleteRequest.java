package com.codeshu.request;

import lombok.Data;

import java.util.Map;

/**
 * @author CodeShu
 * @date 2023/1/3 12:48
 */
@Data
public class CompleteRequest {
	/**
	 * 待办任务ID
	 */
	private String taskId;
	/**
	 * 评论
	 */
	private String comment;
	/**
	 * 流程实例变量 - 可控制网关走向
	 */
	private Map<String,Object> processInstanceVariables;
	/**
	 * 任务变量
	 */
	private Map<String,Object> taskVariables;
}
