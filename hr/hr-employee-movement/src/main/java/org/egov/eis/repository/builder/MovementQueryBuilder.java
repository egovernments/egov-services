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

package org.egov.eis.repository.builder;

import java.util.List;

import org.egov.eis.config.ApplicationProperties;
import org.egov.eis.web.contract.MovementSearchRequest;
import org.egov.eis.web.contract.RequestInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MovementQueryBuilder {

    private static final Logger logger = LoggerFactory.getLogger(MovementQueryBuilder.class);

    @Autowired
    private ApplicationProperties applicationProperties;

    public static final String GENERATE_SEQUENCE_QUERY = "SELECT nextval('seq_egeis_movement') AS id" ;

    private static final String BASE_QUERY = "SELECT m.id AS m_id,"
            + " m.employee AS m_employee, m.typeOfMovement AS m_typeOfMovement, m.currentAssignment AS m_currentAssignment,"
            + " m.transferType AS m_transferType, m.remarks AS m_remarks,"
            + " m.promotionBasis AS m_promotionBasis, m.reason AS m_reason, "
            + " m.effectiveFrom AS m_effectiveFrom, m.departmentAssigned AS m_departmentAssigned,"
            + " m.transferedLocation AS m_transferedLocation, m.enquiryPassedDate AS m_enquiryPassedDate,"
            + " m.designationAssigned AS m_designationAssigned, m.positionAssigned AS m_positionAssigned,"
            + " m.fundAssigned AS m_fundAssigned, m.functionAssigned AS m_functionAssigned,"
            + " m.employeeAcceptance AS m_employeeAcceptance,"
            + " m.status AS m_status, m.stateId AS m_stateId, m.createdBy AS m_createdBy,"
            + " m.createdDate AS m_createdDate, m.lastModifiedBy AS m_lastModifiedBy,"
            + " m.lastModifiedDate AS m_lastModifiedDate, m.tenantId AS m_tenantId"
            + " FROM egeis_movement m";

    @SuppressWarnings("rawtypes")
    public String getQuery(final MovementSearchRequest movementSearchRequest,
            final List preparedStatementValues, final RequestInfo requestInfo) {
        final StringBuilder selectQuery = new StringBuilder(BASE_QUERY);

        addWhereClause(selectQuery, preparedStatementValues, movementSearchRequest);
        addOrderByClause(selectQuery, movementSearchRequest);
        addPagingClause(selectQuery, preparedStatementValues, movementSearchRequest);

        logger.debug("Query : " + selectQuery);
        return selectQuery.toString();
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private void addWhereClause(final StringBuilder selectQuery, final List preparedStatementValues,
            final MovementSearchRequest movementSearchRequest) {

        if (movementSearchRequest.getId() == null && movementSearchRequest.getEmployeeId() == null
                && movementSearchRequest.getTypeOfmovement() == null && movementSearchRequest.getEffectiveFromDate() == null
                && movementSearchRequest.getEffectiveToDate() == null && movementSearchRequest.getStateId() == null
                && movementSearchRequest.getTenantId() == null)
            return;

        selectQuery.append(" WHERE");
        boolean isAppendAndClause = false;

        if (movementSearchRequest.getTenantId() != null) {
            isAppendAndClause = true;
            selectQuery.append(" m.tenantId = ?");
            preparedStatementValues.add(movementSearchRequest.getTenantId());
        }

        if (movementSearchRequest.getId() != null && !movementSearchRequest.getId().isEmpty()) {
            isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
            selectQuery.append(" m.id IN " + getIdQuery(movementSearchRequest.getId()));
        }

        if (movementSearchRequest.getEmployeeId() != null) {
            isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
            selectQuery.append(" m.employee = ?");
            preparedStatementValues.add(movementSearchRequest.getEmployeeId());
        }

        if (movementSearchRequest.getTypeOfmovement() != null) {
            isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
            selectQuery.append(" m.typeOfMovement = ?");
            preparedStatementValues.add(movementSearchRequest.getTypeOfmovement());
        }

        if (movementSearchRequest.getEffectiveFromDate() != null) {
            isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
            selectQuery.append(" m.effectiveFrom >= ?");
            preparedStatementValues.add(movementSearchRequest.getEffectiveFromDate());
        }

        if (movementSearchRequest.getEffectiveToDate() != null) {
            isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
            selectQuery.append(" m.effectiveFrom <= ?");
            preparedStatementValues.add(movementSearchRequest.getEffectiveToDate());
        }
        if (movementSearchRequest.getStateId() != null) {
            isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
            selectQuery.append(" m.stateId = ?");
            preparedStatementValues.add(movementSearchRequest.getStateId());
        }
        if (movementSearchRequest.getStatus() != null) {
            isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
            selectQuery.append(" m.status IN( " + movementSearchRequest.getStatus() + ")");
        }

    }

    private void addOrderByClause(final StringBuilder selectQuery,
            final MovementSearchRequest movementSearchRequest) {
        final String sortBy = movementSearchRequest.getSortBy() == null ? "m.id"
                : movementSearchRequest.getSortBy();
        final String sortOrder = movementSearchRequest.getSortOrder() == null ? "ASC"
                : movementSearchRequest.getSortOrder();
        selectQuery.append(" ORDER BY " + sortBy + " " + sortOrder);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private void addPagingClause(final StringBuilder selectQuery, final List preparedStatementValues,
            final MovementSearchRequest movementSearchRequest) {
        // handle limit(also called pageSize) here
        selectQuery.append(" LIMIT ?");
        long pageSize = Integer.parseInt(applicationProperties.employeeMovementSearchPageSizeDefault());
        if (movementSearchRequest.getPageSize() != null)
            pageSize = movementSearchRequest.getPageSize();
        preparedStatementValues.add(pageSize); // Set limit to pageSize

        // handle offset here
        selectQuery.append(" OFFSET ?");
        int pageNumber = 0; // Default pageNo is zero meaning first page
        if (movementSearchRequest.getPageNumber() != null)
            pageNumber = movementSearchRequest.getPageNumber() - 1;
        preparedStatementValues.add(pageNumber * pageSize); // Set offset to
                                                            // pageNo * pageSize
    }

    /**
     * This method is always called at the beginning of the method so that and is prepended before the field's predicate is
     * handled.
     *
     * @param appendAndClauseFlag
     * @param queryString
     * @return boolean indicates if the next predicate should append an "AND"
     */
    private boolean addAndClauseIfRequired(final boolean appendAndClauseFlag, final StringBuilder queryString) {
        if (appendAndClauseFlag)
            queryString.append(" AND");

        return true;
    }

    private static String getIdQuery(final List<Long> idList) {
        final StringBuilder query = new StringBuilder("(");
        if (idList.size() >= 1) {
            query.append(idList.get(0).toString());
            for (int i = 1; i < idList.size(); i++)
                query.append(", " + idList.get(i));
        }
        return query.append(")").toString();
    }

    public static String insertMovementQuery() {
        return "INSERT INTO egeis_movement(id, employee, typeofmovement,"
                + " currentassignment, transfertype, promotionbasis, remarks, reason,"
                + " effectivefrom, enquiryPassedDate, transferedLocation, departmentassigned, designationassigned, positionassigned,"
                + " fundassigned, functionassigned, employeeacceptance, status,"
                + " stateid, createdby, createddate, lastmodifiedby, lastmodifieddate, tenantid)"
                + " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,"
                + " ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    }

    public static String updateMovementQuery() {
        return "UPDATE egeis_movement SET employee=?, typeofmovement=?,"
                + " currentassignment=?, transfertype=?, promotionbasis=?, remarks=?, reason=?,"
                + " effectivefrom=?, enquiryPassedDate=?, transferedLocation=?, departmentassigned=?, designationassigned=?, positionassigned=?,"
                + " fundassigned=?, functionassigned=?, employeeacceptance=?, status=?,"
                + " stateid=?, createdby=?, createddate=?, lastmodifiedby=?, lastmodifieddate=? WHERE id=? and tenantid=?";
    }
}
