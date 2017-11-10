package org.egov.lcms.notification.model;

import java.util.List;

import javax.validation.Valid;

import org.egov.common.contract.request.RequestInfo;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * This object holds information about the opinion request
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OpinionRequest {
	@JsonProperty("RequestInfo")
	@Valid
	private RequestInfo requestInfo = null;

	@JsonProperty("opinions")
	@Valid
	private List<Opinion> opinions = null;
}
