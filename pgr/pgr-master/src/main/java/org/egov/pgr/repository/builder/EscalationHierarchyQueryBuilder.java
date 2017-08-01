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

import org.egov.pgr.domain.model.EscalationHierarchy;
import org.egov.pgr.web.contract.EscalationHierarchyGetReq;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class EscalationHierarchyQueryBuilder {
	
private static final Logger logger = LoggerFactory.getLogger(EscalationHierarchyQueryBuilder.class);
	
	private static final String BASE_QUERY = "SELECT tenantid, fromposition, toposition, servicecode, department, designation FROM egpgr_escalation_hierarchy esc ";
	
	@SuppressWarnings("rawtypes")
	public String getQuery(final EscalationHierarchyGetReq escHierarchy, final List preparedStatementValues) {
		final StringBuilder selectQuery = new StringBuilder(BASE_QUERY);
		addWhereClause(selectQuery, preparedStatementValues, escHierarchy);
		logger.debug("Query : " + selectQuery);
		return selectQuery.toString();
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void addWhereClause(final StringBuilder selectQuery, final List preparedStatementValues,
			final EscalationHierarchyGetReq escHierarchy) {

		if (escHierarchy.getTenantId() == null)
			return;

		selectQuery.append(" WHERE");
		boolean isAppendAndClause = false;

		if (escHierarchy.getTenantId() != null) {
			isAppendAndClause = true;
			selectQuery.append(" esc.tenantid = ?");
			preparedStatementValues.add(escHierarchy.getTenantId());
		}

		if (null != escHierarchy.getFromPosition() && escHierarchy.getFromPosition() != 0) {
			isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
			selectQuery.append(" esc.fromposition = ?");
			preparedStatementValues.add(escHierarchy.getFromPosition());
		}
		
		if (null != escHierarchy.getToPosition() && escHierarchy.getToPosition() != 0) {
			isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
			selectQuery.append(" esc.toposition = ?");
			preparedStatementValues.add(escHierarchy.getToPosition());
		}
		
		if (null != escHierarchy.getServiceCode() && 0 <= escHierarchy.getServiceCode().size()) {
			isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
			selectQuery.append(" esc.servicecode IN " + getCodeQuery(escHierarchy.getServiceCode()));
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

	private static String getCodeQuery(final List<String> codeList) {
		final StringBuilder query = new StringBuilder("(");
		if (codeList.size() >= 1) {
			query.append("'" + codeList.get(0).toString()+ "'");
			for (int i = 1; i < codeList.size(); i++)
				query.append(", " + "'" + codeList.get(i) + "'");
		}
		return query.append(")").toString();
	}
	
	public String insertEscalationHierarchy() {
		return "INSERT INTO egpgr_escalation_hierarchy (tenantid, fromposition, toposition, servicecode, department, designation, createdby, createddate) "
				+ " values (?, ?, ?, ?, ?, ?, ?, ?)";
	}
	
	public String deleteEscalationHierarchy(List<EscalationHierarchy> escHierarchyList) {
		StringBuilder deleteBaseQuery = new StringBuilder();
		StringBuilder tenantIdConstructor= new StringBuilder();
		StringBuilder fromPositionConstructor = new StringBuilder();
		StringBuilder serviceCodeConstructor = new StringBuilder();
		if (null != escHierarchyList && escHierarchyList.size() > 0) {
			serviceCodeConstructor.append("servicecode IN (");
			tenantIdConstructor.append("tenantid IN (");
			fromPositionConstructor.append("fromposition IN (");
			for (int i = 0; i < escHierarchyList.size(); i++) {
				if(i==0 && escHierarchyList.size()-1==0){
					serviceCodeConstructor.append("'" + escHierarchyList.get(i).getServiceCode() + "'"); 
					tenantIdConstructor.append("'" + escHierarchyList.get(i).getTenantId() + "'");
					fromPositionConstructor.append("'" + escHierarchyList.get(i).getFromPosition() + "'");
				} else if (i== escHierarchyList.size()-1) {
					serviceCodeConstructor.append("'" + escHierarchyList.get(i).getServiceCode() + "'");
					tenantIdConstructor.append("'" + escHierarchyList.get(i).getTenantId() + "'");
					fromPositionConstructor.append("'" + escHierarchyList.get(i).getFromPosition() + "'");
				} else {
					serviceCodeConstructor.append("'" + escHierarchyList.get(i).getServiceCode() + "'");
					serviceCodeConstructor.append(",");
					tenantIdConstructor.append("'" + escHierarchyList.get(i).getTenantId() + "'");
					tenantIdConstructor.append(",");
					fromPositionConstructor.append("'" + escHierarchyList.get(i).getFromPosition() + "'");
					fromPositionConstructor.append(",");
				}
			}
			deleteBaseQuery.append("DELETE FROM egpgr_escalation_hierarchy WHERE ");
			deleteBaseQuery.append(serviceCodeConstructor.toString() + ")");
			deleteBaseQuery.append(" AND " + tenantIdConstructor.toString() + ")");
			deleteBaseQuery.append(" AND " + fromPositionConstructor.toString() + ")");
		}
		logger.info("We will be running this : "+ deleteBaseQuery);
		return deleteBaseQuery.toString();
	}

}
