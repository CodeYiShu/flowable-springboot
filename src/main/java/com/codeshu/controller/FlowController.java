package com.codeshu.controller;

import com.codeshu.request.CompleteRequest;
import com.codeshu.request.DeployRequest;
import com.codeshu.request.HistoryActivityRequest;
import com.codeshu.request.StartProcessInstanceRequest;
import com.codeshu.response.HistoryActivityResponse;
import com.codeshu.response.TodoTaskResponse;
import com.codeshu.service.FlowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author CodeShu
 * @date 2023/1/3 10:44
 */
@RestController
public class FlowController {
	@Autowired
	private FlowService flowService;

	@PostMapping("/deploy")
	public void deploy(@RequestBody DeployRequest request) {
		flowService.deploy(request);
	}

	@PostMapping("/startProcessInstance")
	public void startProcessInstance(@RequestBody StartProcessInstanceRequest request) {
		flowService.startProcessInstance(request);
	}

	@GetMapping("/getTodoTask/{userId}")
	public List<TodoTaskResponse> getTodoTask(@PathVariable("userId") Integer userId) {
		return flowService.getTodoTask(userId);
	}

	@PostMapping("/complete")
	public void complete(@RequestBody CompleteRequest request) {
		flowService.complete(request);
	}

	@GetMapping("/getHistoryActivity")
	private List<HistoryActivityResponse> getHistoryActivity(HistoryActivityRequest request) {
		return flowService.getHistoryActivity(request);
	}
}
