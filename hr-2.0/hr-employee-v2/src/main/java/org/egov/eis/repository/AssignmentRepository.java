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

import org.egov.eis.model.Assignment;
import org.egov.eis.model.enums.EntityType;
import org.egov.eis.repository.builder.AssignmentQueryBuilder;
import org.egov.eis.repository.helper.PreparedStatementHelper;
import org.egov.eis.repository.rowmapper.AssignmentRowMapper;
import org.egov.eis.repository.rowmapper.AssignmentTableRowMapper;
import org.egov.eis.repository.rowmapper.EmployeeIdsRowMapper;
import org.egov.eis.web.contract.AssignmentGetRequest;
import org.egov.eis.web.contract.EmployeeRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
@Transactional
public class AssignmentRepository {

	public static final Logger LOGGER = LoggerFactory.getLogger(AssignmentRepository.class);

	public static final String INSERT_ASSIGNMENT_QUERY = "INSERT INTO egeis_assignment"
			+ " (id, employeeId, positionId, fundId, functionaryId, functionId, departmentId, designationId,"
			+ " isPrimary, fromDate, toDate, gradeId, govtOrderNumber, createdBy, createdDate,"
			+ " lastModifiedBy, lastModifiedDate, tenantId)" + " VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

	public static final String UPDATE_ASSIGNMENT_QUERY = "UPDATE egeis_assignment"
			+ " SET (positionId, fundId, functionaryId, functionId, departmentId, designationId,"
			+ " isPrimary, fromDate, toDate, gradeId, govtOrderNumber, lastModifiedBy, lastModifiedDate)"
			+ " = (?,?,?,?,?,?,?,?,?,?,?,?,?) WHERE id = ? AND tenantId=?";

	public static final String SELECT_BY_EMPLOYEEID_QUERY = "SELECT"
			+ " id, positionId, fundId, functionaryId, functionId, departmentId, designationId,"
			+ " isPrimary, fromDate, toDate, gradeId, govtOrderNumber, createdBy, createdDate, lastModifiedBy,"
			+ " lastModifiedDate, tenantId FROM egeis_assignment WHERE employeeId = ? AND tenantId = ? ";

	public static final String DELETE_QUERY = "DELETE FROM egeis_assignment"
			+ " WHERE id IN (:id) AND employeeId = :employeeId AND tenantId = :tenantId";

	public static final String ASSIGNMENTS_OPERLAPPING_CHECK_QUERY = "SELECT exists(SELECT FROM egeis_assignment"
			+ " WHERE ((:fromDate BETWEEN fromDate AND toDate) OR (:toDate BETWEEN fromDate AND toDate)"
			+ " OR (fromDate BETWEEN :fromDate AND :toDate) OR (toDate BETWEEN :fromDate AND :toDate))"
			+ " AND id != :id AND isPrimary = true AND employeeId = :employeeId AND tenantId = :tenantId)";

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	private EmployeeIdsRowMapper employeeIdsRowMapper;

	@Autowired
	private AssignmentTableRowMapper assignmentTableRowMapper;

	@Autowired
	private AssignmentRowMapper assignmentRowMapper;

	@Autowired
	private AssignmentQueryBuilder assignmentQueryBuilder;

	@Autowired
	private PreparedStatementHelper psHelper;

	@Autowired
	private EmployeeDocumentsRepository documentsRepository;

	@Autowired
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	/**
	 * @param namedParameterJdbcTemplate
	 *            the namedParameterJdbcTemplate to set
	 */
	public void setNamedParameterJdbcTemplate(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
		this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
	}

	@Transactional(readOnly = true)
	public List<Assignment> findForCriteria(Long employeeId, AssignmentGetRequest assignmentGetRequest) {
		Map<String, Object> namedParametersForListOfAssignmentIds = new HashMap<>();
		String queryStrForListOfAssignmentIds = assignmentQueryBuilder.getQueryForListOfAssignmentIds(employeeId,
				assignmentGetRequest, namedParametersForListOfAssignmentIds);

		List<Long> listOfIds = namedParameterJdbcTemplate.query(queryStrForListOfAssignmentIds,
				namedParametersForListOfAssignmentIds, employeeIdsRowMapper);

		if (listOfIds.isEmpty()) {
			return Collections.emptyList();
		}

		Map<String, Object> namedParameters = new HashMap<>();
		String queryStr = assignmentQueryBuilder.getQuery(employeeId, assignmentGetRequest, namedParameters, listOfIds);
		List<Assignment> assignments = namedParameterJdbcTemplate.query(queryStr, namedParameters, assignmentRowMapper);
		return assignments;
	}

