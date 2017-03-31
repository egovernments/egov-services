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

import java.io.StringWriter;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.egov.eis.model.DepartmentalTest;
import org.egov.eis.model.enums.DocumentReferenceType;
import org.egov.eis.web.contract.EmployeeRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class DepartmentalTestRepository {

	public static final Logger LOGGER = LoggerFactory.getLogger(DepartmentalTestRepository.class);

	public static final String INSERT_DEPARTMENTAL_TEST_QUERY = "INSERT INTO egeis_departmentalTest"
			+ "(id, employeeId, test, yearOfPassing, remarks,"
			+ " createdBy, createdDate, lastModifiedBy, lastModifiedDate, tenantId)" + " VALUES (?,?,?,?,?,?,?,?,?,?)";

	public static final String UPDATE_DEPARTMENTAL_TEST_QUERY = "UPDATE egeis_departmentalTest"
			+ " SET (test, yearOfPassing, remarks," + " lastModifiedBy, lastModifiedDate)"
			+ " = (?,?,?,?,?)" + " WHERE id = ?";

	public static final String CHECK_IF_ID_EXISTS_QUERY = "SELECT id FROM egeis_departmentaltest where "
			+ "id=? and employeeId=? and tenantId=?";
	
	public static final String DELETE_QUERY = "DELETE FROM egeis_departmentaltest where "
			+ "id=? and employeeId=? and tenantId=?";
	
	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	private EmployeeDocumentsRepository documentsRepository;

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
				ps.setLong(6, Long.parseLong(employeeRequest.getRequestInfo().getRequesterId()));
				ps.setTimestamp(7, new Timestamp(new java.util.Date().getTime()));
				ps.setLong(8, Long.parseLong(employeeRequest.getRequestInfo().getRequesterId()));
				ps.setTimestamp(9, new Timestamp(new java.util.Date().getTime()));
				ps.setString(10, departmentalTest.getTenantId());

				if (departmentalTest.getDocuments() != null && !departmentalTest.getDocuments().isEmpty()) {
					documentsRepository.save(employeeRequest.getEmployee().getId(), departmentalTest.getDocuments(),
							DocumentReferenceType.TEST.toString(), departmentalTest.getId(),
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
				test.getLastModifiedBy(), test.getLastModifiedDate(),test.getId() };

		jdbcTemplate.update(UPDATE_DEPARTMENTAL_TEST_QUERY, obj);
	}

	public void insert(DepartmentalTest test, Long empId) {
		Object[] obj = new Object[] { test.getId(), empId, test.getTest(), test.getYearOfPassing(), test.getRemarks(),
				test.getCreatedBy(), test.getCreatedDate(), test.getLastModifiedBy(), test.getLastModifiedDate(),
				test.getTenantId() };

		jdbcTemplate.update(INSERT_DEPARTMENTAL_TEST_QUERY, obj);
	}
	
	public void findAndDeleteThatAreNotInList(List<DepartmentalTest> test) {
/*		for(test) -> {
		});
		
		StringWriter stringWriter = new StringWriter();
		Long id[] = 
		String[] b = new String[a.length];
		for ( int i = 0; i < a.length; i++) {
		    b[i] = a[i];
		}
		CSVWriter csvWriter = new CSVWriter(stringWriter, ",");
		csvWriter.writeNext(b);*/
		// TODO Auto-generated method stub
		
		// DELETE FROM egeis_departmentaltest where empid=? and id NOT IN <<loop probation and get all ids in csv>>)
	}

	public boolean testAlreadyExists(Long id, Long empId, String tenantId) {
		List<Object> values = new ArrayList<Object>();
		values.add(id);
		values.add(empId);
		values.add(tenantId);
		try {
			jdbcTemplate.queryForObject(CHECK_IF_ID_EXISTS_QUERY, values.toArray(), Long.class);
			return true;
		} catch (EmptyResultDataAccessException e) {
			return false;
		}
	}
}