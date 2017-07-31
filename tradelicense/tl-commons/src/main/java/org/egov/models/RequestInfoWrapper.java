package org.egov.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * This class describe the set of fields contained in RequestInfoWrapper
 * 
 * @author Pavan Kumar Kamma
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestInfoWrapper {

	@JsonProperty(value = "RequestInfo")
	private RequestInfo requestInfo;

}