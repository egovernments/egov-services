package org.egov.egf.web.contract;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import lombok.Data;

public @Data class BankContractRequest {

	private RequestInfo requestInfo = null;

	private List<BankContract> banks = null;
	
	@JsonProperty(access=Access.WRITE_ONLY)
	private  BankContract bank = null;

	private Page page;

}
