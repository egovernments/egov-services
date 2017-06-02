package org.egov.mr.web.contract;

import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Component
@Builder
public class RequestInfo {

	private String action;

	private String did;

	private String msgId;

	private String requesterId;

	private String authToken;

	private String apiId;

	private String ver;

	private String ts;

	private String key;

	private String tenantId;
}
