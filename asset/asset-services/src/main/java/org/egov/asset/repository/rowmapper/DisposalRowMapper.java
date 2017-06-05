package org.egov.asset.repository.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.egov.asset.model.AuditDetails;
import org.egov.asset.model.Disposal;
import org.egov.asset.model.enums.TransactionType;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

@Component
public class DisposalRowMapper implements RowMapper<Disposal>{

	@Override
	public Disposal mapRow(ResultSet rs, int rowNum) throws SQLException {
		
		Disposal disposal = new Disposal();
		disposal.setId((Long)rs.getObject("id"));
		disposal.setTenantId(rs.getString("tenantid"));
		disposal.setAssetId(rs.getLong("assetid"));
		disposal.setBuyerName(rs.getString("buyername"));
		disposal.setBuyerAddress(rs.getString("buyeraddress"));
		disposal.setDisposalReason(rs.getString("disposalreason"));
		disposal.setDisposalDate((Long)rs.getObject("disposaldate"));
		disposal.setPanCardNumber(rs.getString("pancardnumber"));
		disposal.setAadharCardNumber(rs.getString("aadharcardnumber"));
		
		disposal.setAssetCurrentValue((Double)rs.getDouble("assetcurrentvalue"));
		disposal.setSaleValue((Double)rs.getDouble("salevalue"));
		disposal.setTransactionType(TransactionType.fromValue(rs.getString("transactiontype")));
		disposal.setAssetSaleAccount((Long)rs.getObject("id"));
		
		AuditDetails auditDetails = new AuditDetails();
		auditDetails.setCreatedBy(rs.getString("createdby"));
		auditDetails.setCreatedDate((Long)rs.getLong("createddate"));
		auditDetails.setLastModifiedBy(rs.getString("lastmodifiedby"));
		auditDetails.setLastModifiedDate((Long)rs.getLong("lastmodifieddate"));
		disposal.setAuditDetails(auditDetails);
		
		return disposal;
	}

}































