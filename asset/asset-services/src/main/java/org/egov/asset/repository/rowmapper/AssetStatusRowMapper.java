package org.egov.asset.repository.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.egov.asset.model.AssetStatus;
import org.egov.asset.model.AuditDetails;
import org.egov.asset.model.StatusValue;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

@Component
public class AssetStatusRowMapper implements RowMapper<AssetStatus> {

	@Override
	public AssetStatus mapRow(final ResultSet rs, final int rowNum) throws SQLException {
		final AssetStatus assetStatus = new AssetStatus();
		final StatusValue statusValue = new StatusValue();
		final AuditDetails auditDetails = new AuditDetails();
		final List<StatusValue> statusValues = new ArrayList<StatusValue>();

		statusValue.setName(rs.getString("name"));
		statusValue.setCode(rs.getString("code"));
		statusValue.setDescription(rs.getString("description"));
		statusValues.add(statusValue);

		assetStatus.setObjectName(rs.getString("objectName"));
		assetStatus.setTenantId(rs.getString("tenantId"));
		assetStatus.setStatusValues(statusValues);

		auditDetails.setCreatedBy(rs.getString("createdby"));
		auditDetails.setCreatedDate(rs.getLong("createddate"));
		auditDetails.setLastModifiedBy(rs.getString("lastmodifiedby"));
		auditDetails.setLastModifiedDate(rs.getLong("lastmodifieddate"));

		assetStatus.setAuditDetails(auditDetails);
		return assetStatus;
	}

}
