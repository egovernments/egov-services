package org.egov.demand.repository.rowmapper;

import java.security.Timestamp;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.egov.demand.model.AuditDetail;
import org.egov.demand.model.TaxHeadMaster;
import org.egov.demand.model.TaxPeriod;
import org.egov.demand.model.enums.Category;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class TaxHeadMasterRowMapper implements RowMapper<TaxHeadMaster> {

	private static final Logger logger = LoggerFactory.getLogger(TaxHeadMasterRowMapper.class);

	@Autowired
	private ObjectMapper objectMapper;

	@Override
	public TaxHeadMaster mapRow(ResultSet rs, int arg1) throws SQLException {
		
		TaxHeadMaster taxHead=new TaxHeadMaster();
		TaxPeriod taxPeriod=new TaxPeriod();
		AuditDetail auditDetails=new AuditDetail();
		try {
			taxHead.setId(rs.getString("id"));
			taxHead.setTenantId(rs.getString("tenantid"));
			taxHead.setCategory(Category.fromValue(rs.getString("category")));
			taxHead.setService(rs.getString("service"));
			taxHead.setName(rs.getString("name"));
			taxHead.setCode(rs.getString("code"));
			taxHead.setGlCode(rs.getString("glcode"));
			taxHead.setIsDebit(rs.getBoolean("isdebit"));
			taxHead.setIsActualDemand(rs.getBoolean("isactualdemand"));
			taxHead.setOrder(rs.getInt("orderno"));
			taxHead.setValidFrom(rs.getLong("validfrom"));
			taxHead.setValidTill(rs.getLong("validtill"));
			//taxPeriod.setId(rs.getString("taxperiod"));
			/*taxPeriod.setFromDate((Long)rs.getObject("fromdate"));
			taxPeriod.setToDate((Long)rs.getObject("todate"));
			taxPeriod.setModule(rs.getString("module"));
			taxPeriod.setFinancialYear(rs.getString("financialyear"));
			taxPeriod.setDescription(rs.getString("description"));*/
			
			auditDetails.setCreatedBy(rs.getString("createdby"));
			auditDetails.setCreatedTime((Long)rs.getObject("createdtime"));
			auditDetails.setLastModifiedBy(rs.getString("lastmodifiedby"));
			auditDetails.setLastModifiedTime((Long)rs.getObject("lastmodifiedtime"));
			
			taxHead.setAuditDetail(auditDetails);
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		}
		
		
		
		
		
		return taxHead;
	}


	
	
}
