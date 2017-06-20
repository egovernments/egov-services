package org.egov.demand.repository.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.egov.demand.model.AuditDetail;
import org.egov.demand.model.DemandDetail;
import org.springframework.jdbc.core.RowMapper;

public class DemandDetailRowMapper implements RowMapper<DemandDetail>{

	@Override
	public DemandDetail mapRow(ResultSet rs, int RowNum) throws SQLException {

		DemandDetail demandDetail = new DemandDetail();
		demandDetail.setId(rs.getString("dlid"));
		demandDetail.setDemandId(rs.getString("dldemandid"));
		demandDetail.setTaxHeadCode(rs.getString("dltaxheadcode"));
		demandDetail.setTenantId(rs.getString("dltenantid"));

		Double dlTaxAmount = rs.getDouble("dltaxamount");
		Double dlcollectionamount = rs.getDouble("dlcollectionamount");
		if (dlTaxAmount == 0)
			demandDetail.setTaxAmount(null);
		else
			demandDetail.setTaxAmount(dlTaxAmount);
		if (dlcollectionamount == 0)
			demandDetail.setCollectionAmount(null);
		else
			demandDetail.setCollectionAmount(dlcollectionamount);

		AuditDetail dlauditDetail = new AuditDetail();
		dlauditDetail.setCreatedBy(rs.getString("dlcreatedby"));
		dlauditDetail.setCreatedTime(rs.getLong("dlcreatedtime"));
		dlauditDetail.setLastModifiedBy(rs.getString("dllastModifiedby"));
		dlauditDetail.setLastModifiedTime(rs.getLong("dllastModifiedtime"));
		demandDetail.setAuditDetail(dlauditDetail);

		return demandDetail;
	}

}
