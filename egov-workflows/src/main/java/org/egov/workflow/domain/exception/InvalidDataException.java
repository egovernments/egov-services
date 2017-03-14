package org.egov.workflow.domain.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InvalidDataException extends RuntimeException {
	public static final String code ="001";
	private String fieldName;
	private String messageKey;
	private String defaultMessage;
	
	
}

