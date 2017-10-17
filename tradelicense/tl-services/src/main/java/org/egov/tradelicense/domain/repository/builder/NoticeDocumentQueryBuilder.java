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

package org.egov.tradelicense.domain.repository.builder;

import java.sql.Timestamp;
import java.util.List;

import org.egov.tl.commons.web.contract.RequestInfo;
import org.egov.tradelicense.common.config.PropertiesManager;
import org.egov.tradelicense.web.contract.NoticeDocumentGetRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Component;

@Component
public class NoticeDocumentQueryBuilder {

	private static final Logger logger = LoggerFactory.getLogger(NoticeDocumentQueryBuilder.class);
	private static final String BASE_QUERY = " SELECT doc.*, app.applicationNumber as applicationNumber,license.licenseNumber as tradeLicenseNumber, app.applicationDate as applicationDate,"
			+ "app.applicationType as applicationType, license.adminWard as ward, license.validityyears as validityYear, app.status as status, license.ownerName as ownerName,"
			+ "license.mobileNumber as mobileNumber, license.tradeTitle as tradeTitle FROM egtl_notice_document as doc JOIN egtl_license as license ON license.id= "
			+ "doc.licenseId JOIN egtl_license_application as app ON license.id = app.licenseId";

	@Autowired
	private PropertiesManager propertiesManager;

	private static String getIdQuery(final List<Long> idList) {
		final StringBuilder query = new StringBuilder("(");
		if (idList.size() >= 1) {
			query.append(idList.get(0).toString());
			for (int i = 1; i < idList.size(); i++)
				query.append(", " + idList.get(i));
		}
		return query.append(")").toString();
	}