	public void save(EmployeeRequest employeeRequest) {
		List<Assignment> assignments = employeeRequest.getEmployee().getAssignments();

		jdbcTemplate.batchUpdate(INSERT_ASSIGNMENT_QUERY, new BatchPreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				Assignment assignment = assignments.get(i);
				ps.setLong(1, assignment.getId());
				ps.setLong(2, employeeRequest.getEmployee().getId());
				psHelper.setLongOrNull(ps, 3, assignment.getPosition());
				psHelper.setLongOrNull(ps, 4, assignment.getFund());
				psHelper.setLongOrNull(ps, 5, assignment.getFunctionary());
				psHelper.setLongOrNull(ps, 6, assignment.getFunction());
				ps.setString(7, assignment.getDepartment());
                ps.setString(8, assignment.getDesignation());
				ps.setBoolean(9, assignment.getIsPrimary());
				ps.setDate(10, new Date(assignment.getFromDate().getTime()));
				ps.setDate(11, new Date(assignment.getToDate().getTime()));
				psHelper.setLongOrNull(ps, 12, assignment.getGrade());
				ps.setString(13, assignment.getGovtOrderNumber());
				ps.setLong(14, employeeRequest.getRequestInfo().getUserInfo().getId());
				ps.setTimestamp(15, new Timestamp(new java.util.Date().getTime()));
				ps.setLong(16, employeeRequest.getRequestInfo().getUserInfo().getId());
				ps.setTimestamp(17, new Timestamp(new java.util.Date().getTime()));
				ps.setString(18, assignment.getTenantId());

				if (assignment.getDocuments() != null && !assignment.getDocuments().isEmpty()) {
					documentsRepository.save(employeeRequest.getEmployee().getId(), assignment.getDocuments(),
							EntityType.ASSIGNMENT.toString(), assignment.getId(), assignment.getTenantId());
				}
			}

			@Override
			public int getBatchSize() {
				return assignments.size();
			}
		});
	}

	public void update(Assignment assignment) {

		Object[] obj = new Object[] { assignment.getPosition(), assignment.getFund(), assignment.getFunctionary(),
				assignment.getFunction(), assignment.getDepartment(), assignment.getDesignation(),
				assignment.getIsPrimary(), assignment.getFromDate(), assignment.getToDate(), assignment.getGrade(),
				assignment.getGovtOrderNumber(), assignment.getLastModifiedBy(), assignment.getLastModifiedDate(),
				assignment.getId(), assignment.getTenantId() };

		jdbcTemplate.update(UPDATE_ASSIGNMENT_QUERY, obj);
	}

	public void insert(Assignment assignment, Long empId) {

		Object[] obj = new Object[] { assignment.getId(), empId, assignment.getPosition(), assignment.getFund(),
				assignment.getFunctionary(), assignment.getFunction(), assignment.getDepartment(),
				assignment.getDesignation(), assignment.getIsPrimary(), assignment.getFromDate(),
				assignment.getToDate(), assignment.getGrade(), assignment.getGovtOrderNumber(),
				assignment.getCreatedBy(), assignment.getCreatedDate(), assignment.getLastModifiedBy(),
				assignment.getLastModifiedDate(), assignment.getTenantId() };

		jdbcTemplate.update(INSERT_ASSIGNMENT_QUERY, obj);
	}

	public List<Assignment> findByEmployeeId(Long id, String tenantId) {
		try {
			return jdbcTemplate.query(SELECT_BY_EMPLOYEEID_QUERY, new Object[] { id, tenantId },
					assignmentTableRowMapper);
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	public void delete(List<Long> assignmentsIdsToDelete, Long employeeId, String tenantId) {
		Map<String, Object> namedParameters = new HashMap<>();
		namedParameters.put("id", assignmentsIdsToDelete);
		namedParameters.put("employeeId", employeeId);
		namedParameters.put("tenantId", tenantId);

		namedParameterJdbcTemplate.update(DELETE_QUERY, namedParameters);
	}

    public Boolean assignmentsOverlapping(Assignment assignment, Long employeeId, String tenantId) {
		Map<String, Object> psValuesMap = new HashMap<>();
		psValuesMap.put("id", assignment.getId());
		psValuesMap.put("fromDate", assignment.getFromDate());
		psValuesMap.put("toDate", assignment.getToDate());
		psValuesMap.put("employeeId", employeeId);
		psValuesMap.put("tenantId", tenantId);
		return namedParameterJdbcTemplate.queryForObject(ASSIGNMENTS_OPERLAPPING_CHECK_QUERY, psValuesMap, Boolean.class);
    }
}