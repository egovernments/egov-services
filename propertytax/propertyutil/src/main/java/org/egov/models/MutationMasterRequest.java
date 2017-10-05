package org.egov.models;

import java.util.List;

import javax.validation.Valid;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 
 * @author Prasad
 *
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MutationMasterRequest {

	@JsonProperty("RequestInfo")
	private RequestInfo requestInfo;

	@JsonProperty("mutationMasters")
	@Valid
	private List<MutationMaster> mutationMasters;

}
