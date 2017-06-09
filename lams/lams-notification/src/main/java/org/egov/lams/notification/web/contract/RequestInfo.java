package org.egov.lams.notification.web.contract;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

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
	@JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss", timezone = "IST")
	private Date ts;	
	private String action;	
	private String did;	
	private String key;	
	private String msgId;	
	private String requesterId;	
	private String correlationId;	
	private String authToken;
	private User userInfo;
}
