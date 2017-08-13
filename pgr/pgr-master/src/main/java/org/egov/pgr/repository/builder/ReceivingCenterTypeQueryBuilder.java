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

package org.egov.pgr.repository.builder;

import java.util.List;

import org.egov.pgr.web.contract.ReceivingCenterTypeGetReq;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class ReceivingCenterTypeQueryBuilder {

	private static final Logger logger = LoggerFactory.getLogger(ReceivingCenterTypeQueryBuilder.class);

	// private static final String BASE_QUERY = "SELECT
	// centerType.id,centerType.code,centerType.name,centerType.description,centerType.active,centerType.tenantId,centerType.visible
	// from egpgr_receivingcenter_type centerType";

	private static final String BASE_QUERY = "SELECT centerType.id,centerType.code,centerType.name,centerType.description,centerType.iscrnrequired,centerType.orderno,centerType.active,centerType.tenantId from egpgr_receiving_center centerType";

	@SuppressWarnings("rawtypes")
	public String getQuery(final ReceivingCenterTypeGetReq centerTypeRequest, final List preparedStatementValues) {
		final StringBuilder selectQuery = new StringBuilder(BASE_QUERY);
		addWhereClause(selectQuery, preparedStatementValues, centerTypeRequest);
		addOrderByClause(selectQuery, centerTypeRequest);
		addPagingClause(selectQuery, preparedStatementValues, centerTypeRequest);
		logger.debug("Query : " + selectQuery);
		return selectQuery.toString();
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void addWhereClause(final StringBuilder selectQuery, final List preparedStatementValues,
			final ReceivingCenterTypeGetReq centerTypeRequest) {

		if (centerTypeRequest.getId() == null && centerTypeRequest.getName() == null
				&& centerTypeRequest.getActive() == null && centerTypeRequest.getTenantId() == null)
			return;

		selectQuery.append(" WHERE");
		boolean isAppendAndClause = false;

		if (centerTypeRequest.getTenantId() != null) {
			isAppendAndClause = true;
			selectQuery.append(" centerType.tenantId = ?");
			preparedStatementValues.add(centerTypeRequest.getTenantId());
		}

		if (centerTypeRequest.getId() != null) {
			isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
			selectQuery.append(" centerType.id IN " + getIdQuery(centerTypeRequest.getId()));
		}

		if (centerTypeRequest.getName() != null) {
			isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
			selectQuery.append(" centerType.name = ?");
			preparedStatementValues.add(centerTypeRequest.getName());
		}

		if (centerTypeRequest.getCode() != null) {
			isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
			selectQuery.append(" centerType.code = ?");
			preparedStatementValues.add(centerTypeRequest.getCode());
		}

		if (centerTypeRequest.getActive() != null) {
			isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
			selectQuery.append(" centerType.active = ?");
			preparedStatementValues.add(centerTypeRequest.getActive());
		}

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
			final ReceivingCenterTypeGetReq centerTypeGetRequest) {
		final String sortBy = centerTypeGetRequest.getSortBy() == null ? "centerType.code"
				: "centerType." + centerTypeGetRequest.getSortBy();
		final String sortOrder = centerTypeGetRequest.getSortOrder() == null ? "DESC"
				: centerTypeGetRequest.getSortOrder();
		selectQuery.append(" ORDER BY " + sortBy + " " + sortOrder);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void addPagingClause(final StringBuilder selectQuery, final List preparedStatementValues,
			final ReceivingCenterTypeGetReq centerTypeGetRequest) {
		// handle limit(also called pageSize) here
		selectQuery.append(" LIMIT ?");
		long pageSize = Integer.parseInt("100");
		if (centerTypeGetRequest.getPageSize() != null)
			pageSize = centerTypeGetRequest.getPageSize();
		preparedStatementValues.add(pageSize); // Set limit to pageSize

		// handle offset here
		selectQuery.append(" OFFSET ?");
		int pageNumber = 0; // Default pageNo is zero meaning first page
		if (centerTypeGetRequest.getPageNumber() != null)
			pageNumber = centerTypeGetRequest.getPageNumber() - 1;
		preparedStatementValues.add(pageNumber * pageSize); // Set offset to
		// pageNo * pageSize
	}

	public static String insertReceivingCenterTypeQuery() {

		return "INSERT INTO egpgr_receiving_center(id,code,name,description,iscrnrequired,orderno,active,createdby,lastmodifiedby,createddate,lastmodifieddate,tenantid) values "
				+ "(nextval('seq_egpgr_receiving_center'),?,?,?,?,?,?,?,?,?,?,?)";
	}

	public static String updateReceivingCenterTypeQuery() {
		return "UPDATE egpgr_receiving_center SET name = ?,description = ?,iscrnrequired=?,orderno=?,"
				+ "active = ?,lastmodifiedby = ?,lastmodifieddate = ? where code = ?";
	}

	public static String checkReceivingCenterTypeByName() {

		return "select code,name from egpgr_receiving_center where tenantid=? and upper(name)=? ";
	}
	
	public static String checkReceivingCenterTypeByNameUpdate() {

		return "select id from egpgr_receiving_center where tenantid=? and trim(upper(name))=? and id not in (?)" ;
	}

	public static String checkReceivingCenterTypeByCode() {

		return "select id from egpgr_receiving_center where tenantid = ? and trim(code) = ?";

	}

	public static String checkReceivingCenterTypeByCodeName() {

		//return "select code,name from egpgr_receiving_center where tenantid = ? and trim(code) = ? and trim(upper(name))=?";

		return "select code from egpgr_receiving_center where tenantid = ? and trim(upper(code)) = ? and trim(upper(name)) = ?";

	}

}
