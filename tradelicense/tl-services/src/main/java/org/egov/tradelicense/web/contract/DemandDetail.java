package org.egov.tradelicense.web.contract;

import java.math.BigDecimal;

import javax.validation.constraints.NotNull;

import org.egov.tl.commons.web.contract.AuditDetails;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DemandDetail {

	private String id;
	
	private String demandId;	

	@NotNull
	private String taxHeadMasterCode;

	@NotNull
	private BigDecimal taxAmount;
	
	@NotNull
	private BigDecimal collectionAmount = BigDecimal.ZERO;

	private AuditDetails auditDetail;

	private String tenantId;
}
