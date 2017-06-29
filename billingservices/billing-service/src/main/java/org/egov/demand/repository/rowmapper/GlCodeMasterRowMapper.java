package org.egov.demand.repository.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.egov.demand.model.AuditDetail;
import org.egov.demand.model.GlCodeMaster;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class GlCodeMasterRowMapper implements RowMapper<GlCodeMaster> {

	private static final Logger logger = LoggerFactory.getLogger(GlCodeMasterRowMapper.class);
	
	@Autowired
	private ObjectMapper objectMapper;

	@Override
	public GlCodeMaster mapRow(ResultSet rs, int arg1) throws SQLException {

		GlCodeMaster glCodeMaster=new GlCodeMaster();
		AuditDetail audit=new AuditDetail();
		glCodeMaster.setId(rs.getString("id"));
		glCodeMaster.setGlCode(rs.getString("glcode"));
		glCodeMaster.setService(rs.getString("service"));
		glCodeMaster.setTaxHead(rs.getString("taxhead"));
		glCodeMaster.setTenantId(rs.getString("tenantid"));
		glCodeMaster.setFromDate((Long)rs.getObject("fromdate"));
		glCodeMaster.setToDate((Long)rs.getObject("todate"));
		audit.setCreatedBy(rs.getString("createdby"));
		audit.setCreatedTime((Long)rs.getObject("createdtime"));
		audit.setLastModifiedBy(rs.getString("lastmodifiedby"));
		audit.setLastModifiedTime((Long)rs.getObject("lastmodifiedtime"));
		glCodeMaster.setAuditDetails(audit);
		return glCodeMaster;
	}

}
