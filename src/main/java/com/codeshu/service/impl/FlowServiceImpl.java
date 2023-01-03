package com.codeshu.service.impl;

import com.codeshu.entity.SysUserEntity;
import com.codeshu.request.CompleteRequest;
import com.codeshu.request.DeployRequest;
import com.codeshu.request.HistoryActivityRequest;
import com.codeshu.request.StartProcessInstanceRequest;
import com.codeshu.response.HistoryActivityResponse;
import com.codeshu.response.TodoTaskResponse;
import com.codeshu.service.FlowService;
import com.codeshu.service.SysUserService;
import org.apache.commons.lang3.StringUtils;
import org.flowable.bpmn.constants.BpmnXMLConstants;
import org.flowable.engine.HistoryService;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.common.impl.identity.Authentication;
import org.flowable.engine.history.HistoricActivityInstance;
import org.flowable.engine.history.HistoricActivityInstanceQuery;
import org.flowable.engine.repository.DeploymentBuilder;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.engine.task.Comment;
import org.flowable.task.api.Task;
import org.flowable.task.api.TaskQuery;
import org.flowable.variable.api.history.HistoricVariableInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author CodeShu
 * @date 2023/1/3 10:44
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class FlowServiceImpl implements FlowService {
	@Autowired
	private SysUserService userService;
	@Autowired
	private RuntimeService runtimeService;
	@Autowired
	private TaskService taskService;
	@Autowired
	private HistoryService historyService;
	@Autowired
	private RepositoryService repositoryService;

	public void deploy(DeployRequest request) {
		String fileName = request.getFileName();
		String deploymentName = request.getDeploymentName();
		//1、创建一个新的部署
		DeploymentBuilder deployment = repositoryService.createDeployment();
		//2、指定流程模型XML文件和流程名字
		deployment.addClasspathResource(fileName);
		deployment.name(deploymentName);
		//3、进行部署
		deployment.deploy();
	}

	@Override
	public void startProcessInstance(StartProcessInstanceRequest request) {
		Integer startUserId = request.getStartUserId();
		String processDefinitionKey = request.getProcessDefinitionKey();
		SysUserEntity sysUserEntity = userService.selectById(startUserId);

		Map<String, Object> params = new HashMap<>();
		params.put("startUser", sysUserEntity);

		//设置流程启动人ID
		Authentication.setAuthenticatedUserId(String.valueOf(startUserId));
		//根据流程定义的key进行启动流程实例，将启动流程用户对象存为流程实例变量
		ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(processDefinitionKey, params);
		System.out.println("processInstance.getId() = " + processInstance.getId());
		System.out.println("processInstance.getProcessDefinitionId() = " + processInstance.getProcessDefinitionId());

		//恢复为NULL，避免多线程问题
		Authentication.setAuthenticatedUserId(null);
	}

	@Override
	public List<TodoTaskResponse> getTodoTask(Integer userId) {
		List<TodoTaskResponse> responseList = new ArrayList<>();

		//根据处理人获取其所有流程实例的待办任务
		TaskQuery taskQuery = taskService.createTaskQuery().taskAssignee(String.valueOf(userId)).active();
		List<Task> taskList = taskQuery.list();
		for (Task task : taskList) {
			TodoTaskResponse todoTaskResponse = new TodoTaskResponse();
			todoTaskResponse.setTaskId(task.getId());
			todoTaskResponse.setProcessDefinitionId(task.getProcessDefinitionId());
			todoTaskResponse.setProcessInstanceId(task.getProcessInstanceId());
			todoTaskResponse.setTaskName(task.getName());
			todoTaskResponse.setProcessDefinitionKey(task.getTaskDefinitionKey());
			responseList.add(todoTaskResponse);
		}
		return responseList;
	}

	@Override
	public void complete(CompleteRequest request) {
		String taskId = request.getTaskId();
		String comment = request.getComment();
		Map<String, Object> processInstanceVariables = request.getProcessInstanceVariables();
		Map<String, Object> taskVariables = request.getTaskVariables();

		//获取要完成的待办任务
		Task task = taskService.createTaskQuery().taskId(taskId).active().singleResult();
		if (Objects.isNull(task)) {
			return;
		}

		//添加评论
		if (StringUtils.isNotBlank(comment)) {
			taskService.addComment(taskId, task.getProcessInstanceId(), comment);
		}

		//为此待办任务设置任务变量
		if (!taskVariables.isEmpty()) {
			taskService.setVariablesLocal(taskId, taskVariables);
		}

		if (!processInstanceVariables.isEmpty()) {
			//为此待办任务所在的流程实例设置流程实例变量
			taskService.setVariables(taskId, processInstanceVariables);
		}

		//完成任务
		taskService.complete(taskId);
	}

	@Override
	public List<HistoryActivityResponse> getHistoryActivity(HistoryActivityRequest request) {
		List<HistoryActivityResponse> responseList = new ArrayList<>();
		String processInstanceId = request.getProcessInstanceId();
		Boolean onlyUserTask = request.getOnlyUserTask();

		//获取历史活动节点的查询对象
		HistoricActivityInstanceQuery historicActivityInstanceQuery = historyService.createHistoricActivityInstanceQuery();

		//只查询用户任务节点 - 根据流程实例ID进行查询所有已经结束的用户任务节点，且按照节点结束时间倒叙
		if (Objects.nonNull(onlyUserTask) && onlyUserTask.equals(true)) {
			historicActivityInstanceQuery.processInstanceId(processInstanceId)
					.activityType(BpmnXMLConstants.ELEMENT_TASK_USER)
					.finished()
					.orderByHistoricActivityInstanceEndTime().asc();
		} else {
			//查询所有节点 - 根据流程实例ID进行查询所有已经结束的节点，且按照节点结束时间倒叙
			historicActivityInstanceQuery.processInstanceId(processInstanceId)
					.finished()
					.orderByHistoricActivityInstanceEndTime().asc();
		}

		List<HistoricActivityInstance> historicActivityInstances = historicActivityInstanceQuery.list();
		for (HistoricActivityInstance historicActivityInstance : historicActivityInstances) {
			HistoryActivityResponse response = new HistoryActivityResponse();
			//节点基本信息
			response.setActivityId(historicActivityInstance.getActivityId());
			response.setActivityName(historicActivityInstance.getActivityName());
			response.setActivityType(historicActivityInstance.getActivityType());
			//任务ID（有些节点不是任务，所以没有任务ID）
			if (StringUtils.isNotBlank(historicActivityInstance.getTaskId())){
				response.setTaskId(historicActivityInstance.getTaskId());
				//任务变量
				List<HistoricVariableInstance> taskVariables = historyService.createHistoricVariableInstanceQuery()
						.processInstanceId(historicActivityInstance.getProcessInstanceId())
						.taskId(historicActivityInstance.getTaskId()).list();
				if (Objects.nonNull(taskVariables) && !taskVariables.isEmpty()) {
					response.setTaskVariables(new HashMap<>());
					for (HistoricVariableInstance taskVariable : taskVariables) {
						response.getTaskVariables().put(taskVariable.getVariableName(), taskVariable.getValue());
					}
				}
			}
			//处理人ID和处理人名字（有些节点不是用户任务，所以没有处理人）
			if (StringUtils.isNotBlank(historicActivityInstance.getAssignee())) {
				response.setAssigneeId(historicActivityInstance.getAssignee());
				SysUserEntity userEntity = userService.selectById(Integer.valueOf(historicActivityInstance.getAssignee()));
				response.setAssigneeName(Objects.nonNull(userEntity) ? userEntity.getUserName() : "");
			}
			//评论
			List<Comment> taskComments = taskService.getTaskComments(historicActivityInstance.getTaskId());
			if (Objects.nonNull(taskComments) && !taskComments.isEmpty()) {
				response.setComment(new ArrayList<>());
				for (Comment taskComment : taskComments) {
					response.getComment().add(taskComment.getFullMessage());
				}
			}
			//处理时间
			response.setDurationInSeconds(historicActivityInstance.getDurationInMillis() / 1000);
			responseList.add(response);
		}

		return responseList;
	}
}
