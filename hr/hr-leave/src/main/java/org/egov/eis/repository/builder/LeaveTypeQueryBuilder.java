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

import org.egov.eis.config.ApplicationProperties;
import org.egov.eis.web.contract.LeaveTypeGetRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class LeaveTypeQueryBuilder {

	private static final Logger logger = LoggerFactory.getLogger(LeaveTypeQueryBuilder.class);

	@Autowired
	private ApplicationProperties applicationProperties;

	private static final String BASE_QUERY = "SELECT id, name, description, halfdayAllowed, payEligible, accumulative,"
			+ " encashable, encloseHoliday, includePrefixSuffix, active, createdBy, createdDate, lastModifiedBy, lastModifiedDate, tenantId"
			+ " FROM egeis_leaveType";

	@SuppressWarnings("rawtypes")
	public String getQuery(LeaveTypeGetRequest leaveTypeGetRequest, List preparedStatementValues) {
		StringBuilder selectQuery = new StringBuilder(BASE_QUERY);

		addWhereClause(selectQuery, preparedStatementValues, leaveTypeGetRequest);
		addOrderByClause(selectQuery, leaveTypeGetRequest);
		addPagingClause(selectQuery, preparedStatementValues, leaveTypeGetRequest);

		logger.debug("Query : " + selectQuery);
		return selectQuery.toString();
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void addWhereClause(StringBuilder selectQuery, List preparedStatementValues,
			LeaveTypeGetRequest leaveTypeGetRequest) {

		if (leaveTypeGetRequest.getId() == null && leaveTypeGetRequest.getActive() == null
				&& leaveTypeGetRequest.getTenantId() == null)
			return;

		selectQuery.append(" WHERE");
		boolean isAppendAndClause = false;

		if (leaveTypeGetRequest.getTenantId() != null) {
			isAppendAndClause = true;
			selectQuery.append(" tenantId = ?");
			preparedStatementValues.add(leaveTypeGetRequest.getTenantId());
		}

		if (leaveTypeGetRequest.getId() != null) {
			isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
			selectQuery.append(" id IN " + getIdQuery(leaveTypeGetRequest.getId()));
		}

		if (leaveTypeGetRequest.getActive() != null) {
			isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
			selectQuery.append(" active = ?");
			preparedStatementValues.add(leaveTypeGetRequest.getActive());
		}

		if (leaveTypeGetRequest.getAccumulative() != null) {
			isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
			selectQuery.append(" accumulative = ?");
			preparedStatementValues.add(leaveTypeGetRequest.getAccumulative());
		}
	}

	private void addOrderByClause(StringBuilder selectQuery, LeaveTypeGetRequest leaveTypeGetRequest) {
		String sortBy = (leaveTypeGetRequest.getSortBy() == null ? "name" : leaveTypeGetRequest.getSortBy());
		String sortOrder = (leaveTypeGetRequest.getSortOrder() == null ? "ASC" : leaveTypeGetRequest.getSortOrder());
		selectQuery.append(" ORDER BY " + sortBy + " " + sortOrder);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void addPagingClause(StringBuilder selectQuery, List preparedStatementValues,
			LeaveTypeGetRequest leaveTypeGetRequest) {
		// handle limit(also called pageSize) here
		selectQuery.append(" LIMIT ?");
		long pageSize = Integer.parseInt(applicationProperties.hrLeaveSearchPageSizeDefault());
		if (leaveTypeGetRequest.getPageSize() != null)
			pageSize = leaveTypeGetRequest.getPageSize();
		preparedStatementValues.add(pageSize); // Set limit to pageSize

		// handle offset here
		selectQuery.append(" OFFSET ?");
		int pageNumber = 0; // Default pageNo is zero meaning first page
		if (leaveTypeGetRequest.getPageNumber() != null)
			pageNumber = leaveTypeGetRequest.getPageNumber() - 1;
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

	public static String insertLeaveTypeQuery() {
		return "INSERT INTO egeis_leavetype(id,name,description,halfdayallowed,payeligible,accumulative,encashable,encloseholiday,includeprefixsuffix,active,createdby,lastmodifiedby,createddate,lastmodifieddate,tenantid) values "
				+ "(nextval('seq_egeis_leaveType'),?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
	}

	public static String updateLeaveTypeQuery() {
		return "UPDATE egeis_leavetype SET name = ?, description = ?,"
				+ " halfdayAllowed = ?, payEligible = ?, accumulative = ?, encashable = ?, encloseholiday = ?, includeprefixsuffix = ?,"
				+ " active = ? , createdBy = ?, lastModifiedBy=?, createdDate=? , lastModifiedDate=?, tenantId=? where id = ? and tenantid=?";
	}

	public static String selectLeaveTypeByNameQuery() {
		return " select * from egeis_leavetype where upper(name) = ? and tenantId = ? ";
	}

	public static String selectLeaveTypeByNameAndIdNotInQuery() {
		return " select * from egeis_leavetype where upper(name) = ? and tenantId = ? and id != ? ";
	}

}
