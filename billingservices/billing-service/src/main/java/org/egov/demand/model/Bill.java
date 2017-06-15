package org.egov.demand.model;

import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Bill {

	private Long id;

	private String payeeName;

	private String payeeAddress;

	private String payeeEmail;

	private Boolean isActive;

	private Boolean isCancelled;

	private List<BillDetail> billDetails = new ArrayList<>();

	private String tenantId;

	private AuditDetail auditDetail;
}