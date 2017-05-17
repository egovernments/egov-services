package org.egov.property.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class RequestInfo {

	private String appId;

	private String ver;

	private String ts;

	private String msgId;

	private String action;

	private String did;

	private String key;

	private String requesterId;

	private String authToken;
}
