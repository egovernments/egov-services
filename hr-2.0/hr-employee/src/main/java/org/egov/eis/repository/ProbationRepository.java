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

import org.egov.eis.model.Probation;
import org.egov.eis.model.enums.EntityType;
import org.egov.eis.repository.rowmapper.ProbationTableRowMapper;
import org.egov.eis.web.contract.EmployeeRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class ProbationRepository {

	public static final String INSERT_PROBATION_QUERY = "INSERT INTO egeis_probation"
			+ " (id, employeeId, designationId, declaredOn, orderNo, orderDate, remarks, createdBy, createdDate,"
			+ " lastModifiedBy, lastModifiedDate, tenantId) VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";

	public static final String UPDATE_PROBATION_QUERY = "UPDATE egeis_probation"
			+ " SET (designationId, declaredOn, orderNo, orderDate, remarks, lastModifiedBy, lastModifiedDate)"
			+ " = (?,?,?,?,?,?,?) WHERE id = ? and tenantId=?";

	public static final String SELECT_BY_EMPLOYEEID_QUERY = "SELECT"
			+ " id, designationid, declaredon, orderno, orderdate, remarks, createdby, createddate, lastmodifiedby,"
			+ " lastmodifieddate,tenantId FROM egeis_probation WHERE employeeId = ? AND tenantId = ? ";

	public static final String DELETE_QUERY = "DELETE FROM egeis_probation"
			+ " WHERE id IN (:id) AND employeeId = :employeeId AND tenantId = :tenantId";
	
	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	private EmployeeDocumentsRepository documentsRepository;
	
	@Autowired
	private ProbationTableRowMapper probationRowMapper;
	
	@Autowired
	 private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	 
	 /**
	  * @param namedParameterJdbcTemplate the namedParameterJdbcTemplate to set
	  */
	 public void setNamedParameterJdbcTemplate(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
	  this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
	 }


	public void save(EmployeeRequest employeeRequest) {
		List<Probation> probations = employeeRequest.getEmployee().getProbation();

		jdbcTemplate.batchUpdate(INSERT_PROBATION_QUERY, new BatchPreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				Probation probation = probations.get(i);
				ps.setLong(1, probation.getId());
				ps.setLong(2, employeeRequest.getEmployee().getId());
				ps.setLong(3, probation.getDesignation());
				ps.setDate(4, new Date(probation.getDeclaredOn().getTime()));
				ps.setString(5, probation.getOrderNo());
				ps.setDate(6, (probation.getOrderDate() == null ? null : new Date(probation.getOrderDate().getTime())));
				ps.setString(7, probation.getRemarks());
				ps.setLong(8, employeeRequest.getRequestInfo().getUserInfo().getId());
				ps.setTimestamp(9, new Timestamp(new java.util.Date().getTime()));
				ps.setLong(10, employeeRequest.getRequestInfo().getUserInfo().getId());
				ps.setTimestamp(11, new Timestamp(new java.util.Date().getTime()));
				ps.setString(12, probation.getTenantId());

				if (probation.getDocuments() != null && !probation.getDocuments().isEmpty()) {
					documentsRepository.save(employeeRequest.getEmployee().getId(), probation.getDocuments(),
							EntityType.PROBATION.toString(), probation.getId(), probation.getTenantId());
				}
			}

			@Override
			public int getBatchSize() {
				return probations.size();
			}
		});
	}

	public void update(Probation probation) {

		Object[] obj = new Object[] { probation.getDesignation(), probation.getDeclaredOn(), probation.getOrderNo(),
				probation.getOrderDate(), probation.getRemarks(), probation.getLastModifiedBy(),
				probation.getLastModifiedDate(), probation.getId(), probation.getTenantId() };

		jdbcTemplate.update(UPDATE_PROBATION_QUERY, obj);
	}

	public void insert(Probation probation, Long empId) {
		Object[] obj = new Object[] { probation.getId(), empId, probation.getDesignation(), probation.getDeclaredOn(),
				probation.getOrderNo(), probation.getOrderDate(), probation.getRemarks(), probation.getCreatedBy(),
				probation.getCreatedDate(), probation.getLastModifiedBy(), probation.getLastModifiedDate(),
				probation.getTenantId() };

		jdbcTemplate.update(INSERT_PROBATION_QUERY, obj);
	}

	public List<Probation> findByEmployeeId(Long id, String tenantId) {
		try {
			return jdbcTemplate.query(SELECT_BY_EMPLOYEEID_QUERY, new Object[] { id, tenantId }, probationRowMapper);
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	public void delete(List<Long> probationIdsToDelete, Long employeeId, String tenantId) {
		 
		Map<String, Object> namedParameters = new HashMap<>();
		namedParameters.put("id", probationIdsToDelete );
		namedParameters.put("employeeId", employeeId);
		namedParameters.put("tenantId", tenantId);
		
		namedParameterJdbcTemplate.update(DELETE_QUERY, namedParameters);
	}
}