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

package org.egov.eis.repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.egov.eis.model.DepartmentalTest;
import org.egov.eis.model.enums.EntityType;
import org.egov.eis.repository.rowmapper.DepartmentalTestTableRowMapper;
import org.egov.eis.web.contract.EmployeeRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class DepartmentalTestRepository {

	public static final Logger LOGGER = LoggerFactory.getLogger(DepartmentalTestRepository.class);

	public static final String INSERT_DEPARTMENTAL_TEST_QUERY = "INSERT INTO egeis_departmentalTest"
			+ "(id, employeeId, test, yearOfPassing, remarks,"
			+ " createdBy, createdDate, lastModifiedBy, lastModifiedDate, tenantId)" + " VALUES (?,?,?,?,?,?,?,?,?,?)";

	public static final String UPDATE_DEPARTMENTAL_TEST_QUERY = "UPDATE egeis_departmentalTest"
			+ " SET (test, yearOfPassing, remarks," + " lastModifiedBy, lastModifiedDate)" + " = (?,?,?,?,?)"
			+ " WHERE id = ? and tenantId=?";

	public static final String SELECT_BY_EMPLOYEEID_QUERY = "SELECT" + " id, test, yearOfPassing, remarks,"
			+ " createdBy, createdDate, lastModifiedBy, lastModifiedDate, tenantId" + " FROM egeis_departmentalTest"
			+ " WHERE employeeId = ? AND tenantId = ? ";

	public static final String DELETE_QUERY = "DELETE FROM egeis_departmentalTest"
			+ " WHERE id IN (:id) AND employeeId = :employeeId AND tenantId = :tenantId";

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	private EmployeeDocumentsRepository documentsRepository;

	@Autowired
	private DepartmentalTestTableRowMapper departmentalTestTableRowMapper;

	@Autowired
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	/**
	 * @param namedParameterJdbcTemplate
	 *            the namedParameterJdbcTemplate to set
	 */
	public void setNamedParameterJdbcTemplate(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
		this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
	}

	public void save(EmployeeRequest employeeRequest) {
		List<DepartmentalTest> departmentalTests = employeeRequest.getEmployee().getTest();

		jdbcTemplate.batchUpdate(INSERT_DEPARTMENTAL_TEST_QUERY, new BatchPreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				DepartmentalTest departmentalTest = departmentalTests.get(i);
				ps.setLong(1, departmentalTest.getId());
				ps.setLong(2, employeeRequest.getEmployee().getId());
				ps.setString(3, departmentalTest.getTest());
				ps.setInt(4, departmentalTest.getYearOfPassing());
				ps.setString(5, departmentalTest.getRemarks());
				ps.setLong(6, employeeRequest.getRequestInfo().getUserInfo().getId());
				ps.setTimestamp(7, new Timestamp(new java.util.Date().getTime()));
				ps.setLong(8, employeeRequest.getRequestInfo().getUserInfo().getId());
				ps.setTimestamp(9, new Timestamp(new java.util.Date().getTime()));
				ps.setString(10, departmentalTest.getTenantId());

				if (departmentalTest.getDocuments() != null && !departmentalTest.getDocuments().isEmpty()) {
					documentsRepository.save(employeeRequest.getEmployee().getId(), departmentalTest.getDocuments(),
							EntityType.TEST.toString(), departmentalTest.getId(),
							departmentalTest.getTenantId());
				}
			}

			@Override
			public int getBatchSize() {
				return departmentalTests.size();
			}
		});
	}

	public void update(DepartmentalTest test) {
		Object[] obj = new Object[] { test.getTest(), test.getYearOfPassing(), test.getRemarks(),
				test.getLastModifiedBy(), test.getLastModifiedDate(), test.getId(), test.getTenantId() };

		jdbcTemplate.update(UPDATE_DEPARTMENTAL_TEST_QUERY, obj);
	}

	public void insert(DepartmentalTest test, Long empId) {
		Object[] obj = new Object[] { test.getId(), empId, test.getTest(), test.getYearOfPassing(), test.getRemarks(),
				test.getCreatedBy(), test.getCreatedDate(), test.getLastModifiedBy(), test.getLastModifiedDate(),
				test.getTenantId() };

		jdbcTemplate.update(INSERT_DEPARTMENTAL_TEST_QUERY, obj);
	}

	public List<DepartmentalTest> findByEmployeeId(Long id, String tenantId) {
		try {
			return jdbcTemplate.query(SELECT_BY_EMPLOYEEID_QUERY, new Object[] { id, tenantId },
					departmentalTestTableRowMapper);
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	public void delete(List<Long> testsIdsToDelete, Long employeeId, String tenantId) {

		Map<String, Object> namedParameters = new HashMap<>();
		namedParameters.put("id", testsIdsToDelete);
		namedParameters.put("employeeId", employeeId);
		namedParameters.put("tenantId", tenantId);

		namedParameterJdbcTemplate.update(DELETE_QUERY, namedParameters);

	}
}