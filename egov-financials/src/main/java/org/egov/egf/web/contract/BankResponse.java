package org.egov.egf.web.contract;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
@Data
public class BankResponse {
	
	@JsonProperty("response_info")
	private ResponseInfo responseInfo = null;
	
	@JsonProperty("banks")
	private List<Bank> banks = new ArrayList<Bank>();

	public ResponseInfo getResponseInfo() {
		return responseInfo;
	}

	public void setResponseInfo(ResponseInfo responseInfo) {
		this.responseInfo = responseInfo;
	}

	public List<Bank> getBanks() {
		return banks;
	}

	public void setBanks(List<Bank> banks) {
		this.banks = banks;
	}


	

	
	 
}
