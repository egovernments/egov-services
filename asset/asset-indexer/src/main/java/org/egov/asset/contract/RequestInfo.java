package org.egov.asset.contract;

import java.util.Date;

import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;



@Component
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
