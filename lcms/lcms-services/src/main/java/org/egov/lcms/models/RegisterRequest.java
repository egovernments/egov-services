package org.egov.lcms.models;

import java.util.List;
import javax.validation.Valid;
import org.egov.common.contract.request.RequestInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 
 * @author Veswanth
 *	This object holds information about the Register request
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {

	@Valid
	@JsonProperty("RequestInfo")
	private RequestInfo requestInfo;

	@Valid
	@JsonProperty("registers")
	private List<Register> registers;

}
