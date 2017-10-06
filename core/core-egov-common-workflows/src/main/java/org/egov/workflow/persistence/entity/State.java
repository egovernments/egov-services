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
import java.util.HashSet;
import java.util.Set;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotNull;

import org.egov.workflow.persistence.entity.Enum.StateStatus;
import org.egov.workflow.web.contract.Attribute;
import org.egov.workflow.web.contract.Position;
import org.egov.workflow.web.contract.ProcessInstance;
import org.egov.workflow.web.contract.Task;
import org.hibernate.validator.constraints.Length;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@AllArgsConstructor
@Getter
@Setter

public class State extends AuditableEntity {

	public static final String DEFAULT_STATE_VALUE_CREATED = "Created";
	public static final String DEFAULT_STATE_VALUE_CLOSED = "Closed";
	public static final String STATE_REOPENED = "Reopened";
	static final String SEQ_STATE = "SEQ_EG_WF_STATES";
	public static final String TABLE_NAME = "eg_wf_states";
	public static final String SEQUENCE_NAME = "seq_eg_wf_states";
	public static final String ALIAS = "states";

	private Long id;

	@NotNull
	@Length(max = 128)
	private String type;

	@NotNull
	@Length(min = 1, max = 256)
	private String value;
	private Long owner_pos;
	private Long owner_user;
	private Set<StateHistory> history = new HashSet<>();
	private String senderName;
	private String nextAction;
	private String comments;
	private String natureOfTask;
	private String extraInfo;
	private Date dateInfo;
	private Date extraDateInfo;
	@NotNull
	private Integer status;
	private Long initiator_pos;
	private String myLinkId;
	private String tenantId;

	public State() {
	}

	public void addStateHistory(final StateHistory history) {
		getHistory().add(history);
	}
	public boolean isNew() {
		return status.equals(StateStatus.STARTED);
	}

	public boolean isEnded() {
		return status.equals(StateStatus.ENDED);
	}



	public Task map() {
		Position p = Position.builder().id(this.getOwner_pos()).build();
		Task t = Task.builder().businessKey(this.type).comments(this.comments == null ? "" : this.comments)
				.createdDate(this.getCreatedDate()).id(this.getId().toString()).state(this.getStatus().toString())
				.status(this.getValue()).natureOfTask(this.getNatureOfTask()).owner(p)
				.details(this.extraInfo == null ? "" : this.extraInfo)
				.senderName(this.senderName == null ? "" : this.senderName)
				.action(this.nextAction == null ? "" : this.nextAction).attributes(new HashMap<String, Attribute>())
				.url(this.myLinkId == null ? "" : this.myLinkId.replace(":ID", this.getId().toString())).build();
		return t;
	}

	public ProcessInstance mapToProcess(ProcessInstance pi) {
		Position p = Position.builder().id(this.getOwner_pos()).build();
		pi = pi.builder().businessKey(this.type)
				.comments(this.comments == null ? "" : this.comments)
				.createdDate(this.getCreatedDate())
				.id(this.getId().toString())
				.status(this.getValue()).owner(p)
				.senderName(this.senderName == null ? "" : this.senderName)
				.attributes(new HashMap<String, Attribute>())
				.build();
		return pi;
	}
	/*public void test(ProcessInstance p) {
		State s=new State();
		this.id=setId(Long.parseLong(p.getId()));
	  }
	*/

}
