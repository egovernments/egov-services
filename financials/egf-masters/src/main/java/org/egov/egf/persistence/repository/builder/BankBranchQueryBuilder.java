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

package org.egov.egf.persistence.repository.builder;

import java.util.List;

import org.egov.egf.config.ApplicationProperties;
import org.egov.egf.persistence.queue.contract.BankBranchGetRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BankBranchQueryBuilder {

	private static final Logger logger = LoggerFactory.getLogger(BankBranchQueryBuilder.class);

	@Autowired
	private ApplicationProperties applicationProperties;

	private static final String BASE_QUERY = "SELECT bb.id AS bb_id,bb.code AS bb_code,bb.name AS bb_name,bb.address AS bb_address,bb.address2 AS bb_address2,bb.city AS bb_city,bb.state AS bb_state,bb.fax AS bb_fax,bb.contactPerson AS bb_contactPerson,bb.pincode AS bb_pincode,bb.phone as bb_phone,bb.active as bb_active,bb.description as bb_description,bb.micr as bb_micr, "
			+ " bb.createdby AS bb_createdby,bb.createddate AS bb_createddate, bb.lastmodifiedby AS bb_lastmodifiedby,"
			+ " bb.lastmodifieddate AS bb_lastmodifieddate, bb.tenantid AS bb_tenantid, "
			+ " b.id AS b_id,b.code AS b_code,b.name AS b_name,b.type AS b_type,b.active AS b_active,b.description AS b_description,"
			+ " b.createdby AS b_createdby,b.createddate AS b_createddate, b.lastmodifiedby AS b_lastmodifiedby,"
			+ " b.lastmodifieddate AS b_lastmodifieddate, b.tenantid AS b_tenantid "
			+ " FROM egf_bankbranch bb,egf_bank b where b.id=bb.bankid and b.tenantid=bb.tenantid ";

	@SuppressWarnings("rawtypes")
	public String getQuery(BankBranchGetRequest bankBranchGetRequest, List preparedStatementValues) {
		StringBuilder selectQuery = new StringBuilder(BASE_QUERY);

		addWhereClause(selectQuery, preparedStatementValues, bankBranchGetRequest);
		addOrderByClause(selectQuery, bankBranchGetRequest);
		addPagingClause(selectQuery, preparedStatementValues, bankBranchGetRequest);

		logger.debug("Query : " + selectQuery);
		return selectQuery.toString();
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void addWhereClause(StringBuilder selectQuery, List preparedStatementValues,
			BankBranchGetRequest bankBranchGetRequest) {

		if (bankBranchGetRequest.getId() == null && bankBranchGetRequest.getCode() == null
				&& bankBranchGetRequest.getName() == null && bankBranchGetRequest.getAddress() == null
				&& bankBranchGetRequest.getAddress2() == null && bankBranchGetRequest.getCity() == null
				&& bankBranchGetRequest.getState() == null && bankBranchGetRequest.getPincode() == null
				&& bankBranchGetRequest.getPhone() == null && bankBranchGetRequest.getFax() == null
				&& bankBranchGetRequest.getContactPerson() == null && bankBranchGetRequest.getActive() == null
				&& bankBranchGetRequest.getDescription() == null && bankBranchGetRequest.getMicr() == null
				&& bankBranchGetRequest.getTenantId() == null)
			return;

		boolean isAppendAndClause = true;

		if (bankBranchGetRequest.getTenantId() != null) {
			isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
			selectQuery.append(" bb.tenantId = ?");
			preparedStatementValues.add(bankBranchGetRequest.getTenantId());
		}

		if (bankBranchGetRequest.getId() != null && !bankBranchGetRequest.getId().isEmpty()) {
			isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
			selectQuery.append(" bb.id IN " + getIdQuery(bankBranchGetRequest.getId()));
		}
		
		if (bankBranchGetRequest.getBank() != null) {
			isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
			selectQuery.append(" bb.bankid = ? ");
			preparedStatementValues.add(bankBranchGetRequest.getBank());
		}
		
		if (bankBranchGetRequest.getCode() != null && !bankBranchGetRequest.getCode().isEmpty()) {
			isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
			selectQuery.append(" bb.code = ? ");
			preparedStatementValues.add(bankBranchGetRequest.getCode());
		}

		if (bankBranchGetRequest.getName() != null && !bankBranchGetRequest.getName().isEmpty()) {
			isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
			selectQuery.append(" bb.name = ? ");
			preparedStatementValues.add(bankBranchGetRequest.getName());
		}
		
		if (bankBranchGetRequest.getAddress() != null && !bankBranchGetRequest.getAddress().isEmpty()) {
			isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
			selectQuery.append(" bb.address = ? ");
			preparedStatementValues.add(bankBranchGetRequest.getAddress());
		}
		
		if (bankBranchGetRequest.getAddress2() != null && !bankBranchGetRequest.getAddress2().isEmpty()) {
			isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
			selectQuery.append(" bb.address2 = ? ");
			preparedStatementValues.add(bankBranchGetRequest.getAddress2());
		}
		
		if (bankBranchGetRequest.getCity() != null && !bankBranchGetRequest.getCity().isEmpty()) {
			isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
			selectQuery.append(" bb.city = ? ");
			preparedStatementValues.add(bankBranchGetRequest.getCity());
		}
		
		if (bankBranchGetRequest.getState() != null && !bankBranchGetRequest.getState().isEmpty()) {
			isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
			selectQuery.append(" bb.state = ? ");
			preparedStatementValues.add(bankBranchGetRequest.getState());
		}
		
		if (bankBranchGetRequest.getPincode() != null && !bankBranchGetRequest.getPincode().isEmpty()) {
			isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
			selectQuery.append(" bb.pincode = ? ");
			preparedStatementValues.add(bankBranchGetRequest.getPincode());
		}
		
		if (bankBranchGetRequest.getPhone() != null && !bankBranchGetRequest.getPhone().isEmpty()) {
			isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
			selectQuery.append(" bb.phone = ? ");
			preparedStatementValues.add(bankBranchGetRequest.getPhone());
		}
		
		if (bankBranchGetRequest.getFax() != null && !bankBranchGetRequest.getFax().isEmpty()) {
			isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
			selectQuery.append(" bb.fax = ? ");
			preparedStatementValues.add(bankBranchGetRequest.getFax());
		}
		
		if (bankBranchGetRequest.getContactPerson() != null && !bankBranchGetRequest.getContactPerson().isEmpty()) {
			isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
			selectQuery.append(" bb.contactPerson = ? ");
			preparedStatementValues.add(bankBranchGetRequest.getContactPerson());
		}
		
		if (bankBranchGetRequest.getActive() != null ) {
			isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
			selectQuery.append(" bb.active = ? ");
			preparedStatementValues.add(bankBranchGetRequest.getActive());
		}
		
		if (bankBranchGetRequest.getDescription() != null && !bankBranchGetRequest.getDescription().isEmpty()) {
			isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
			selectQuery.append(" bb.description = ? ");
			preparedStatementValues.add(bankBranchGetRequest.getDescription());
		}
		
		if (bankBranchGetRequest.getMicr() != null && !bankBranchGetRequest.getMicr().isEmpty()) {
			isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
			selectQuery.append(" bb.micr = ? ");
			preparedStatementValues.add(bankBranchGetRequest.getMicr());
		}

	}

	private void addOrderByClause(StringBuilder selectQuery, BankBranchGetRequest bankBranchGetRequest) {
		String sortBy = (bankBranchGetRequest.getSortBy() == null ? "bb.name  " : bankBranchGetRequest.getSortBy());
		String sortOrder = (bankBranchGetRequest.getSortOrder() == null ? "ASC" : bankBranchGetRequest.getSortOrder());
		selectQuery.append(" ORDER BY " + sortBy + " " + sortOrder);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void addPagingClause(StringBuilder selectQuery, List preparedStatementValues,
			BankBranchGetRequest bankBranchGetRequest) {
		// handle limit(also called pageSize) here
		selectQuery.append(" LIMIT ?");
		long pageSize = Integer.parseInt(applicationProperties.egfLeaveSearchPageSizeDefault());
		if (bankBranchGetRequest.getPageSize() != null)
			pageSize = bankBranchGetRequest.getPageSize();
		preparedStatementValues.add(pageSize); // Set limit to pageSize

		// handle offset here
		selectQuery.append(" OFFSET ?");
		int pageNumber = 0; // Default pageNo is zero meaning first page
		if (bankBranchGetRequest.getPageNumber() != null)
			pageNumber = bankBranchGetRequest.getPageNumber() - 1;
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
