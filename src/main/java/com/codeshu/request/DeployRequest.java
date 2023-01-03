package com.codeshu.request;

import lombok.Data;

/**
 * @author CodeShu
 * @date 2023/1/3 14:40
 */
@Data
public class DeployRequest {
	private String fileName;
	private String deploymentName;
}
