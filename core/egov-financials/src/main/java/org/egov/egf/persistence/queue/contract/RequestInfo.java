package org.egov.egf.persistence.queue.contract;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
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