	@SuppressWarnings("rawtypes")
	public String getQuery(final NoticeDocumentGetRequest noticeDocumentGetRequest,
			final MapSqlParameterSource preparedStatementValues, final RequestInfo requestInfo) {
		final StringBuilder selectQuery = new StringBuilder(BASE_QUERY);

		addWhereClause(selectQuery, preparedStatementValues, noticeDocumentGetRequest, requestInfo);
		addOrderByClause(selectQuery, noticeDocumentGetRequest);
		addPagingClause(selectQuery, preparedStatementValues, noticeDocumentGetRequest);

		logger.debug("Query : " + selectQuery);
		return selectQuery.toString();
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void addWhereClause(final StringBuilder selectQuery, final MapSqlParameterSource preparedStatementValues,
			final NoticeDocumentGetRequest noticeDocumentGetRequest, final RequestInfo requestInfo) {

         if( noticeDocumentGetRequest.getTenantId() == null ){
        	 return;
         }
         
		selectQuery.append(" WHERE");
		boolean isAppendAndClause = false;

		if (noticeDocumentGetRequest.getTenantId() != null && !noticeDocumentGetRequest.getTenantId().isEmpty()) {
			isAppendAndClause = true;
			selectQuery.append(" upper(doc.tenantId) = :tenantId");
			preparedStatementValues.addValue("tenantId",  noticeDocumentGetRequest.getTenantId().toUpperCase() );
		}

		if (noticeDocumentGetRequest.getIds() != null && !noticeDocumentGetRequest.getIds().isEmpty()) {
			isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
			selectQuery.append(" doc.id IN " + getIdQuery(noticeDocumentGetRequest.getIds()));
		}

		if (noticeDocumentGetRequest.getDocumentType() != null) {
			isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
			selectQuery.append(" upper(doc.documentname) like :documentName");
			preparedStatementValues.addValue("documentName", '%' + noticeDocumentGetRequest.getDocumentType().toUpperCase() + '%');
		}

		if (noticeDocumentGetRequest.getOwnerName() != null && !noticeDocumentGetRequest.getOwnerName().isEmpty()) {
			isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
			selectQuery.append(" upper(license.ownername) like :ownerName");
			preparedStatementValues.addValue("ownerName", '%' + noticeDocumentGetRequest.getOwnerName().toUpperCase() + '%');
		}

		if ( noticeDocumentGetRequest.getWard() != null ) {
			isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
			selectQuery.append(" upper(license.adminward) = :ward");
			preparedStatementValues.addValue("ward", noticeDocumentGetRequest.getWard().toUpperCase());
		}

		if (noticeDocumentGetRequest.getMobileNumber() != null && !noticeDocumentGetRequest.getMobileNumber() .isEmpty()) {
			isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
			selectQuery.append(" license.mobilenumber like :mobileNumber");
			preparedStatementValues.addValue("mobileNumber", '%' + noticeDocumentGetRequest.getMobileNumber() + '%');
		}

		if (noticeDocumentGetRequest.getTradeTitle() != null && !noticeDocumentGetRequest.getTradeTitle().isEmpty()) {
			isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
			selectQuery.append(" upper(license.tradetitle) like :tradeTitle");
			preparedStatementValues.addValue("tradeTitle", '%' + noticeDocumentGetRequest.getTradeTitle().toUpperCase() + '%');
		}

		if (noticeDocumentGetRequest.getTradeLicenseNumber() != null && !noticeDocumentGetRequest.getTradeLicenseNumber().isEmpty()) {
			isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
			selectQuery.append(" upper(license.licensenumber) like :licenseNumber");
			preparedStatementValues.addValue("licenseNumber", '%' + noticeDocumentGetRequest.getTradeLicenseNumber().toUpperCase() + '%');
		}

		if (noticeDocumentGetRequest.getStatus() != null && !noticeDocumentGetRequest.getStatus().isEmpty()) {
			isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
			selectQuery.append(" upper(license.status) = :status");
			preparedStatementValues.addValue("status",  noticeDocumentGetRequest.getStatus().toUpperCase());
		}
		
		if (noticeDocumentGetRequest.getApplicationStatus() != null && !noticeDocumentGetRequest.getApplicationStatus().isEmpty()) {
			isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
			selectQuery.append(" upper(app.status) = :appstatus");
			preparedStatementValues.addValue("appstatus",  noticeDocumentGetRequest.getApplicationStatus().toUpperCase());
		}

		if (noticeDocumentGetRequest.getApplicationType() != null  && !noticeDocumentGetRequest.getApplicationType().isEmpty()) {
			isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
			selectQuery.append(" upper(app.applicationtype) like :applicationType");
			preparedStatementValues.addValue("applicationType", '%' + noticeDocumentGetRequest.getApplicationType().toUpperCase() + '%');
		}

		if (noticeDocumentGetRequest.getApplicationNumber() != null && !noticeDocumentGetRequest.getApplicationNumber().isEmpty()) {
			isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
			selectQuery.append(" upper(app.applicationnumber) like :applicationNumber");
			preparedStatementValues.addValue("applicationNumber", '%' + noticeDocumentGetRequest.getApplicationNumber().toUpperCase() + '%');
		}

		if (noticeDocumentGetRequest.getDateFrom() != null ) {
			Timestamp fromDate = new Timestamp(noticeDocumentGetRequest.getDateFrom());
			isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
			selectQuery.append(" app.applicationdate > :applicationFromDate");
			preparedStatementValues.addValue("applicationFromDate", fromDate);
		}

		if (noticeDocumentGetRequest.getDateTo() != null ) {
			Timestamp toDate = new Timestamp(noticeDocumentGetRequest.getDateTo());
			isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
			selectQuery.append(" app.applicationdate < :applicationToDate");
			preparedStatementValues.addValue("applicationToDate", toDate);
		}

	}

	private void addOrderByClause(final StringBuilder selectQuery,
			final NoticeDocumentGetRequest noticeDocumentGetRequest) {
		final String sortBy = noticeDocumentGetRequest.getSortBy() == null ? "id"
				: noticeDocumentGetRequest.getSortBy();
		final String sortOrder = noticeDocumentGetRequest.getSortOrder() == null ? "ASC"
				: noticeDocumentGetRequest.getSortOrder();
		selectQuery.append(" ORDER BY " + sortBy + " " + sortOrder);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void addPagingClause(final StringBuilder selectQuery, final MapSqlParameterSource preparedStatementValues,
			final NoticeDocumentGetRequest noticeDocumentGetRequest) {
		// handle limit(also called pageSize) here
		selectQuery.append(" LIMIT :limit");
		long pageSize = Integer.parseInt(propertiesManager.getTlSearchPageSizeDefault());
		if (noticeDocumentGetRequest.getPageSize() != null)
			pageSize = noticeDocumentGetRequest.getPageSize();
		preparedStatementValues.addValue("limit", pageSize); // Set limit to
																// pageSize

		// handle offset here
		selectQuery.append(" OFFSET :offset");
		int pageNumber = 0; // Default pageNo is zero meaning first page
		if (noticeDocumentGetRequest.getPageNumber() != null)
			pageNumber = noticeDocumentGetRequest.getPageNumber() - 1;
		preparedStatementValues.addValue("offset", pageNumber * pageSize); // Set
																			// offset
																			// to
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
	private boolean addAndClauseIfRequired(final boolean appendAndClauseFlag, final StringBuilder queryString) {
		if (appendAndClauseFlag)
			queryString.append(" AND");

		return true;
	}
}
