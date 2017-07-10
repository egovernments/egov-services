package org.egov.collection.web.contract;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class BillAccountDetailsWrapper {

	@JsonProperty("BillAccountDetails")
	private BillAccountDetails billAccountDetails;
	
	private String functionCode;

}
