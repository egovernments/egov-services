package org.egov.citizen.repository.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.egov.citizen.model.ServiceReq;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import lombok.EqualsAndHashCode;

@Component
@EqualsAndHashCode
public class ServiceRequestRowMapper implements RowMapper<ServiceReq> {
	
	@Override
	public ServiceReq mapRow(ResultSet rs, int rowNum) throws SQLException {
		ServiceReq serviceReq = new ServiceReq();
		
		serviceReq.setTenantId(rs.getString("tenantid"));
		serviceReq.setServiceRequestId(rs.getString("id"));
		serviceReq.setBackendServiceDetails(rs.getString("servicerequest"));
		
		return serviceReq;
	}

}
