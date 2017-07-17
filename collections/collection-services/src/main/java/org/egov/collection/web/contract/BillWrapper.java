package org.egov.collection.web.contract;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotNull;

import org.egov.collection.model.AuditDetails;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BillWrapper {

	@NotNull
	@JsonProperty("Bill")
	private Bill billInfo;
	
	private String paidBy;
	
	@NotNull
	@JsonProperty("BillDetailsWrapper")
	private List<BillDetailsWrapper> billDetailsWrapper = new ArrayList<>(); //for collection-service
	
	private AuditDetails auditDetails;

}
