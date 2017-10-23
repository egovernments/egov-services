package org.egov.models;

import java.util.List;

import javax.validation.Valid;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * demolitionMasterRequest
 * 
 * @author vishal
 *
 */
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class DemolitionReasonRequest {
	@JsonProperty("RequestInfo")
	private RequestInfo requestInfo;

	@Valid
	@JsonProperty("demolitionReasons")
	private List<DemolitionReason> demolitionReasons;

}
