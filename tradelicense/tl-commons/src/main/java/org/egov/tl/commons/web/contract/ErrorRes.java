package org.egov.tl.commons.web.contract;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * All APIs will return ErrorRes in case of failure which will carry
 * ResponseInfo as metadata and Error object as actual representation of error.
 * In case of bulk apis, some apis may chose to return the array of Error
 * objects to indicate individual failure.
 * 
 * @author : Pavan Kumar Kamma
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ErrorRes {

	@JsonProperty("ResponseInfo")
	@SerializedName("ResponseInfo")
	private ResponseInfo responseInfo = null;

	@JsonProperty("Errors")
	@SerializedName("Errors")
	private List<Error> errors = new ArrayList<Error>();
}
