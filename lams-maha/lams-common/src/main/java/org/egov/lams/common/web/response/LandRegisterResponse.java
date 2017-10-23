package org.egov.lams.common.web.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.egov.common.contract.response.ResponseInfo;
import org.egov.lams.common.web.contract.EstateRegister;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LandRegisterResponse {

	@JsonProperty("ResponseInfo")
	private ResponseInfo responseInfo;
	
	@JsonProperty("landRegisters")
    private List<EstateRegister> landRegisters = new ArrayList<EstateRegister>();
}
