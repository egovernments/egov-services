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

import org.egov.eis.model.ServiceHistory;
import org.egov.eis.model.enums.DocumentReferenceType;
import org.egov.eis.web.contract.EmployeeRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class ServiceHistoryRepository {

	public static final Logger LOGGER = LoggerFactory.getLogger(ServiceHistoryRepository.class);

	public static final String INSERT_SERVICE_HISTORY_QUERY = "INSERT INTO egeis_serviceHistory"
			+ " (id, employeeId, serviceInfo, serviceFrom, remarks, orderNo,"
			+ " createdBy, createdDate, lastModifiedBy, lastModifiedDate, tenantId)"
			+ " VALUES (?,?,?,?,?,?,?,?,?,?,?)";

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	private EmployeeDocumentsRepository documentsRepository;

	public void save(EmployeeRequest employeeRequest) {
		List<ServiceHistory> serviceHistories = employeeRequest.getEmployee().getServiceHistory();

		jdbcTemplate.batchUpdate(INSERT_SERVICE_HISTORY_QUERY, new BatchPreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				ServiceHistory serviceHistory = serviceHistories.get(i);
				ps.setLong(1, serviceHistory.getId());
				ps.setLong(2, employeeRequest.getEmployee().getId());
				ps.setString(3, serviceHistory.getServiceInfo());
				ps.setDate(4, new Date(serviceHistory.getServiceFrom().getTime()));
				ps.setString(5, serviceHistory.getRemarks());
				ps.setString(6, serviceHistory.getOrderNo());
				ps.setLong(7, Long.parseLong(employeeRequest.getRequestInfo().getRequesterId()));
				ps.setTimestamp(8, new Timestamp(new java.util.Date().getTime()));
				ps.setLong(9, Long.parseLong(employeeRequest.getRequestInfo().getRequesterId()));
				ps.setTimestamp(10, new Timestamp(new java.util.Date().getTime()));
				ps.setString(11, serviceHistory.getTenantId());

				if(serviceHistory.getDocuments() != null && !serviceHistory.getDocuments().isEmpty()) {
					documentsRepository.save(employeeRequest.getEmployee().getId(), serviceHistory.getDocuments(),
							DocumentReferenceType.SERVICE.toString(), serviceHistory.getId(), serviceHistory.getTenantId());
				}
			}

			@Override
			public int getBatchSize() {
				return serviceHistories.size();
			}
		});
	}
}