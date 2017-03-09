package org.egov.egf.persistence.queue.contract;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RequestInfo {

	private String action = null;

	private String did = null;

	private String msgId = null;

	private String requesterId = null;

	private String authToken = null;

	private String apiId = null;

	private String ver = null;

	private Date ts = null;

	private String key = null;

	private String tenantId = null;

}
