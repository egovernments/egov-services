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
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.egov.eis.model.Regularisation;
import org.egov.eis.model.ServiceHistory;
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
public class RegularisationRepository {

	public static final Logger LOGGER = LoggerFactory.getLogger(RegularisationRepository.class);

	public static final String INSERT_REGULARISATION_QUERY = "INSERT INTO egeis_regularisation"
			+ " (id, employeeId, designationId, declaredOn, orderNo, orderDate, remarks,"
			+ " createdBy, createdDate, lastModifiedBy, lastModifiedDate, tenantId)"
			+ " VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";
	
	public static final String UPDATE_REGULARISATION_QUERY = "UPDATE egeis_regularisation"
			+ " SET (designationId, declaredOn, orderNo, orderDate, remarks,"
			+ " lastModifiedBy, lastModifiedDate)"
			+ "= (?,?,?,?,?,?,?)"
			+"where id = ?";

	public static final String CHECK_IF_ID_EXISTS_QUERY = "SELECT id FROM egeis_regularisation where "
			+ "id=? and employeeId=? and tenantId=?";

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	private EmployeeDocumentsRepository documentsRepository;

	public void save(EmployeeRequest employeeRequest) {
		List<Regularisation> regularisations = employeeRequest.getEmployee().getRegularisation();

		jdbcTemplate.batchUpdate(INSERT_REGULARISATION_QUERY, new BatchPreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				Regularisation regularisation = regularisations.get(i);
				ps.setLong(1, regularisation.getId());
				ps.setLong(2, employeeRequest.getEmployee().getId());
				ps.setLong(3, regularisation.getDesignation());
				ps.setDate(4, new Date(regularisation.getDeclaredOn().getTime()));
				ps.setString(5, regularisation.getOrderNo());
				ps.setDate(6, (regularisation.getOrderDate() == null ? null
						: new Date(regularisation.getOrderDate().getTime())));
				ps.setString(7, regularisation.getRemarks());
				ps.setLong(8, Long.parseLong(employeeRequest.getRequestInfo().getRequesterId()));
				ps.setTimestamp(9, new Timestamp(new java.util.Date().getTime()));
				ps.setLong(10, Long.parseLong(employeeRequest.getRequestInfo().getRequesterId()));
				ps.setTimestamp(11, new Timestamp(new java.util.Date().getTime()));
				ps.setString(12, regularisation.getTenantId());

				if(regularisation.getDocuments() != null && !regularisation.getDocuments().isEmpty()) {
					documentsRepository.save(employeeRequest.getEmployee().getId(), regularisation.getDocuments(),
							DocumentReferenceType.REGULARISATION.toString(), regularisation.getId(), regularisation.getTenantId());
				}
			}

			@Override
			public int getBatchSize() {
				return regularisations.size();
			}
		});
	}

	public void update(Regularisation regularisation) {

		Object[] obj = new Object[] { regularisation.getDesignation(), regularisation.getDeclaredOn(),
				regularisation.getOrderNo(), regularisation.getOrderDate(), regularisation.getRemarks(),
				regularisation.getLastModifiedBy(), regularisation.getLastModifiedDate(),
				regularisation.getId() };

		jdbcTemplate.update(UPDATE_REGULARISATION_QUERY, obj);

	}

	public void insert(Regularisation regularisation, Long empId) {
		Object[] obj = new Object[] { regularisation.getId(), empId, regularisation.getDesignation(),
				regularisation.getDeclaredOn(), regularisation.getOrderNo(), regularisation.getOrderDate(),
				regularisation.getRemarks(), regularisation.getCreatedBy(), regularisation.getCreatedDate(),
				regularisation.getLastModifiedBy(), regularisation.getLastModifiedDate(), regularisation.getTenantId(),
				 };

		jdbcTemplate.update(INSERT_REGULARISATION_QUERY, obj);
	}

	public boolean regularisationAlreadyExists(Long id, Long empId, String tenantId) {
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

	public void findAndDeleteRegularisationInDBThatAreNotInList(List<Regularisation> regularisation) {
		// TODO Auto-generated method stub
		
	}
}