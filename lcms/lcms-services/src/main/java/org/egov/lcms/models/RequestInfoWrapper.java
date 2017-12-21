package org.egov.lcms.models;

import org.egov.common.contract.request.RequestInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
/**
 * 
 * @author Veswanth
 *	This object holds information about the RequestInfo wrapper
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestInfoWrapper {
	
	@JsonProperty(value = "RequestInfo")
	private RequestInfo requestInfo;
}
