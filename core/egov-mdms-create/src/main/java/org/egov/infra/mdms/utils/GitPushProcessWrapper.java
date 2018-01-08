package org.egov.infra.mdms.utils;

import org.egov.mdms.model.MDMSCreateRequest;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
@Builder
public class GitPushProcessWrapper {
	private String filePath;
	private String content;
	private boolean isCreate;
	private MDMSCreateRequest mdmsCreateRequest;

}
