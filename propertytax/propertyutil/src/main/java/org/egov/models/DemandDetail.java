package org.egov.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

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
