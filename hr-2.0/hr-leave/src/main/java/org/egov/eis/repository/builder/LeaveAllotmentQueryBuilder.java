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
import org.egov.eis.web.contract.LeaveAllotmentGetRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class LeaveAllotmentQueryBuilder {

	private static final Logger logger = LoggerFactory.getLogger(LeaveAllotmentQueryBuilder.class);
	private static final String BASE_QUERY = "SELECT la.id AS la_id, la.designationId AS la_designationId,"
			+ " la.noOfDays AS la_noOfDays, la.createdBy AS la_createdBy, la.createdDate AS la_createdDate,"
			+ " la.lastModifiedBy AS la_lastModifiedBy, la.lastModifiedDate AS la_lastModifiedDate,"
			+ " la.tenantId AS la_tenantId," + " lt.id AS lt_id, lt.name AS lt_name, lt.description AS lt_description,"
			+ " lt.halfdayAllowed AS lt_halfdayAllowed, lt.payEligible AS lt_payEligible,"
			+ " lt.accumulative AS lt_accumulative, lt.encashable AS lt_encashable,"
			+ " lt.active AS lt_active, lt.createdBy AS lt_createdBy, lt.createdDate AS lt_createdDate,"
			+ " lt.lastModifiedBy AS lt_lastModifiedBy, lt.lastModifiedDate AS lt_lastModifiedDate"
			+ " FROM egeis_leaveAllotment la" + " JOIN egeis_leaveType lt ON la.leaveTypeId = lt.id";
	@Autowired
	private ApplicationProperties applicationProperties;

	private static String getIdQuery(List<Long> idList) {
		StringBuilder query = new StringBuilder("(");
		if (idList.size() >= 1 && idList.get(0) != null) {
			query.append(idList.get(0).toString());
			for (int i = 1; i < idList.size(); i++) {
				query.append(", " + idList.get(i));
			}
		}
		return query.append(")").toString();
	}

	public static String selectLeaveAllotmentByDesignationQuery() {
		return " select * from egeis_leaveallotment where leavetypeid = ?  and tenantId = ? and designationid = ?";
	}

	public static String selectLeaveAllotmentByLeavetypeQuery() {
		return " select * from egeis_leaveallotment where leavetypeid = ? and tenantId = ? and designationid is null ";
	}

	public static String selectLeaveAllotmentByDesignationAndIdNotInQuery() {
		return " select * from egeis_leaveallotment where leavetypeid = ?  and tenantId = ? and designationid = ? and id!= ?";
	}

	public static String selectLeaveAllotmentByLeavetypeAndIdNotInQuery() {
		return " select * from egeis_leaveallotment where leavetypeid = ? and tenantId = ? and id!= ? and designationid is null ";
	}

	@SuppressWarnings("rawtypes")
	public String getQuery(LeaveAllotmentGetRequest leaveAllotmentGetRequest, List preparedStatementValues) {
		StringBuilder selectQuery = new StringBuilder(BASE_QUERY);

		addWhereClause(selectQuery, preparedStatementValues, leaveAllotmentGetRequest);
		addOrderByClause(selectQuery, leaveAllotmentGetRequest);
		addPagingClause(selectQuery, preparedStatementValues, leaveAllotmentGetRequest);

		logger.debug("Query : " + selectQuery);
		return selectQuery.toString();
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void addWhereClause(StringBuilder selectQuery, List preparedStatementValues,
			LeaveAllotmentGetRequest leaveAllotmentGetRequest) {

		if (leaveAllotmentGetRequest.getId() == null && leaveAllotmentGetRequest.getDesignationId() == null
				&& leaveAllotmentGetRequest.getLeaveType() == null && leaveAllotmentGetRequest.getTenantId() == null)
			return;

		selectQuery.append(" WHERE");
		boolean isAppendAndClause = false;

		if (leaveAllotmentGetRequest.getTenantId() != null) {
			isAppendAndClause = true;
			selectQuery.append(" la.tenantId = ?");
			preparedStatementValues.add(leaveAllotmentGetRequest.getTenantId());
			isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
			selectQuery.append(" lt.tenantId = ?");
			preparedStatementValues.add(leaveAllotmentGetRequest.getTenantId());
		}

		if (leaveAllotmentGetRequest.getId() != null && !leaveAllotmentGetRequest.getId().isEmpty()) {
			isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
			selectQuery.append(" la.id IN " + getIdQuery(leaveAllotmentGetRequest.getId()));
		}

		if (leaveAllotmentGetRequest.getDesignationId() != null
				&& !leaveAllotmentGetRequest.getDesignationId().isEmpty()
				&& leaveAllotmentGetRequest.getDesignationId().get(0) != null) {
			isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
			selectQuery.append(" la.designationId IN " + getIdQuery(leaveAllotmentGetRequest.getDesignationId()));
		}

		if (leaveAllotmentGetRequest.getLeaveType() != null && !leaveAllotmentGetRequest.getLeaveType().isEmpty()) {
			isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
			selectQuery.append(" la.leaveTypeId IN " + getIdQuery(leaveAllotmentGetRequest.getLeaveType()));
		}
	}

	private void addOrderByClause(StringBuilder selectQuery, LeaveAllotmentGetRequest leaveAllotmentGetRequest) {
		String sortBy = (leaveAllotmentGetRequest.getSortBy() == null ? "lt.name"
				: leaveAllotmentGetRequest.getSortBy());
		String sortOrder = (leaveAllotmentGetRequest.getSortOrder() == null ? "ASC"
				: leaveAllotmentGetRequest.getSortOrder());
		selectQuery.append(" ORDER BY " + sortBy + " " + sortOrder);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void addPagingClause(StringBuilder selectQuery, List preparedStatementValues,
			LeaveAllotmentGetRequest leaveAllotmentGetRequest) {
		// handle limit(also called pageSize) here
		selectQuery.append(" LIMIT ?");
		long pageSize = Integer.parseInt(applicationProperties.hrLeaveSearchPageSizeDefault());
		if (leaveAllotmentGetRequest.getPageSize() != null)
			pageSize = leaveAllotmentGetRequest.getPageSize();
		preparedStatementValues.add(pageSize); // Set limit to pageSize

		// handle offset here
		selectQuery.append(" OFFSET ?");
		int pageNumber = 0; // Default pageNo is zero meaning first page
		if (leaveAllotmentGetRequest.getPageNumber() != null)
			pageNumber = leaveAllotmentGetRequest.getPageNumber() - 1;
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
			queryString.append(" AND ");

		return true;
	}
}
