package org.egov.lcms.models;

import java.util.List;
import org.egov.common.contract.response.ResponseInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 
 * @author Veswanth
 *	This object holds information about the Register Response
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterResponse {

	@JsonProperty("ResponseInfo")
	private ResponseInfo responseInfo;

	@JsonProperty("registers")
	private List<Register> registers;
}
