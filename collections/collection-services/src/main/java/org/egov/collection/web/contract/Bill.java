package org.egov.collection.web.contract;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

import org.egov.collection.model.AuditDetails;

import com.fasterxml.jackson.annotation.JsonProperty;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Bill   {
	//TODO some of the fields are mandatory in yml, lets discuss billdetail and billaccountdetail also for more clarity
	private String id;

	private String payeeName;

	private String payeeAddress;

	private String payeeEmail;
	
	private Boolean isActive;

	private Boolean isCancelled;
	
	private String paidBy;

	@JsonProperty("BillDetail")
	private List<BillDetail> billDetails = new ArrayList<>(); //for billing-service
	
	private String tenantId;
	
}

