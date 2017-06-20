package org.egov.demand.model;

import org.egov.demand.model.enums.Category;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaxHeadMaster {
		
		  private Long id;

		  private String tenantId;

		  private Category category;

		  private String service;

		  private String name;

		  private String code;

		  private String glCode;

		  private Boolean isDebit = false;

		  private Boolean isActualDemand;
		  
		  private TaxPeriod taxPeriod = null;

		  private AuditDetail auditDetail;
}
