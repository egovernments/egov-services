package org.egov.collection.web.contract;

import java.util.ArrayList;
import java.util.List;

import org.egov.collection.model.AuditDetails;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
@Builder
@Setter
@Getter
@ToString
public class BillWrapper {

	@JsonProperty("Bill")
	private Bill billInfo;
	
	private String paidBy;
	
	@JsonProperty("BillDetailsWrapper")
	private List<BillDetailsWrapper> billDetailsWrapper = new ArrayList<>(); //for collection-service
	
	private AuditDetails auditDetails;

}
