package org.egov.tl.commons.web.contract;

import java.util.HashMap;
import java.util.Map;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Error object will be returned as a part of reponse body in conjunction with
 * ResponseInfo as part of ErrorResponse whenever the request processing status
 * in the ResponseInfo is FAILED. HTTP return in this scenario will usually be
 * HTTP 400.
 * 
 * @author : Pavan Kumar Kamma
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Error {

	@JsonProperty("code")
	@NotNull
	private String code = null;

	@JsonProperty("message")
	@NotNull
	private String message = null;

	@JsonProperty("description")
	private String description = null;

	@JsonProperty("params")
	private Map<String, String> fileds = new HashMap<String, String>();
}