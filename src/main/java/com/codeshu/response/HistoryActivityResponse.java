package com.codeshu.response;

import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @author CodeShu
 * @date 2023/1/3 13:49
 */
@Data
public class HistoryActivityResponse {
	/**
	 * 活动节点ID
	 */
	private String activityId;

	/**
	 * 活动节点名称
	 */
	private String activityName;

	/**
	 * 活动节点类型
	 */
	private String activityType;
	/**
	 *
	 * 任务ID
	 */
	private String taskId;

	/**
	 * 处理人ID
	 */
	private String assigneeId;

	/**
	 * 处理人名字
	 */
	private String assigneeName;

	/**
	 * 评论
	 */
	private List<String> comment;

	/**
	 * 活动节点的任务变量
	 */
	private Map<String,Object> taskVariables;

	/**
	 * 处理时长
	 */
	private Long durationInSeconds;
}
