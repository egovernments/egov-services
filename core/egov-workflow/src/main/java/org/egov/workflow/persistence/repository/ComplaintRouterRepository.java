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

package org.egov.workflow.persistence.repository;

import org.egov.workflow.persistence.entity.ComplaintRouter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ComplaintRouterRepository extends JpaRepository<ComplaintRouter, Long> {

	@Query("select cr from ComplaintRouter cr where cr.complaintType=:complaintType and cr.boundary=:boundary")
	ComplaintRouter findByComplaintTypeAndBoundary(@Param("complaintType") Long complaintType,
												   @Param("boundary") Long boundary);

	@Query("select cr from ComplaintRouter cr where cr.complaintType=:complaintType and cr.boundary is null")
	ComplaintRouter findByOnlyComplaintType(@Param("complaintType") Long complaintType);

	@Query("select cr from ComplaintRouter cr where cr.boundary=:bndry and cr.complaintType is null")
	ComplaintRouter findByOnlyBoundary(@Param("bndry") Long bndry);

	@Query(value = "select cr.* from egpgr_router cr, eg_boundary boundary , eg_boundary_type boundarytype ,eg_hierarchy_type hierarchytype where boundary.boundarytype = boundarytype.id and hierarchytype.id = boundarytype.hierarchytype and cr.bndryid = boundary.id and boundary.parent is null and cr.complainttypeid is null and hierarchytype.name=:hierarchyType and cr.tenantid=:tenantId", nativeQuery = true)
	ComplaintRouter findCityAdminGrievanceOfficer(@Param("hierarchyType") String hierarchyType ,@Param("tenantId") String tenantId);
}