package org.egov.asset.repository.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.egov.asset.model.AssetCurrentValue;
import org.egov.asset.model.AuditDetails;
import org.egov.asset.model.enums.TransactionType;
import org.springframework.jdbc.core.RowMapper;

public class CurrentValueRowMapper implements RowMapper<AssetCurrentValue> {

	@Override
	public AssetCurrentValue mapRow(ResultSet rs, int rowNum) throws SQLException {

		AuditDetails auditDetails = AuditDetails.builder().createdBy(rs.getString("createdby"))
				.createdDate(rs.getLong("createdtime")).lastModifiedBy(rs.getString(
				"lastmodifiedby")).lastModifiedDate(rs.getLong("lastmodifiedtime")).build();
		
		return AssetCurrentValue.builder().auditDetails(auditDetails).assetId(
				rs.getLong("assetid")).assetTranType(TransactionType.fromValue(rs.getString("assetTranType")))
				.tenantId(rs.getString("tenantId")).currentAmount(rs.getBigDecimal("currentamount")).build();
	}
}
