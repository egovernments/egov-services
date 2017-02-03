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

package org.egov.workflow.repository;

import java.util.List;

import org.egov.workflow.entity.ComplaintRouter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ComplaintRouterRepository extends JpaRepository<ComplaintRouter, Long> {

	@Query("select cr from ComplaintRouter cr where cr.complaintType=:complaintType and cr.boundary=:boundary")
	public ComplaintRouter findByComplaintTypeAndBoundary(@Param("complaintType") Long complaintType,
			@Param("boundary") Long boundary);

	@Query("select cr from ComplaintRouter cr where cr.complaintType=:complaintType and cr.boundary is null")
	public ComplaintRouter findByOnlyComplaintType(@Param("complaintType") Long complaintType);

	@Query("select cr from ComplaintRouter cr where cr.complaintType=:complaintTypeId ")
	List<ComplaintRouter> findRoutersByComplaintType(@Param("complaintTypeId") Long complaintTypeId);

	@Query("select cr from ComplaintRouter cr where cr.position=:positionId and cr.complaintType=:complaintTypeId ")
	public List<ComplaintRouter> findRoutersByComplaintTypeAndPosition(@Param("complaintTypeId") Long complaintTypeId,
			@Param("positionId") Long positionId);

	@Query("select cr from ComplaintRouter cr where cr.position=:positionId and cr.complaintType=:complaintTypeId  and cr.boundary=:boundaryId")
	public List<ComplaintRouter> findRoutersByComplaintTypeAndBoundaryAndPosition(
			@Param("complaintTypeId") Long complaintTypeId, @Param("boundaryId") Long boundaryId,
			@Param("positionId") Long positionId);

	@Query("select cr from ComplaintRouter cr where  cr.complaintType=:complaintTypeId and  cr.boundary=:boundaryId")
	public List<ComplaintRouter> findRoutersByComplaintTypeAndBoundary(@Param("complaintTypeId") Long complaintTypeId,
			@Param("boundaryId") Long boundaryId);

	@Query("select cr from ComplaintRouter cr where cr.position=:positionId ")
	public List<ComplaintRouter> findRoutersByPosition(@Param("positionId") Long positionId);

	@Query("select cr from ComplaintRouter cr where cr.position=:positionId and cr.boundary=:boundaryId")
	public List<ComplaintRouter> findRoutersByBoundaryAndPosition(@Param("boundaryId") Long boundaryId,
			@Param("positionId") Long positionId);

	@Query("select cr from ComplaintRouter cr where  cr.boundary=:boundaryId")
	public List<ComplaintRouter> findRoutersByBoundary(@Param("boundaryId") Long boundaryId);

	@Query("select cr from ComplaintRouter cr where cr.boundary=:bndry and cr.complaintType is null")
	public ComplaintRouter findByOnlyBoundary(@Param("bndry") Long bndry);

	@Query(value = "select cr from egpgr_router cr eg_boundary boundary , eg_boundary_type boundarytype ,eg_hierarchy_type hierarchytype where boundary.boundarytype = boundarytype.id and hierarchytype.id = boundarytype.hierarchytype and cr.boundary = boundary.id and boundary.parent is null and cr.complainttype is null and hierarchytype.name=:hierarchyType", nativeQuery = true)
	public ComplaintRouter findCityAdminGrievanceOfficer(@Param("hierarchyType") String hierarchyType);

}