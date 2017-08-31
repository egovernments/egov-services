package org.egov.mr.contract;

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
public class ResponseInfo {
	private String resMsgId;

	private String status;

	private String apiId;

	private String ver;

	private String ts;

	private String key;

	private String tenantId;
}
