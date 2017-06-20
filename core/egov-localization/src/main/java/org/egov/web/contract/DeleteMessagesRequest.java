package org.egov.web.contract;

import java.util.List;

import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DeleteMessagesRequest {
	@NotNull
	private RequestInfo requestInfo;
	
	@NotNull
	private String locale;
	
	@NotNull
	private List<Message> messages; 
	
	@NotNull
	private String tenantId;
}
