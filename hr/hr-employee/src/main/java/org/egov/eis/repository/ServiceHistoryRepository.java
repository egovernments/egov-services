package org.egov.eis.repository;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

import org.egov.eis.model.ServiceHistory;
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

	// FIXME ServiceHistory Sequence, Employee ID
	public static final String INSERT_SERVICE_HISTORY_QUERY = "INSERT INTO egeis_serviceHistory"
			+ " (id, employeeId, serviceInfo, serviceFrom, remarks, orderNo,"
			+ " createdBy, createdDate, lastModifiedBy, lastModifiedDate, tenantId)"
			+ " VALUES (?,?,?,?,?,?,?,?,?,?,?)";

	@Autowired
	private JdbcTemplate jdbcTemplate;

	// FIXME put tenantId
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
				ps.setString(11, employeeRequest.getEmployee().getTenantId());
			}

			@Override
			public int getBatchSize() {
				return serviceHistories.size();
			}
		});
	}
}