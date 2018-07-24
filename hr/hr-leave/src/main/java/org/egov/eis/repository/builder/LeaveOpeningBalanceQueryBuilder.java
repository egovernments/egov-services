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
import org.egov.eis.web.contract.LeaveOpeningBalanceGetRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class LeaveOpeningBalanceQueryBuilder {

	private static final Logger logger = LoggerFactory.getLogger(LeaveOpeningBalanceQueryBuilder.class);

	@Autowired
	private ApplicationProperties applicationProperties;

	private static final String BASE_QUERY = "SELECT lob.id AS lob_id, lob.employeeId AS lob_employeeId,"
			+ " lob.calendarYear AS lob_calendarYear, lob.noOfDays AS lob_noOfDays, lob.createdBy AS lob_createdBy,"
			+ " lob.createdDate AS lob_createdDate, lob.lastModifiedBy AS lob_lastModifiedBy,"
			+ " lob.lastModifiedDate AS lob_lastModifiedDate, lob.tenantId AS lob_tenantId,"
			+ " lt.id AS lt_id, lt.name AS lt_name, lt.description AS lt_description,"
			+ " lt.halfdayAllowed AS lt_halfdayAllowed, lt.payEligible AS lt_payEligible,"
			+ " lt.accumulative AS lt_accumulative, lt.encashable AS lt_encashable,"
			+ " lt.active AS lt_active, lt.createdBy AS lt_createdBy, lt.createdDate AS lt_createdDate,"
			+ " lt.lastModifiedBy AS lt_lastModifiedBy, lt.lastModifiedDate AS lt_lastModifiedDate"
			+ " FROM egeis_leaveOpeningBalance lob" + " JOIN egeis_leaveType lt ON lob.leaveTypeId = lt.id";

	public static String selectLeaveOpeningBalanceQuery() {
		return " select * from egeis_leaveopeningbalance where employeeid=? leavetypeid=? calendaryear=? tenantid=?";
	}

	@SuppressWarnings("rawtypes")
	public String getQuery(LeaveOpeningBalanceGetRequest leaveOpeningBalanceGetRequest, List preparedStatementValues) {
		StringBuilder selectQuery = new StringBuilder(BASE_QUERY);

		addWhereClause(selectQuery, preparedStatementValues, leaveOpeningBalanceGetRequest);
		addOrderByClause(selectQuery, leaveOpeningBalanceGetRequest);
		addPagingClause(selectQuery, preparedStatementValues, leaveOpeningBalanceGetRequest);

		logger.debug("Query : " + selectQuery);
		return selectQuery.toString();
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void addWhereClause(StringBuilder selectQuery, List preparedStatementValues,
								LeaveOpeningBalanceGetRequest leaveOpeningBalanceGetRequest) {

		if (leaveOpeningBalanceGetRequest.getId() == null && leaveOpeningBalanceGetRequest.getEmployee() == null
				&& leaveOpeningBalanceGetRequest.getYear() == null
				&& leaveOpeningBalanceGetRequest.getLeaveType() == null
				&& leaveOpeningBalanceGetRequest.getTenantId() == null)
			return;

		selectQuery.append(" WHERE");
		boolean isAppendAndClause = false;

		if (leaveOpeningBalanceGetRequest.getTenantId() != null) {
			isAppendAndClause = true;
			selectQuery.append(" lob.tenantId = ?");
			preparedStatementValues.add(leaveOpeningBalanceGetRequest.getTenantId());
			isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
			selectQuery.append(" lt.tenantId = ?");
			preparedStatementValues.add(leaveOpeningBalanceGetRequest.getTenantId());
		}

		if (leaveOpeningBalanceGetRequest.getId() != null && !leaveOpeningBalanceGetRequest.getId().isEmpty()) {
			isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
			selectQuery.append(" lob.id IN " + getIdQuery(leaveOpeningBalanceGetRequest.getId()));
		}

		if (leaveOpeningBalanceGetRequest.getEmployee() != null
				&& !leaveOpeningBalanceGetRequest.getEmployee().isEmpty()) {
			isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
			selectQuery.append(" lob.employeeId IN " + getIdQuery(leaveOpeningBalanceGetRequest.getEmployee()));
		}

		if (leaveOpeningBalanceGetRequest.getYear() != null) {
			isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
			selectQuery.append(" lob.calendarYear = ?");
			preparedStatementValues.add(leaveOpeningBalanceGetRequest.getYear());
		}

		if (leaveOpeningBalanceGetRequest.getLeaveType() != null
				&& !leaveOpeningBalanceGetRequest.getLeaveType().isEmpty()) {
			isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
			selectQuery.append(" lob.leaveTypeId IN " + getIdQuery(leaveOpeningBalanceGetRequest.getLeaveType()));
		}
	}

	private void addOrderByClause(StringBuilder selectQuery,
								  LeaveOpeningBalanceGetRequest leaveOpeningBalanceGetRequest) {
		String sortBy = (leaveOpeningBalanceGetRequest.getSortBy() == null ? "lob.leaveTypeId , lob.employeeId "
				: leaveOpeningBalanceGetRequest.getSortBy());
		String sortOrder = (leaveOpeningBalanceGetRequest.getSortOrder() == null ? "ASC"
				: leaveOpeningBalanceGetRequest.getSortOrder());
		selectQuery.append(" ORDER BY " + sortBy + " " + sortOrder);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void addPagingClause(StringBuilder selectQuery, List preparedStatementValues,
								 LeaveOpeningBalanceGetRequest leaveOpeningBalanceGetRequest) {
		// handle limit(also called pageSize) here
		selectQuery.append(" LIMIT ?");
		long pageSize = Integer.parseInt(applicationProperties.hrLeaveSearchPageSizeDefault());
		if (leaveOpeningBalanceGetRequest.getPageSize() != null)
			pageSize = leaveOpeningBalanceGetRequest.getPageSize();
		preparedStatementValues.add(pageSize); // Set limit to pageSize

		// handle offset here
		selectQuery.append(" OFFSET ?");
		int pageNumber = 0; // Default pageNo is zero meaning first page
		if (leaveOpeningBalanceGetRequest.getPageNumber() != null)
			pageNumber = leaveOpeningBalanceGetRequest.getPageNumber() - 1;
		preparedStatementValues.add(pageNumber * pageSize); // Set offset to
		// pageNo * pageSize
	}

	/**
	 * This method is always called at the beginning of the method so that and
	 * is prepended before the field's predicate is handled.
	 *
	 * @param appendAndClauseFlag
	 * @param queryString
	 * @return boolean indicates if the next predicate should append an "AND"
	 */
	private boolean addAndClauseIfRequired(boolean appendAndClauseFlag, StringBuilder queryString) {
		if (appendAndClauseFlag)
			queryString.append(" AND");

		return true;
	}

	private static String getIdQuery(List<Long> idList) {
		StringBuilder query = new StringBuilder("(");
		if (idList.size() >= 1) {
			query.append(idList.get(0).toString());
			for (int i = 1; i < idList.size(); i++) {
				query.append(", " + idList.get(i));
			}
		}
		return query.append(")").toString();
	}
}
