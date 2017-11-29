package org.egov.common.exception;

import java.io.StringBufferInputStream;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.egov.inv.model.Error;

@Getter
@Setter
@NoArgsConstructor
public class InvalidDataException extends RuntimeException {
	private static final long serialVersionUID = -1509069993620266971L;
	// public static final String code = "001";
	@JsonProperty("code")
	private String code = null;

	@JsonProperty("message")
	private String message = null;

	@JsonProperty("description")
	private String description = null;

	@JsonProperty("params")
	private List<String> params = null;
	
	private String fieldName;
	
	private String messageKey;

	private List<Error> validationErrors = new ArrayList<>();

	public void addDataError(String errorCode,String ...params) {
		Error error=new Error();
		error.setCode(errorCode);
		error.setMessage(MessageFormat.format(ErrorCode.getError(errorCode).getMessage(),params));
		error.setDescription(MessageFormat.format(ErrorCode.getError(errorCode).getDescription(),params));
		error.params(Arrays.asList(params));
		validationErrors.add(error);
	}

	public InvalidDataException(String code, String message, String description, List<String> params) {
		super();
		this.code = code;
		this.message = message;
		this.description = description;
		this.params = params;
	}

	public InvalidDataException(String code, String message, String description, List<String> params,
			List<Error> validationErrors) {
		super();
		this.code = code;
		this.message = message;
		this.description = description;
		this.params = params;
		this.validationErrors = validationErrors;
	}

	public InvalidDataException(String string, String code2, Object object) {
		 
	}

}
