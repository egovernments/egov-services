package org.egov.collection.indexer.contract;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@ToString
public class BillWrapper {

	@JsonProperty("Bill")
	private Bill billInfo;
	
	private String paidBy;
	
	@JsonProperty("BillDetailsWrapper")
	private List<BillDetailsWrapper> billDetailsWrapper = new ArrayList<>(); //for collection-service

}
