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
package org.egov.pgr.repository.builder;

import java.util.List;

import org.egov.pgr.web.contract.EscalationTimeTypeGetReq;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class EscalationTimeTypeQueryBuilder {
	
	private static final Logger logger = LoggerFactory.getLogger(EscalationTimeTypeQueryBuilder.class);
	
	private static final String BASE_QUERY = "SELECT escalation.id,escalation.complaint_type_id,escalation.designation_id,escalation.no_of_hrs,escalation.tenantId from egpgr_escalation escalation";
	
	public String insertEscalationTimeType(){
		return "INSERT INTO egpgr_escalation(complaint_type_id, no_of_hrs, designation_id, tenantid, createdby"
				+ ", lastmodifiedby, createddate, lastmodifieddate) VALUES(?,?,?,?,?,?,?,?)";
	}
	
	public String updateEscalationTimeType(){
		return "UPDATE egpgr_escalation set complaint_type_id = ?, no_of_hrs = ?, designation_id = ?, tenantid = ?, "
				+ "createdby = ?, lastmodifiedby = ?, createddate = ?, lastmodifieddate = ? where id = ?";
	}
	
	@SuppressWarnings("rawtypes")
	public String getQuery(final EscalationTimeTypeGetReq centerTypeRequest, final List preparedStatementValues) {
		final StringBuilder selectQuery = new StringBuilder(BASE_QUERY);
		addWhereClause(selectQuery, preparedStatementValues, centerTypeRequest);
		addOrderByClause(selectQuery, centerTypeRequest);
		addPagingClause(selectQuery, preparedStatementValues, centerTypeRequest);
		logger.debug("Query : " + selectQuery);
		return selectQuery.toString();
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void addWhereClause(final StringBuilder selectQuery, final List preparedStatementValues,
			final EscalationTimeTypeGetReq escalationRequest) {

		if (escalationRequest.getId() == null && escalationRequest.getGrievanceType() == 0 && escalationRequest.getDesignation() == 0
				 && escalationRequest.getTenantId() == null)
			return;

		selectQuery.append(" WHERE");
		boolean isAppendAndClause = false;

		if (escalationRequest.getTenantId() != null) {
			isAppendAndClause = true;
			selectQuery.append(" escalation.tenantId = ?");
			preparedStatementValues.add(escalationRequest.getTenantId());
		}

		if (escalationRequest.getId() != null) {
			isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
			selectQuery.append(" escalation.id IN " + getIdQuery(escalationRequest.getId()));
		}

		if (escalationRequest.getGrievanceType() != 0) {
			isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
			selectQuery.append(" escalation.complaint_type_id = ?");
			preparedStatementValues.add(escalationRequest.getGrievanceType());
		}
		
		if (escalationRequest.getDesignation() != 0) {
			isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
			selectQuery.append(" escalation.designation_id = ?");
			preparedStatementValues.add(escalationRequest.getDesignation());
		}
		
		

		/*
		 * if (centerTypeRequest.getCode() != null) { isAppendAndClause =
		 * addAndClauseIfRequired(isAppendAndClause, selectQuery);
		 * selectQuery.append(" centerType.code = ?");
		 * preparedStatementValues.add(centerTypeRequest.getCode()); }
		 */
		/*if (escalationRequest.getActive() != null) {
			isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
			selectQuery.append(" centerType.active = ?");
			preparedStatementValues.add(escalationRequest.getActive());
		}*/

	}

	
	
	/**
	 * This method is always called at the beginning of the method so that and
	 * is prepended before the field's predicate is handled.
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

	private void addOrderByClause(final StringBuilder selectQuery,
			final EscalationTimeTypeGetReq escalationGetRequest) {
		final String sortBy = escalationGetRequest.getSortBy() == null ? "escalation.id"
				: "escalation." + escalationGetRequest.getSortBy();
		final String sortOrder = escalationGetRequest.getSortOrder() == null ? "DESC"
				: escalationGetRequest.getSortOrder();
		selectQuery.append(" ORDER BY " + sortBy + " " + sortOrder);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void addPagingClause(final StringBuilder selectQuery, final List preparedStatementValues,
			final EscalationTimeTypeGetReq escalationGetRequest) {
		// handle limit(also called pageSize) here
		selectQuery.append(" LIMIT ?");
		long pageSize = Integer.parseInt("100");
		if (escalationGetRequest.getPageSize() != null)
			pageSize = escalationGetRequest.getPageSize();
		preparedStatementValues.add(pageSize); // Set limit to pageSize

		// handle offset here
		selectQuery.append(" OFFSET ?");
		int pageNumber = 0; // Default pageNo is zero meaning first page
		if (escalationGetRequest.getPageNumber() != null)
			pageNumber = escalationGetRequest.getPageNumber() - 1;
		preparedStatementValues.add(pageNumber * pageSize); // Set offset to
		// pageNo * pageSize
	}

}
