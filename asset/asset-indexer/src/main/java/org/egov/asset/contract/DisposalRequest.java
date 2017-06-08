package org.egov.asset.contract;

import org.egov.asset.model.Disposal;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * DisposalRequest
 */
@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class DisposalRequest {

	@JsonProperty("RequestInfo")
	private RequestInfo requestInfo = null;

	@JsonProperty("Disposal")
	private Disposal disposal = null;

}
