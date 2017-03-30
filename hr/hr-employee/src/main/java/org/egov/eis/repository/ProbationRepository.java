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
import java.util.List;

import org.egov.eis.model.Probation;
import org.egov.eis.model.enums.DocumentReferenceType;
import org.egov.eis.web.contract.EmployeeRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class ProbationRepository {

	public static final Logger LOGGER = LoggerFactory.getLogger(ProbationRepository.class);

	public static final String INSERT_PROBATION_QUERY = "INSERT INTO egeis_probation"
			+ " (id, employeeId, designationId, declaredOn, orderNo, orderDate, remarks,"
			+ " createdBy, createdDate, lastModifiedBy, lastModifiedDate, tenantId)"
			+ " VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	private EmployeeDocumentsRepository documentsRepository;

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
				ps.setDate(6, (probation.getOrderDate() == null ? null
						: new Date(probation.getOrderDate().getTime())));
				ps.setString(7, probation.getRemarks());
				ps.setLong(8, Long.parseLong(employeeRequest.getRequestInfo().getRequesterId()));
				ps.setTimestamp(9, new Timestamp(new java.util.Date().getTime()));
				ps.setLong(10, Long.parseLong(employeeRequest.getRequestInfo().getRequesterId()));
				ps.setTimestamp(11, new Timestamp(new java.util.Date().getTime()));
				ps.setString(12, probation.getTenantId());

				if(probation.getDocuments() != null && !probation.getDocuments().isEmpty()) {
					documentsRepository.save(employeeRequest.getEmployee().getId(), probation.getDocuments(),
							DocumentReferenceType.PROBATION.toString(), probation.getId(), probation.getTenantId());
				}
			}

			@Override
			public int getBatchSize() {
				return probations.size();
			}
		});
	}
	
	
}