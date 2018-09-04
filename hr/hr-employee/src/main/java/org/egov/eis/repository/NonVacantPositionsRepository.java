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

package org.egov.eis.repository;

import org.egov.eis.model.Assignment;
import org.egov.eis.web.contract.NonVacantPositionsGetRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class NonVacantPositionsRepository {

    public static final String SEARCH_ASSIGNMENT_FOR_POSITION_IDS_QUERY = "SELECT DISTINCT positionId"
            + " FROM egeis_assignment"
            + " WHERE departmentId = ? AND designationId = ? AND ? BETWEEN fromDate AND toDate AND tenantId = ?";

    public static final String SEARCH_ASSIGNED_POSITION_FOR_DATERANGE_QUERY = "SELECT DISTINCT positionId"
            + " FROM egeis_assignment"
            + " WHERE departmentId = ? AND designationId = ? AND ((fromdate <= ? and todate <= ?)"
            + " OR (fromdate >= ? and todate >= ?) OR (fromdate >= ? and todate <= ?) OR"
            + " (fromdate <= ? and todate >= ?)) AND tenantId = ?";

    public static final String CHECK_IF_POSITION_IS_OCCUPIED_QUERY = "SELECT exists(SELECT positionId"
            + " FROM egeis_assignment"
            + " WHERE positionId = :positionId AND departmentId = :departmentId AND designationId = :designationId"
            + " AND (:fromDate BETWEEN fromDate AND toDate OR :toDate BETWEEN fromDate AND toDate"
            + " OR fromDate BETWEEN :fromDate AND :toDate OR toDate BETWEEN :fromDate AND :toDate)"
            + " AND isPrimary = true AND tenantId = :tenantId $employeeIdCheck)";

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public List<Long> findForCriteria(NonVacantPositionsGetRequest nonVacantPositionsGetRequest) {
        Object[] searchConditions = {nonVacantPositionsGetRequest.getDepartmentId(),
                nonVacantPositionsGetRequest.getDesignationId(),
                nonVacantPositionsGetRequest.getAsOnDate(),
                nonVacantPositionsGetRequest.getTenantId()};

        List<Long> positionIds = jdbcTemplate.queryForList(SEARCH_ASSIGNMENT_FOR_POSITION_IDS_QUERY,
                searchConditions, Long.class);

        return positionIds;
    }

    public List<Long> findForPositionCriteria(NonVacantPositionsGetRequest nonVacantPositionsGetRequest) {
        Map<String, Object> namedParameters = new HashMap<String, Object>() {{
            put("departmentId", nonVacantPositionsGetRequest.getDepartmentId());
            put("designationId", nonVacantPositionsGetRequest.getDesignationId());
            put("fromDate", nonVacantPositionsGetRequest.getFromDate());
            put("toDate", nonVacantPositionsGetRequest.getToDate());
            put("tenantId", nonVacantPositionsGetRequest.getTenantId());
        }};

        List<Long> positionIds = namedParameterJdbcTemplate.queryForList(SEARCH_ASSIGNED_POSITION_FOR_DATERANGE_QUERY,
                namedParameters, Long.class);

        return positionIds;
    }

    public Boolean checkIfPositionIsOccupied(Assignment assignment, Long employeeId, String tenantId, String requestType) {
        Map<String, Object> namedParameters = new HashMap<String, Object>() {{
            put("departmentId", assignment.getDepartment());
            put("designationId", assignment.getDesignation());
            put("positionId", assignment.getPosition());
            put("fromDate", assignment.getFromDate());
            put("toDate", assignment.getToDate());
            put("tenantId", tenantId);
            put("employeeId", employeeId);
        }};

        if (requestType.equals("create")) {
            Boolean aBoolean = namedParameterJdbcTemplate.queryForObject(
                    CHECK_IF_POSITION_IS_OCCUPIED_QUERY.replace(" $employeeIdCheck", ""),
                    namedParameters, Boolean.class);
            return aBoolean;
        } else {
            return namedParameterJdbcTemplate.queryForObject(CHECK_IF_POSITION_IS_OCCUPIED_QUERY.replace(
                    " $employeeIdCheck", " AND employeeId != :employeeId"),
                    namedParameters, Boolean.class);
        }
    }
}