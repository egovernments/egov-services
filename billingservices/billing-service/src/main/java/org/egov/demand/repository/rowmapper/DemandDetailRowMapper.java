package org.egov.demand.repository.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.egov.demand.model.AuditDetail;
import org.egov.demand.model.DemandDetail;
import org.egov.demand.model.TaxHeadMaster;
import org.springframework.jdbc.core.RowMapper;

public class DemandDetailRowMapper implements RowMapper<DemandDetail>{

	@Override
	public DemandDetail mapRow(ResultSet rs, int RowNum) throws SQLException {

		DemandDetail demandDetail = new DemandDetail();
		demandDetail.setId(rs.getString("dlid"));
		demandDetail.setDemandId(rs.getString("dldemandid"));
		
		TaxHeadMaster taxHeadMaster = new TaxHeadMaster();
		taxHeadMaster.setCode(rs.getString("dltaxheadcode"));
		//demandDetail.setTaxHeadMaster(taxHeadMaster);
		demandDetail.setTenantId(rs.getString("dltenantid"));
		demandDetail.setTaxAmount(rs.getBigDecimal("dltaxamount"));
		demandDetail.setCollectionAmount(rs.getBigDecimal("dlcollectionamount"));

		AuditDetail dlauditDetail = new AuditDetail();
		dlauditDetail.setCreatedBy(rs.getString("dlcreatedby"));
		dlauditDetail.setCreatedTime(rs.getLong("dlcreatedtime"));
		dlauditDetail.setLastModifiedBy(rs.getString("dllastModifiedby"));
		dlauditDetail.setLastModifiedTime(rs.getLong("dllastModifiedtime"));
		demandDetail.setAuditDetail(dlauditDetail);

		return demandDetail;
	}

}
