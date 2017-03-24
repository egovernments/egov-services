package org.egov.lams.notification.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class RequestInfo {

	private String apiId;	
	private String ver;	
	private String ts;	
	private String action;	
	private String did;	
	private String key;	
	private String msgId;	
	private String requesterId;	
	private String authToken;
}
