package org.egov.lams.common.web.response;

import java.util.ArrayList;
import java.util.List;

import org.egov.common.contract.response.ResponseInfo;
import org.egov.lams.common.web.contract.Agreement;

import com.fasterxml.jackson.annotation.JsonProperty;

public class LandEstateAgreementResponse {
	@JsonProperty("ResponseInfo")
	private ResponseInfo responseInfo;
	
	@JsonProperty("Agreements")
    private List<Agreement> agreements = new ArrayList<Agreement>();
}
