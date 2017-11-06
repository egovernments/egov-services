package org.egov.lams.common.web.response;

import java.util.ArrayList;
import java.util.List;

import org.egov.common.contract.response.ResponseInfo;
import org.egov.lams.common.web.contract.Agreement;
import org.egov.lams.common.web.contract.EstateRegister;
import org.egov.lams.common.web.response.EstateRegisterResponse.EstateRegisterResponseBuilder;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AgreementResponse {

	@JsonProperty("ResponseInfo")
	private ResponseInfo responseInfo;
	
	@JsonProperty("Agreements")
    private List<Agreement> agreements = new ArrayList<Agreement>();
}
