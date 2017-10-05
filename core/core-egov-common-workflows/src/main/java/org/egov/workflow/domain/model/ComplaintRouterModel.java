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

package org.egov.workflow.domain.model;

import javax.validation.constraints.NotNull;

import org.egov.workflow.domain.model.ComplaintRouterModel;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ComplaintRouterModel extends Auditable {

	public static final String SEQ_COMPLAINTROUTER = "SEQ_EGPGR_ROUTER";
	static final String SEQ_STATE = "SEQ_EGPGR_ROUTER";
	public static final String TABLE_NAME = "egpgr_router";
	public static final String SEQUENCE_NAME = "seq_egpgr__router";
	public static final String ALIAS = "router";

	private Long id;

	private Long complaintType;

	private Long boundary;

	@NotNull
	private Long position;
	
	public ComplaintRouterModel toDomain() {
		ComplaintRouterModel router = new ComplaintRouterModel();
		router.setId(this.id);
		router.setComplaintType(this.complaintType);
		router.setBoundary(this.boundary);
		router.setPosition(this.position);
		router.setTenantId(this.tenantId);
		
		return router;

	}

	public ComplaintRouterModel toEntity(ComplaintRouterModel router) {

		this.id = router.getId();
		this.complaintType = router.getComplaintType();
		this.boundary = router.getBoundary();
		this.position = router.getPosition();
		this.tenantId = router.getTenantId();
		
		return this;

	}
}