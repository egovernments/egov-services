package org.egov.tl.commons.web.requests;

import java.util.List;

import javax.validation.Valid;

import org.egov.tl.commons.web.contract.RequestInfo;
import org.egov.tl.commons.web.contract.UOM;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * This class describe the set of fields contained in UOMRequest
 * 
 * @author Pavan Kumar Kamma
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UOMRequest {

	@JsonProperty("RequestInfo")
	private RequestInfo requestInfo;

	@Valid
	private List<UOM> uoms;
}