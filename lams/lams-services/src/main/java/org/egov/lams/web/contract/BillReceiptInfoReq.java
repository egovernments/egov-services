package org.egov.lams.web.contract;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class BillReceiptInfoReq {
	
	@JsonProperty("RequestInfo")
	private RequestInfo requestInfo = null;

	private BillReceiptReq billReceiptInfo;

}
