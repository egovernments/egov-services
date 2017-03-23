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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import org.egov.eis.config.ApplicationProperties;
import org.egov.eis.web.contract.AttendanceGetRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AttendanceQueryBuilder {

    private static final Logger logger = LoggerFactory.getLogger(AttendanceQueryBuilder.class);

    @Autowired
    private ApplicationProperties applicationProperties;

    private static final String BASE_QUERY = "SELECT a.id AS a_id, a.attendancedate AS a_date, a.employee AS "
            + "a_employee, a.month AS a_month, a.year AS a_year, a.type AS a_type, a.remarks AS "
            + "a_remarks, a.createdby AS a_createdby, a.createddate AS a_createddate, a.lastmodifiedby"
            + " AS a_lastmodifiedby, a.lastmodifieddate AS a_lastmodifieddate, a.tenantId AS a_tenantId, "
            + "t.id AS t_id, t.code AS t_code, t.description AS t_description"
            + " FROM egeis_attendance AS a JOIN egeis_attendance_type AS t ON a.type = t.id";

    @SuppressWarnings("rawtypes")
    public String getQuery(final AttendanceGetRequest attendanceGetRequest, final List preparedStatementValues)
            throws ParseException {
        final StringBuilder selectQuery = new StringBuilder(BASE_QUERY);

        addWhereClause(selectQuery, preparedStatementValues, attendanceGetRequest);
        addOrderByClause(selectQuery, attendanceGetRequest);
        addPagingClause(selectQuery, preparedStatementValues, attendanceGetRequest);

        logger.debug("Query : " + selectQuery);
        return selectQuery.toString();
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private void addWhereClause(final StringBuilder selectQuery, final List preparedStatementValues,
            final AttendanceGetRequest attendanceGetRequest) throws ParseException {

        if (attendanceGetRequest.getId() == null && attendanceGetRequest.getApplicableOn() == null
                && attendanceGetRequest.getCode() == null && attendanceGetRequest.getMonth() == null
                && attendanceGetRequest.getTenantId() == null && attendanceGetRequest.getYear() == null
                && attendanceGetRequest.getDepartmentId() == null && attendanceGetRequest.getDesignationId() == null)
            return;

        selectQuery.append(" WHERE");
        boolean isAppendAndClause = false;

        if (attendanceGetRequest.getTenantId() != null) {
            isAppendAndClause = true;
            selectQuery.append(" tenantId = ?");
            preparedStatementValues.add(attendanceGetRequest.getTenantId());
        }

        if (attendanceGetRequest.getId() != null) {
            isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
            selectQuery.append(" a.id IN " + getIdQuery(attendanceGetRequest.getId()));
        }

        if (attendanceGetRequest.getApplicableOn() != null) {
            final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
            selectQuery.append(" attendancedate = ?");
            preparedStatementValues.add(sdf.parse(attendanceGetRequest.getApplicableOn()));
        }

        if (attendanceGetRequest.getMonth() != null) {
            isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
            selectQuery.append(" month = ?");
            preparedStatementValues.add(attendanceGetRequest.getMonth());
        }

        if (attendanceGetRequest.getYear() != null) {
            isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
            selectQuery.append(" year = ?");
            preparedStatementValues.add(attendanceGetRequest.getYear());
        }

        if (attendanceGetRequest.getDesignationId() != null) {
            isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
            selectQuery
                    .append(" employee in (select employee from egeis_assignment where designationId = ? and isprimary = 't')");
            preparedStatementValues.add(attendanceGetRequest.getDesignationId());
        }

        if (attendanceGetRequest.getDepartmentId() != null) {
            isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
            selectQuery.append(" employee in (select employee from egeis_assignment where departmentId = ? and isprimary = 't')");
            preparedStatementValues.add(attendanceGetRequest.getDepartmentId());
        }

        if (attendanceGetRequest.getCode() != null) {
            isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
            selectQuery.append(" employee = (select id from egeis_employee where upper(code) = ?)");
            preparedStatementValues.add(attendanceGetRequest.getCode().toUpperCase());
        }
    }

    private void addOrderByClause(final StringBuilder selectQuery,
            final AttendanceGetRequest attendanceGetRequest) {
        final String sortBy = attendanceGetRequest.getSortBy() == null ? "attendancedate"
                : attendanceGetRequest.getSortBy();
        final String sortOrder = attendanceGetRequest.getSortOrder() == null ? "ASC"
                : attendanceGetRequest.getSortOrder();
        selectQuery.append(" ORDER BY " + sortBy + " " + sortOrder);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private void addPagingClause(final StringBuilder selectQuery, final List preparedStatementValues,
            final AttendanceGetRequest attendanceGetRequest) {
        // handle limit(also called pageSize) here
        selectQuery.append(" LIMIT ?");
        long pageSize = Integer.parseInt(applicationProperties.attendanceSearchPageSizeDefault());
        if (attendanceGetRequest.getPageSize() != null)
            pageSize = attendanceGetRequest.getPageSize();
        preparedStatementValues.add(pageSize); // Set limit to pageSize

        // handle offset here
        selectQuery.append(" OFFSET ?");
        int pageNumber = 0; // Default pageNo is zero meaning first page
        if (attendanceGetRequest.getPageNumber() != null)
            pageNumber = attendanceGetRequest.getPageNumber() - 1;
        preparedStatementValues.add(pageNumber * pageSize); // Set offset to pageNo * pageSize
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

    public static String insertAttendanceQuery() {
        return "INSERT INTO egeis_attendance values "
                + "(nextval('seq_egeis_attendance'),?,?,?,?,?,?,?,?,?,?,?)";
    }

    public static String updateAttendanceQuery() {
        return "UPDATE egeis_attendance SET attendancedate = ?, employee = ?,"
                + " month = ?, year = ?, type = ?, remarks = ?, lastmodifiedby = ?, lastmodifieddate = ?,"
                + " tenantid = ? where id = ? ";
    }

    public static String selectTypeByCodeQuery() {
        return "select * from egeis_attendance_type where upper(code) = ?";
    }

    public static String selectAttendanceTypeQuery() {
        return "select * from egeis_attendance_type";
    }

    public static String selectAttendanceByEmployeeAndDateQuery() {
        return "SELECT id FROM egeis_attendance where employee = ? and attendancedate = ? and tenantId = ?";
    }
}
