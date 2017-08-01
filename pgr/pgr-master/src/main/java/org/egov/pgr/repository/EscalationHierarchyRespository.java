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
package org.egov.pgr.repository;

import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.egov.pgr.domain.model.AuditDetails;
import org.egov.pgr.domain.model.EscalationHierarchy;
import org.egov.pgr.repository.builder.EscalationHierarchyQueryBuilder;
import org.egov.pgr.repository.rowmapper.EscalationHierarchyRowMapper;
import org.egov.pgr.web.contract.EscalationHierarchyGetReq;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class EscalationHierarchyRespository {
	
	public static final Logger LOGGER = LoggerFactory.getLogger(EscalationHierarchyRespository.class);

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Autowired
	private EscalationHierarchyQueryBuilder escalationHierarchyQueryBuilder;

	@Autowired
	private EscalationHierarchyRowMapper escalationHierarchyRowMapper;

	public List<EscalationHierarchy> persistEscalationHierarchy(final List<EscalationHierarchy> escalationHierarchyList, final AuditDetails auditDetails ) {
		LOGGER.info("EscalationHierarchy::" + escalationHierarchyList);
		
		final String escalationHierarchyInsert = escalationHierarchyQueryBuilder.insertEscalationHierarchy();
		try {
			jdbcTemplate.batchUpdate(escalationHierarchyInsert ,
					new BatchPreparedStatementSetter() {
						@Override
						public void setValues(java.sql.PreparedStatement statement, int i) throws SQLException {
							EscalationHierarchy eachEscHierarchy = escalationHierarchyList.get(i);
							statement.setString(1, eachEscHierarchy.getTenantId());
							statement.setLong(2, eachEscHierarchy.getFromPosition());
							statement.setLong(3, eachEscHierarchy.getToPosition());
							statement.setString(4, eachEscHierarchy.getServiceCode());
							statement.setLong(5, (null == eachEscHierarchy.getDepartment())? 0L : eachEscHierarchy.getDepartment());
							statement.setLong(6, (null == eachEscHierarchy.getDesignation())? 0L : eachEscHierarchy.getDesignation());
							statement.setLong(7, auditDetails.getCreatedBy());
							statement.setDate(8, new Date(new java.util.Date().getTime()));
						}

						@Override
						public int getBatchSize() {
							return escalationHierarchyList.size();
						}
					});
		} catch (Exception ex) {
			LOGGER.error("Encountered an Exception :" + ex.getMessage());
		}
		return escalationHierarchyList;
	}
	
	public void deleteEscalationHierarchy(final List<EscalationHierarchy> escalationHierarchyList) {
		LOGGER.info("EscalationHierarchy::" + escalationHierarchyList);
		String deleteQuery = escalationHierarchyQueryBuilder.deleteEscalationHierarchy(escalationHierarchyList);
		jdbcTemplate.update(deleteQuery);
	}
	
	public List<EscalationHierarchy> getAllEscalationHierarchy(EscalationHierarchyGetReq escHierarchyGetRequest) {
		LOGGER.info("EscalationHierarchy::" + escHierarchyGetRequest);
		final List<Object> preparedStatementValues = new ArrayList<>();
		String getQuery = escalationHierarchyQueryBuilder.getQuery(escHierarchyGetRequest, preparedStatementValues);
		LOGGER.info("Query for Get All Escalation Hierarchy ::" + getQuery);
		final List<EscalationHierarchy> escalationHierarchies = jdbcTemplate.query(getQuery,
				preparedStatementValues.toArray(), escalationHierarchyRowMapper);
		return escalationHierarchies;
	}

}
