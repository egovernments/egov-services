package org.egov.collection.web.contract;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

import org.egov.collection.model.AuditDetails;

import com.fasterxml.jackson.annotation.JsonProperty;


@Getter
@Setter
@ToString
public class Bill   {
	//TODO some of the fields are mandatory in yml, lets discuss billdetail and billaccountdetail also for more clarity
	private String id;

	private String payeeName;

	private String payeeAddress;

	private String payeeEmail;
	
	private String paidBy;

	private Boolean isActive;

	private Boolean isCancelled;

	private List<BillDetail> billDetails = new ArrayList<>(); //for billing-service
	
	@JsonProperty("BillDetailsWrapper")
	private List<BillDetailsWrapper> billDetailsWrapper = new ArrayList<>(); //for collection-service


	private String tenantId;
	
	private AuditDetails auditDetails;

}

