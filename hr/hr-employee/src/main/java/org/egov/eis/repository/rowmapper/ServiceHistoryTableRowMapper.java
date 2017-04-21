package org.egov.eis.repository.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.egov.eis.model.ServiceHistory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

@Component
public class ServiceHistoryTableRowMapper implements RowMapper<ServiceHistory> {

	@Override
	public ServiceHistory mapRow(ResultSet rs, int rowNum) throws SQLException, DataAccessException {
		final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

		ServiceHistory serviceHistory = new ServiceHistory();
		serviceHistory.setId(rs.getLong("id"));
		serviceHistory.setServiceInfo(rs.getString("serviceinfo"));
		serviceHistory.setRemarks(rs.getString("remarks"));
		serviceHistory.setOrderNo(rs.getString("orderno"));
		serviceHistory.setCreatedBy(rs.getLong("createdby"));
		serviceHistory.setLastModifiedBy(rs.getLong("lastmodifiedby"));
		serviceHistory.setTenantId(rs.getString("tenantid"));
		try {
			serviceHistory.setServiceFrom(sdf.parse(sdf.format(rs.getDate("servicefrom"))));
			serviceHistory.setCreatedDate(sdf.parse(sdf.format(rs.getDate("createddate"))));
			serviceHistory.setLastModifiedDate(sdf.parse(sdf.format(rs.getDate("lastmodifieddate"))));
		} catch (ParseException e) {
			e.printStackTrace();
			throw new SQLException("Parse exception while parsing date");
		}
		
		return serviceHistory;
	}
	
}
