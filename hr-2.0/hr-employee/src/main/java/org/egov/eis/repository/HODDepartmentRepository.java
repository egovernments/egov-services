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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.egov.eis.model.Assignment;
import org.egov.eis.model.HODDepartment;
import org.egov.eis.repository.rowmapper.AssignmentHodRowMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class HODDepartmentRepository {

	public static final Logger LOGGER = LoggerFactory.getLogger(HODDepartmentRepository.class);

	public static final String INSERT_HOD_DEPARTMENT_QUERY = "INSERT INTO egeis_hodDepartment"
			+ " (id, assignmentId, departmentId, tenantId)" + " VALUES (NEXTVAL('seq_egeis_hodDepartment'),?,?,?)";

	public static final String SELECT_BY_ASSIGNMENT_QUERY = "SELECT id, departmentid, tenantId FROM egeis_hodDepartment"
			+ " WHERE assignmentId = ? AND tenantId = ? ";

	public static final String CHECK_IF_ID_EXISTS_QUERY = "SELECT id FROM egeis_hodDepartment where "
			+ "assignmentId=? and departmentId=? and tenantId=?";

	public static final String DELETE_QUERY = "DELETE FROM egeis_hodDepartment"
			+ " WHERE departmentId IN (:departmentId) AND assignmentId = :assignmentId AND tenantId = :tenantId";

	public static final String DELETE_FOR_ASSIGNMENT_QUERY = "DELETE FROM egeis_hodDepartment"
			+ " WHERE assignmentId = ? AND tenantId = ?";

	public static final String DELETE_FOR_ASSIGNMENTS_QUERY = "DELETE FROM egeis_hodDepartment"
			+ " WHERE assignmentId IN (:assignmentId) AND tenantId = :tenantId";

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	private AssignmentHodRowMapper assignmentHodRowMapper;

	@Autowired
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	/**
	 * @param namedParameterJdbcTemplate
	 *            the namedParameterJdbcTemplate to set
	 */
	public void setNamedParameterJdbcTemplate(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
		this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
	}

	public void save(Assignment assignment, String tenantId) {
		List<HODDepartment> hodDepartments = assignment.getHod();

		jdbcTemplate.batchUpdate(INSERT_HOD_DEPARTMENT_QUERY, new BatchPreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				HODDepartment hodDepartment = hodDepartments.get(i);
				// ps.setLong(1, hodDepartment.getId());
				ps.setLong(1, assignment.getId());
				ps.setString(2, hodDepartment.getDepartment());
				ps.setString(3, tenantId);
			}

			@Override
			public int getBatchSize() {
				return hodDepartments.size();
			}
		});
	}

	public List<HODDepartment> findByAssignmentId(Long assignmentId, String tenantId) {
		try {
			return jdbcTemplate.query(SELECT_BY_ASSIGNMENT_QUERY, new Object[] { assignmentId, tenantId },
					assignmentHodRowMapper);
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	public void insert(Long assignmentId, String departmentId, String tenantId) {
		jdbcTemplate.update(INSERT_HOD_DEPARTMENT_QUERY, assignmentId, departmentId, tenantId);

	}

	public void delete(List<String> hodIdsToDelete, Long assignmentId, String tenantId) {
		Map<String, Object> namedParameters = new HashMap<>();
		namedParameters.put("departmentId", hodIdsToDelete);
		namedParameters.put("assignmentId", assignmentId);
		namedParameters.put("tenantId", tenantId);

		namedParameterJdbcTemplate.update(DELETE_QUERY, namedParameters);
	}

	public void delete(List<Long> assignmentsIdsToDelete, String tenantId) {
		Map<String, Object> namedParameters = new HashMap<>();
		namedParameters.put("assignmentId", assignmentsIdsToDelete);
		namedParameters.put("tenantId", tenantId);
		namedParameterJdbcTemplate.update(DELETE_FOR_ASSIGNMENTS_QUERY, namedParameters);
	}
}
