/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) <2015>  eGovernments Foundation
 *
 *     The updated version of eGov suite of products as by eGovernments Foundation
 *     is available at http://www.egovernments.org
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program. If not, see http://www.gnu.org/licenses/ or
 *     http://www.gnu.org/licenses/gpl.html .
 *
 *     In addition to the terms of the GPL license to be adhered to in using this
 *     program, the following additional terms are to be complied with:
 *
 *         1) All versions of this program, verbatim or modified must carry this
 *            Legal Notice.
 *
 *         2) Any misrepresentation of the origin of the material is prohibited. It
 *            is required that all modified versions of this material be marked in
 *            reasonable ways as different from the original version.
 *
 *         3) This license does not grant any rights to any user of the program
 *            with regards to rights under trademark law for use of the trade names
 *            or trademarks of eGovernments Foundation.
 *
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */

package org.egov.workflow.persistence.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "egpgr_router")
@SequenceGenerator(name = ComplaintRouter.SEQ_COMPLAINTROUTER, sequenceName = ComplaintRouter.SEQ_COMPLAINTROUTER, allocationSize = 1)
public class ComplaintRouter extends AbstractAuditable<Long> {

	private static final long serialVersionUID = 5691022600220045218L;
	
	public static final String SEQ_COMPLAINTROUTER = "SEQ_EGPGR_ROUTER";

	@Id
	@GeneratedValue(generator = SEQ_COMPLAINTROUTER, strategy = GenerationType.SEQUENCE)
	private Long id;

	@Column(name = "complainttypeid")
	private Long complaintType;

	@Column(name = "bndryid")
	private Long boundary;

	@NotNull
	@Column(name = "position")
	private Long position;
	
    @Column(name = "tenantid")
    private String tenantId;

	public String getTenantId() {
		return tenantId;
	}

	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}
	
	public Long getId() {
		return id;
	}

	public void setId(final Long id) {
		this.id = id;
	}

	public Long getComplaintType() {
		return complaintType;
	}

	public void setComplaintType(final Long complaintType) {
		this.complaintType = complaintType;
	}

	public Long getPosition() {
		return position;
	}

	public void setPosition(final Long position) {
		this.position = position;
	}

	public Long getBoundary() {
		return boundary;
	}

	public void setBoundary(final Long boundary) {
		this.boundary = boundary;
	}
}