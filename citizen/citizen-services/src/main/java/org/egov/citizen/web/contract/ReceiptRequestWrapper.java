package org.egov.citizen.web.contract;

import javax.validation.constraints.NotNull;

import org.egov.common.contract.request.RequestInfo;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ReceiptRequestWrapper {

	@JsonProperty("PGRequest")
	@NotNull
	private ReceiptRequest receiptRequest;
	
	@JsonProperty("RequestInfo")
	@NotNull
	private RequestInfo requestInfo;
}
