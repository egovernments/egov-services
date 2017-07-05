/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 * accountability and the service delivery of the government  organizations.
 *
 *  Copyright (C) 2016  eGovernments Foundation
 *
 *  The updated version of eGov suite of products AS by eGovernments Foundation
 *  is available at http://www.egovernments.org
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License AS published by
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
 *         reasonable ways AS different from the original version.
 *
 *      3) This license does not grant any rights to any user of the program
 *         with regards to rights under trademark law for use of the trade names
 *         or trademarks of eGovernments Foundation.
 *
 *  In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */

package org.egov.eis.repository.builder;

import org.egov.eis.config.ApplicationProperties;
import org.egov.eis.web.contract.PositionHierarchyGetRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PositionHierarchyQueryBuilder {

	private static final Logger logger = LoggerFactory.getLogger(PositionHierarchyQueryBuilder.class);

	@Autowired
	private ApplicationProperties applicationProperties;

	private static final String BASE_QUERY = "SELECT ph.id AS ph_id, ph.objectSubtype AS ph_objectSubtype,"
			+ " ph.tenantId AS ph_tenantId,"
			+ " ot.id AS ot_id, ot.type AS ot_type, ot.description AS ot_description,"
			+ " ot.lastModifiedDate AS ot_lastModifiedDate,"
			+ " fp.id AS fp_id, fp.name AS fp_name, fp.isPostOutsourced AS fp_isPostOutsourced,"
			+ " fp.active AS fp_active,"
			+ " fpDepDes.id AS fpDepDes_id, fpDepDes.departmentId as fpDepDes_departmentId,"
			+ " fpDesig.id AS fpDesig_id, fpDesig.name AS fpDesig_name, fpDesig.code AS fpDesig_code,"
			+ " fpDesig.description AS fpDesig_description, fpDesig.chartOfaccounts AS fpDesig_chartOfAccounts,"
			+ " fpDesig.active AS fpDesig_active,"
			+ " tp.id AS tp_id, tp.name AS tp_name, tp.isPostOutsourced AS tp_isPostOutsourced,"
			+ " tp.active AS tp_active,"
			+ " tpDepDes.id AS tpDepDes_id, tpDepDes.departmentId as tpDepDes_departmentId,"
			+ " tpDesig.id AS tpDesig_id, tpDesig.name AS tpDesig_name, tpDesig.code AS tpDesig_code,"
			+ " tpDesig.description AS tpDesig_description, tpDesig.chartOfaccounts AS tpDesig_chartOfAccounts,"
			+ " tpDesig.active AS tpDesig_active"
			+ " FROM egeis_positionHierarchy ph"
			+ " JOIN egeis_objectType ot ON ph.objectTypeId = ot.id AND ot.tenantId = ph.tenantId"
			+ " JOIN egeis_position fp ON ph.fromPositionId = fp.id AND fp.tenantId = ph.tenantId"
			+ " JOIN egeis_departmentDesignation fpDepDes ON fp.deptDesigId = fpDepDes.id AND fpDepDes.tenantId = ph.tenantId"
			+ " JOIN egeis_designation fpDesig ON fpDepDes.designationid = fpDesig.id AND fpDesig.tenantId = ph.tenantId"
			+ " JOIN egeis_position tp ON ph.toPositionId = tp.id AND tp.tenantId = ph.tenantId"
			+ " JOIN egeis_departmentDesignation tpDepDes ON tp.deptDesigId = tpDepDes.id AND tpDepDes.tenantId = ph.tenantId"
			+ " JOIN egeis_designation tpDesig ON tpDepDes.designationid = tpDesig.id AND tpDesig.tenantId = ph.tenantId";

	@SuppressWarnings("rawtypes")
	public String getQuery(PositionHierarchyGetRequest positionHierarchyGetRequest, List preparedStatementValues) {
		StringBuilder selectQuery = new StringBuilder(BASE_QUERY);

		addWhereClause(selectQuery, preparedStatementValues, positionHierarchyGetRequest);
		addOrderByClause(selectQuery, positionHierarchyGetRequest);
		addPagingClause(selectQuery, preparedStatementValues, positionHierarchyGetRequest);

		logger.debug("Query : " + selectQuery);
		return selectQuery.toString();
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void addWhereClause(StringBuilder selectQuery, List preparedStatementValues,
			PositionHierarchyGetRequest positionHierarchyGetRequest) {

		if (positionHierarchyGetRequest.getId() == null && positionHierarchyGetRequest.getFromPosition() == null
				&& positionHierarchyGetRequest.getToPosition() == null
				&& positionHierarchyGetRequest.getObjectType() == null
				&& positionHierarchyGetRequest.getObjectSubType() == null
				&& positionHierarchyGetRequest.getTenantId() == null)
			return;

		selectQuery.append(" WHERE");
		boolean isAppendAndClause = false;

		if (positionHierarchyGetRequest.getTenantId() != null) {
			isAppendAndClause = true;
			selectQuery.append(" ph.tenantId = ?");
			preparedStatementValues.add(positionHierarchyGetRequest.getTenantId());
		}

		if (positionHierarchyGetRequest.getId() != null) {
			isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
			selectQuery.append(" ph.id IN " + getIdQuery(positionHierarchyGetRequest.getId()));
		}

		if (positionHierarchyGetRequest.getFromPosition() != null) {
			isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
			selectQuery.append(" fp.name = ?");
			preparedStatementValues.add(positionHierarchyGetRequest.getFromPosition());
		}

		if (positionHierarchyGetRequest.getToPosition() != null) {
			isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
			selectQuery.append(" tp.name = ?");
			preparedStatementValues.add(positionHierarchyGetRequest.getToPosition());
		}

		if (positionHierarchyGetRequest.getObjectType() != null) {
			isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
			selectQuery.append(" ot.type = ?");
			preparedStatementValues.add(positionHierarchyGetRequest.getObjectType());
		}

		if (positionHierarchyGetRequest.getObjectSubType() != null) {
			isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
			selectQuery.append(" ph.objectSubtype = ?");
			preparedStatementValues.add(positionHierarchyGetRequest.getObjectSubType());
		}
	}

	private void addOrderByClause(StringBuilder selectQuery, PositionHierarchyGetRequest positionHierarchyGetRequest) {
		String sortBy = (positionHierarchyGetRequest.getSortBy() == null ? "fp.name"
				: positionHierarchyGetRequest.getSortBy());
		String sortOrder = (positionHierarchyGetRequest.getSortOrder() == null ? "ASC"
				: positionHierarchyGetRequest.getSortOrder());
		selectQuery.append(" ORDER BY " + sortBy + " " + sortOrder);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void addPagingClause(StringBuilder selectQuery, List preparedStatementValues,
			PositionHierarchyGetRequest positionHierarchyGetRequest) {
		// handle limit(also called pageSize) here
		selectQuery.append(" LIMIT ?");
		long pageSize = Integer.parseInt(applicationProperties.hrSearchPageSizeDefault());
		if (positionHierarchyGetRequest.getPageSize() != null)
			pageSize = positionHierarchyGetRequest.getPageSize();
		preparedStatementValues.add(pageSize); // Set limit to pageSize

		// handle offset here
		selectQuery.append(" OFFSET ?");
		int pageNumber = 0; // Default pageNo is zero meaning first page
		if (positionHierarchyGetRequest.getPageNumber() != null)
			pageNumber = positionHierarchyGetRequest.getPageNumber() - 1;
		preparedStatementValues.add(pageNumber * pageSize); // Set offset to pageNo * pageSize
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
