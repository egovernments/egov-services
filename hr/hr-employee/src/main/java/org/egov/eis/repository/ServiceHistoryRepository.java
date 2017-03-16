package org.egov.eis.repository;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import org.egov.eis.model.Employee;
import org.egov.eis.model.ServiceHistory;
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
			+ " VALUES (NEXTVAL('seq_egeis_serviceHistory'),?,?,?,?,?,?,?,?,?,?)";

	@Autowired
	private JdbcTemplate jdbcTemplate;

	// FIXME put tenantId
	public void save(Employee employee) {
		List<ServiceHistory> serviceHistories = employee.getServiceHistory();

		jdbcTemplate.batchUpdate(INSERT_SERVICE_HISTORY_QUERY, new BatchPreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				ServiceHistory serviceHistory = serviceHistories.get(i);
				ps.setLong(1, employee.getId());
				ps.setString(2, serviceHistory.getServiceInfo());
				ps.setDate(3, new Date(serviceHistory.getServiceFrom().getTime()));
				ps.setString(4, serviceHistory.getRemarks());
				ps.setString(5, serviceHistory.getOrderNo());
				ps.setLong(6, serviceHistory.getCreatedBy());
				ps.setDate(7, new Date(serviceHistory.getCreatedDate().getTime()));
				ps.setLong(8, serviceHistory.getLastModifiedBy());
				ps.setDate(9, new Date(serviceHistory.getLastModifiedDate().getTime()));
				ps.setString(10, employee.getTenantId());
			}

			@Override
			public int getBatchSize() {
				return serviceHistories.size();
			}
		});
	}
}