package org.egov.lams.web.contract;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonRootName;

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
@JsonRootName("RequestInfo")
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
	private User userInfo;
}
