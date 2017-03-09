package org.egov.lams.model;

import java.util.Date;

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
	private Date ts;	
	private String action;	
	private String did;	
	private String key;	
	private String msgId;	
	private String requesterId;	
	private String authToken;
}
