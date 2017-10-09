package org.egov.workflow.persistence.entity;

import java.math.BigDecimal;
import java.util.Date;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
@Builder
@Getter
@Setter
public class WorkFlowMatrixSearchCriteria {
	public String type;
	public String department;
	public BigDecimal amountRule;
	public String additionalRule;
	public String currentState;
	public String pendingActions;
	public String tenantId;
	public Date date;
	public String designation;
	

}
