package org.egov.eis.repository.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.egov.eis.model.ServiceHistory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

@Component
public class ServiceHistoryTableRowMapper implements RowMapper<ServiceHistory> {

	@Override
	public ServiceHistory mapRow(ResultSet rs, int rowNum) throws SQLException, DataAccessException {
		
		ServiceHistory serviceHistory = new ServiceHistory();
		serviceHistory.setId(rs.getLong("id"));
		serviceHistory.setServiceInfo(rs.getString("serviceinfo"));
		serviceHistory.setServiceFrom(rs.getDate("servicefrom"));
		serviceHistory.setRemarks(rs.getString("remarks"));
		serviceHistory.setOrderNo(rs.getString("orderno"));
		serviceHistory.setCreatedBy(rs.getLong("createdby"));
		serviceHistory.setCreatedDate(rs.getDate("createddate"));
		serviceHistory.setLastModifiedBy(rs.getLong("lastmodifiedby"));
		serviceHistory.setLastModifiedDate(rs.getDate("lastmodifieddate"));
		serviceHistory.setTenantId(rs.getString("tenantid"));
		
		return serviceHistory;
	}
	
}
