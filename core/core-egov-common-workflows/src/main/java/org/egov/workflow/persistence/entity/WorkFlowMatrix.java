/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 * accountability and the service delivery of the government  organizations.
 *
 *  Copyright (C) 2016  eGovernments Foundation
 *
 *  The updated version of eGov suite of products as by eGovernments Foundation
 *  is available at http://www.egovernments.org
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program. If not, see http://www.gnu.org/licenses/ or
 *  http://www.gnu.org/licenses/gpl.html .
 *
 *  In addition to the terms of the GPL license to be adhered to in using this
 *  program, the following additional terms are to be complied with:
 *
 *      1) All versions of this program, verbatim or modified must carry this
 *         Legal Notice.
 *
 *      2) Any misrepresentation of the origin of the material is prohibited. It
 *         is required that all modified versions of this material be marked in
 *         reasonable ways as different from the original version.
 *
 *      3) This license does not grant any rights to any user of the program
 *         with regards to rights under trademark law for use of the trade names
 *         or trademarks of eGovernments Foundation.
 *
 *  In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */

package org.egov.workflow.persistence.entity;

import java.math.BigDecimal;
import java.util.Date;

import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
public class WorkFlowMatrix extends AuditableEntity{

	public static final String SEQ_WF_MATRIX = "SEQ_EG_WF_MATRIX";
    public static final String TABLE_NAME = "eg_wf_matrix";
	public static final String SEQUENCE_NAME = "seq_eg_wf_matrix";
	public static final String ALIAS = "states";
	private Long id;

	private String department;

	@NotNull
	private String objectType;

	private String currentState;

	private String currentStatus;

	private String pendingActions;

	private String currentDesignation;

	private String additionalRule;

	private String nextState;

	private String nextAction;

	private String nextDesignation;

	private String nextStatus;

	private String validActions;

	private BigDecimal fromQty;

	private BigDecimal toQty;

	private Date fromDate;

	private Date toDate;

	private String tenantId;

	public WorkFlowMatrix(final String department, final String objectType, final String currentState,
			final String currentStatus, final String pendingActions, final String currentDesignation,
			final String additionalRule, final String nextState, final String nextAction, final String nextDesignation,
			final String nextStatus, final String validActions, final BigDecimal fromQty, final BigDecimal toQty,
			final Date fromDate, final Date toDate) {
		super();
		this.department = department;
		this.objectType = objectType;
		this.currentState = currentState;
		this.currentStatus = currentStatus;
		this.pendingActions = pendingActions;
		this.currentDesignation = currentDesignation;
		this.additionalRule = additionalRule;
		this.nextState = nextState;
		this.nextAction = nextAction;
		this.nextDesignation = nextDesignation;
		this.nextStatus = nextStatus;
		this.validActions = validActions;
		this.fromQty = fromQty;
		this.toQty = toQty;
		this.fromDate = fromDate;
		this.toDate = toDate;
	}

	@Override
	public WorkFlowMatrix clone() {
		return new WorkFlowMatrix(department, objectType, currentState, currentStatus, pendingActions,
				currentDesignation, additionalRule, nextState, nextAction, nextDesignation, nextStatus, validActions,
				fromQty, toQty, fromDate, toDate);
	}
	


}