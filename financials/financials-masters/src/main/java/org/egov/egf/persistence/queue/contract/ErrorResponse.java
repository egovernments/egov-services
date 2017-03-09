package org.egov.egf.persistence.queue.contract;

import lombok.Data;

@Data
public class ErrorResponse {

	 
	private ResponseInfo responseInfo;

	 
	private Error error;

	
}
