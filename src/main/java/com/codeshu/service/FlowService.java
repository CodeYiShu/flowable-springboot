package com.codeshu.service;

import com.codeshu.request.CompleteRequest;
import com.codeshu.request.DeployRequest;
import com.codeshu.request.HistoryActivityRequest;
import com.codeshu.request.StartProcessInstanceRequest;
import com.codeshu.response.HistoryActivityResponse;
import com.codeshu.response.TodoTaskResponse;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * @author CodeShu
 * @date 2023/1/3 10:44
 */
public interface FlowService {
	void deploy(DeployRequest request);

	void startProcessInstance(@RequestBody StartProcessInstanceRequest request);

	List<TodoTaskResponse> getTodoTask(Integer userId);

	void complete(CompleteRequest request);

	List<HistoryActivityResponse> getHistoryActivity(HistoryActivityRequest request);
}
