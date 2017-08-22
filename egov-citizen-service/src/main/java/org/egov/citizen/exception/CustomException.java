package org.egov.citizen.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class CustomException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;

	private Integer code;
	
	private String customMessage;
	
	private String description;

}
