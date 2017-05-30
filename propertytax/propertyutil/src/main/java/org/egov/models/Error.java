package org.egov.models;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;

/**
 * Error
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Error {

	@NonNull
	private String code;

	@NonNull
	private String message;

	private String description;

	private Map<String,String> filelds;

}
