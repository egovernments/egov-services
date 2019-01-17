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

package org.egov.hrms.repository;

import org.egov.hrms.model.EmployeeDocument;
import org.egov.hrms.model.enums.EntityType;
import org.egov.hrms.repository.rowmapper.EmployeeDocumentsTableRowMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class EmployeeDocumentsRepository {

	public static final Logger LOGGER = LoggerFactory.getLogger(EmployeeDocumentsRepository.class);

	public static final String INSERT_EMPLOYEE_DOCUMENTS_QUERY = "INSERT INTO egeis_employeeDocuments"
			+ " (id, employeeId, document, referenceType, referenceId, tenantId)"
			+ " VALUES (nextval('seq_egeis_employeeDocuments'),?,?,?,?,?)";

	public static final String DELETE_QUERY = "DELETE FROM egeis_employeeDocuments"
			+ " WHERE employeeId = ? AND document = ? AND referenceType = ? AND referenceId =? AND tenantId = ?";
	
	public static final String DELETE_QUERY_FOR_REFERENCE_IDS = "DELETE FROM egeis_employeeDocuments"
			+ " WHERE employeeId = :employeeId AND referenceType = :referenceType AND referenceId IN (:referenceIds)"
			+ " AND tenantId = :tenantId";

	public static final String SELECT_BY_EMPLOYEEID_QUERY = "SELECT id, document, referencetype, referenceid, tenantId"
			+ " FROM egeis_employeeDocuments WHERE employeeId = ? AND tenantId = ? ";

	@Autowired
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	
	/**
	 * @param namedParameterJdbcTemplate
	 *            the namedParameterJdbcTemplate to set
	 */
	public void setNamedParameterJdbcTemplate(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
		this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
	}

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	private EmployeeDocumentsTableRowMapper employeeDocumentsTableRowMapper;

	public void save(Long employeeId, List<String> documents, String referenceType, Long referenceId, String tenantId) {
		jdbcTemplate.batchUpdate(INSERT_EMPLOYEE_DOCUMENTS_QUERY, new BatchPreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				ps.setLong(1, employeeId);
				ps.setString(2, documents.get(i));
				ps.setString(3, referenceType);
				ps.setLong(4, referenceId);
				ps.setString(5, tenantId);
			}

			@Override
			public int getBatchSize() {
				return documents.size();
			}
		});
	}

	public int save(Long employeeId, String docUrl, String referenceType, Long referenceID, String tenantId) {
		return jdbcTemplate.update(INSERT_EMPLOYEE_DOCUMENTS_QUERY, employeeId, docUrl, referenceType, referenceID, tenantId);
	}

	public int delete(Long employeeId, String document, String referenceType, Long referenceID, String tenantId) {
		return jdbcTemplate.update(DELETE_QUERY, employeeId, document, referenceType, referenceID, tenantId);
	}
	
	public int deleteForReferenceIds(Long employeeId, EntityType entityType, List<Long> referenceIds, String tenantId) {
		String referenceType = entityType.getValue();
		Map<String, Object> namedParameters = new HashMap<>();
		namedParameters.put("employeeId", employeeId);
		namedParameters.put("referenceType", referenceType);
		namedParameters.put("referenceIds", referenceIds);
		namedParameters.put("tenantId", tenantId);
		
		return namedParameterJdbcTemplate.update(DELETE_QUERY_FOR_REFERENCE_IDS, namedParameters);
	}

	public List<EmployeeDocument> findByEmployeeId(Long id, String tenantId) {
		try {
			return jdbcTemplate.query(SELECT_BY_EMPLOYEEID_QUERY, new Object[] { id, tenantId },
					employeeDocumentsTableRowMapper);
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

}