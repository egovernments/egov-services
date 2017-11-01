package org.egov.lcms.repository.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.egov.lcms.models.AuditDetails;
import org.egov.lcms.models.Register;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

@Component
public class RegisterRowMapper implements RowMapper<Register>{

	@Override
	public Register mapRow(ResultSet rs, int rowNum) throws SQLException {
		
		Register register = new Register();
		register.setCode(rs.getString("code"));
		register.setRegister(rs.getString("register"));
		register.setIsActive(rs.getBoolean("isActive"));
		register.setTenantId(rs.getString("tenantId"));
		AuditDetails auditDetails = new AuditDetails();
		auditDetails.setCreatedBy(rs.getString("createdBy"));
		auditDetails.setLastModifiedBy(rs.getString("lastModifiedBy"));
		auditDetails.setCreatedTime(rs.getBigDecimal("createdTime"));
		auditDetails.setLastModifiedTime(rs.getBigDecimal("lastModifiedTime"));
		register.setAuditDetails(auditDetails);
		
		return register;
	}

}
