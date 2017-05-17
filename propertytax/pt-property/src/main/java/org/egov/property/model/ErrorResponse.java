package org.egov.property.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ErrorResponse {

	private Error error;
	
	private ResponseInfo resoneInfo;
}
