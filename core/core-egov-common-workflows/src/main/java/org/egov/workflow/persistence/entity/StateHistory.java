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

import java.util.Date;
import java.util.HashMap;

import javax.validation.constraints.NotNull;

import org.egov.workflow.web.contract.Attribute;
import org.egov.workflow.web.contract.Position;
import org.egov.workflow.web.contract.Task;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@AllArgsConstructor
@Getter
@Setter
public class StateHistory extends AuditableEntity {
	static final String SEQ_STATEHISTORY = "SEQ_EG_WF_STATE_HISTORY";
	public static final String TABLE_NAME = "eg_wf_state_history";
	public static final String SEQUENCE_NAME = "seq_eg_wf_states_history";
	public static final String ALIAS = "states";
	private Long id;

	private Long createdBy;

	private Date createdDate;

	private Long lastModifiedBy;

	private Date lastModifiedDate;

	private State state;

	@NotNull
	private String value;

	private Long ownerPosition;

	private Long ownerUser;

	private String senderName;
	private String nextAction;
	private String comments;
	private String natureOfTask;
	private String extraInfo;
	private Date dateInfo;
	private Date extraDateInfo;
	private String tenantId;
	private Long initiatorPosition;

	StateHistory() {
	}

	public StateHistory(final State state) {
		this.state = state;
		createdBy = state.getCreatedBy();
		createdDate = state.getCreatedDate() != null ? state.getCreatedDate() : null;
		lastModifiedBy = state.getLastModifiedBy();
		lastModifiedDate = state.getLastModifiedDate() != null ? state.getLastModifiedDate() : null;
		value = state.getValue();
		ownerPosition = state.getOwner_pos();
		ownerUser = state.getOwner_user();
		senderName = state.getSenderName();
		nextAction = state.getNextAction();
		comments = state.getComments();
		extraInfo = state.getExtraInfo();
		dateInfo = state.getDateInfo();
		extraDateInfo = state.getExtraDateInfo();
		natureOfTask = state.getNatureOfTask();
		initiatorPosition = state.getInitiator_pos();
		tenantId = state.getTenantId();
	}

	public Task map() {
		Position p = Position.builder().id(this.ownerPosition).build();;
		Task t = Task.builder().businessKey(this.getState().getType())
				.comments(this.comments == null ? "" : this.comments).createdDate(this.getCreatedDate())
				.id(this.getId().toString()).status(this.getValue()).natureOfTask(this.getNatureOfTask()).owner(p)
				.details(this.extraInfo == null ? "" : this.extraInfo)
				.senderName(this.senderName == null ? "" : this.senderName)
				.action(this.nextAction == null ? "" : this.nextAction).attributes(new HashMap<String, Attribute>())
				.build();
		return t;

	}
	
}
