package org.egov.tl.commons.web.requests;

import java.util.List;

import javax.validation.Valid;

import org.egov.tl.commons.web.contract.PenaltyRate;
import org.egov.tl.commons.web.contract.RequestInfo;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * This class describe the set of fields contained in PenalityRateRequest
 * 
 * @author Pavan Kumar Kamma
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PenaltyRateRequest {

	@JsonProperty("RequestInfo")
	private RequestInfo requestInfo;

	@Valid
	private List<PenaltyRate> penaltyRates;
}