package org.egov.demand.model;

import org.egov.demand.model.enums.InstallmentType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Installment {
	
	  private Long id;

	  private Long fromDate;

	  private Long toDate;

	  private String module;

	  private Long installmentNumber;

	  private String description;

	  private InstallmentType installmentType;

	  private String financialYear;

	  private InstallmentAuditDetail auditDetails;

	  private String tenantId;
}
