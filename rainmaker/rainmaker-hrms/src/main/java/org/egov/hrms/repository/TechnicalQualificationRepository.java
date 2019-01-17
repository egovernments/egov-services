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

import org.egov.hrms.model.TechnicalQualification;
import org.egov.hrms.model.enums.EntityType;
import org.egov.hrms.repository.helper.PreparedStatementHelper;
import org.egov.hrms.repository.rowmapper.TechnicalQualificationRowMapper;
import org.egov.hrms.web.contract.EmployeeRequest;
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
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class TechnicalQualificationRepository {

	public static final Logger LOGGER = LoggerFactory.getLogger(TechnicalQualificationRepository.class);

	public static final String INSERT_TECHNICAL_QUALIFICATION_QUERY = "INSERT INTO egeis_technicalQualification"
			+ " (id, employeeId, skill, grade, yearOfPassing, remarks,"
			+ " createdBy, createdDate, lastModifiedBy, lastModifiedDate, tenantId)"
			+ " VALUES (?,?,?,?,?,?,?,?,?,?,?)";

	public static final String UPDATE_TECHNICAL_QUALIFICATION_QUERY = "UPDATE egeis_technicalQualification"
			+ " SET (skill, grade, yearOfPassing, remarks,"
			+ " lastModifiedBy, lastModifiedDate)"
			+ " = (?,?,?,?,?,?)"
			+" WHERE id = ? and tenantId=?";

	public static final String SELECT_BY_EMPLOYEEID_QUERY = "SELECT"
			+ " id, skill, grade, yearofpassing, remarks, createdby, createddate,"
			+ " lastmodifiedby,lastmodifieddate, tenantid"
			+ " FROM egeis_technicalqualification"
			+ " WHERE employeeId = ? AND tenantId = ? ";
	
    public static final String DELETE_QUERY = "DELETE FROM egeis_technicalqualification"
			+ " WHERE id IN (:id) AND employeeId = :employeeId AND tenantId = :tenantId";
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Autowired
	private TechnicalQualificationRowMapper technicalQualificationRowMapper;

	@Autowired
	private PreparedStatementHelper psHelper;

	@Autowired
	private EmployeeDocumentsRepository documentsRepository;
	
	@Autowired
	 private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	 
	 /**
	  * @param namedParameterJdbcTemplate the namedParameterJdbcTemplate to set
	  */
	 public void setNamedParameterJdbcTemplate(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
	  this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
	 }


	public void save(EmployeeRequest employeeRequest) {
		List<TechnicalQualification> technicalQualifications = employeeRequest.getEmployee().getTechnical();

		jdbcTemplate.batchUpdate(INSERT_TECHNICAL_QUALIFICATION_QUERY, new BatchPreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				TechnicalQualification technicalQualification = technicalQualifications.get(i);
				ps.setLong(1, technicalQualification.getId());
				ps.setLong(2, employeeRequest.getEmployee().getId());
				ps.setString(3, technicalQualification.getSkill());
				ps.setString(4, technicalQualification.getGrade());
				psHelper.setIntegerOrNull(ps, 5, technicalQualification.getYearOfPassing());
				ps.setString(6, technicalQualification.getRemarks());
				ps.setLong(7, employeeRequest.getRequestInfo().getUserInfo().getId());
				ps.setTimestamp(8, new Timestamp(new java.util.Date().getTime()));
				ps.setLong(9, employeeRequest.getRequestInfo().getUserInfo().getId());
				ps.setTimestamp(10, new Timestamp(new java.util.Date().getTime()));
				ps.setString(11, technicalQualification.getTenantId());

				if(technicalQualification.getDocuments() != null && !technicalQualification.getDocuments().isEmpty()) {
					documentsRepository.save(employeeRequest.getEmployee().getId(), technicalQualification.getDocuments(),
							EntityType.TECHNICAL.toString(), technicalQualification.getId(), technicalQualification.getTenantId());
				}
			}

			@Override
			public int getBatchSize() {
				return technicalQualifications.size();
			}
		});
	}

	public void update(TechnicalQualification technical) {
		Object[] obj = new Object[] { technical.getSkill(), technical.getGrade(), technical.getYearOfPassing(),
				technical.getRemarks(), technical.getLastModifiedBy(), technical.getLastModifiedDate(),
				technical.getId(), technical.getTenantId() };

		jdbcTemplate.update(UPDATE_TECHNICAL_QUALIFICATION_QUERY, obj);

	}

	public void insert(TechnicalQualification technical, Long empId) {
		Object[] obj = new Object[] { technical.getId(), empId, technical.getSkill(), technical.getGrade(),
				technical.getYearOfPassing(), technical.getRemarks(), technical.getCreatedBy(),
				technical.getCreatedDate(), technical.getLastModifiedBy(), technical.getLastModifiedDate(),
				technical.getTenantId() };

		jdbcTemplate.update(INSERT_TECHNICAL_QUALIFICATION_QUERY, obj);
	}

	public List<TechnicalQualification> findByEmployeeId(Long id, String tenantId) {
		try {
			return jdbcTemplate.query(SELECT_BY_EMPLOYEEID_QUERY, new Object[] { id, tenantId },
					technicalQualificationRowMapper);
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	public void delete(List<Long> technicalsIdsToDelete, Long employeeId, String tenantId) {
		
		Map<String, Object> namedParameters = new HashMap<>();
		namedParameters.put("id", technicalsIdsToDelete );
		namedParameters.put("employeeId", employeeId);
		namedParameters.put("tenantId", tenantId);
		
		namedParameterJdbcTemplate.update(DELETE_QUERY, namedParameters);
		
	}
}