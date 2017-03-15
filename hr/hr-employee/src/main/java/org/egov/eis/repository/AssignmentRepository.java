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

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.egov.eis.model.Assignment;
import org.egov.eis.model.Employee;
import org.egov.eis.repository.builder.AssignmentQueryBuilder;
import org.egov.eis.repository.rowmapper.AssignmentRowMapper;
import org.egov.eis.web.contract.AssignmentGetRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class AssignmentRepository {

	public static final Logger LOGGER = LoggerFactory.getLogger(AssignmentRepository.class);

	// FIXME Assignment Sequence, Employee ID
	public static final String INSERT_ASSIGNMENT_QUERY = "INSERT INTO egeis_assignment"
			+ " (id, employeeId, positionId, fundId, functionaryId, functionId, departmentId, designationId,"
			+ " isprimary, fromdate, todate, gradeId, govtordernumber, createdby, createddate,"
			+ " lastmodifiedby, lastmodifieddate, tenantId)"
			+ " VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	private AssignmentRowMapper assignmentRowMapper;

	@Autowired
	private AssignmentQueryBuilder assignmentQueryBuilder;

	public List<Assignment> findForCriteria(Long employeeId, AssignmentGetRequest assignmentGetRequest) {
		List<Object> preparedStatementValues = new ArrayList<Object>();
		String queryStr = assignmentQueryBuilder.getQuery(employeeId, assignmentGetRequest, preparedStatementValues);
		List<Assignment> assignments = jdbcTemplate.query(queryStr, preparedStatementValues.toArray(),
				assignmentRowMapper);
		return assignments;
	}

	// FIXME put tenantId
	public void save(Employee employee) {
		List<Assignment> assignments = employee.getAssignments();

		jdbcTemplate.batchUpdate(INSERT_ASSIGNMENT_QUERY, new BatchPreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				Assignment assignment = assignments.get(i);
				ps.setLong(1, assignment.getId());
				ps.setLong(2, employee.getId());
				ps.setLong(3, assignment.getPosition());
				ps.setLong(4, assignment.getFund());
				ps.setLong(5, assignment.getFunctionary());
				ps.setLong(6, assignment.getFunction());
				ps.setLong(7, assignment.getDepartment());
				ps.setLong(8, assignment.getDesignation());
				ps.setBoolean(9, assignment.getIsPrimary());
				ps.setDate(10, new Date(assignment.getFromDate().getTime()));
				ps.setDate(11, new Date(assignment.getToDate().getTime()));
				ps.setLong(12, assignment.getGrade());
				ps.setString(13, assignment.getGovtOrderNumber());
				ps.setLong(14, assignment.getCreatedBy());
				ps.setDate(15, new Date(assignment.getCreatedDate().getTime()));
				ps.setLong(16, assignment.getLastModifiedBy());
				ps.setDate(17, new Date(assignment.getLastModifiedDate().getTime()));
				ps.setString(18, employee.getTenantId());
			}

			@Override
			public int getBatchSize() {
				return assignments.size();
			}
		});
	}
}