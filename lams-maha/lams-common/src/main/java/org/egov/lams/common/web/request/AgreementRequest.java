package org.egov.lams.common.web.request;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.egov.common.contract.request.RequestInfo;
import org.egov.lams.common.web.contract.Agreement;
import org.egov.lams.common.web.contract.EstateRegister;
import org.egov.lams.common.web.request.EstateRegisterRequest.EstateRegisterRequestBuilder;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AgreementRequest {
	  @JsonProperty("RequestInfo")
	    private RequestInfo requestInfo = null;

	    @JsonProperty("Agreements")
	    @Valid
	    private List<Agreement> agreements = new ArrayList<Agreement>();
}
