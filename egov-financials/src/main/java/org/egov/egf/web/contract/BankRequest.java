package org.egov.egf.web.contract;

import javax.validation.Valid;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public @Data class BankRequest {

	 
	@JsonProperty("request_info")
	private RequestInfo requestInfo = null;
	@Valid
	@JsonProperty("bank")
	private Bank  bank  = null;
	public RequestInfo getRequestInfo() {
		return requestInfo;
	}
	public void setRequestInfo(RequestInfo requestInfo) {
		this.requestInfo = requestInfo;
	}
	public Bank getBank() {
		return bank;
	}
	public void setBank(Bank bank) {
		this.bank = bank;
	}

	
 

	 
}
