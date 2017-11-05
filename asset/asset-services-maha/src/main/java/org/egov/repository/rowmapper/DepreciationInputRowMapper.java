package org.egov.repository.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.egov.model.DepreciationInputs;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

@Component
public class DepreciationInputRowMapper implements RowMapper<DepreciationInputs> {

	@Override
	public DepreciationInputs mapRow(ResultSet rs, int rowNum) throws SQLException {

		return DepreciationInputs.builder().lastDepreciationDate(rs.getLong("lastdepreciationdate"))
				.currentValue(rs.getBigDecimal("currentValue")).assetCategory(rs.getLong("assetCategory"))
				.accumulatedDepreciation(rs.getBigDecimal("accumulatedDepreciation")).originalValue(rs.getBigDecimal("originalValue"))
				.grossValue(rs.getBigDecimal("grossValue")).tenantId(rs.getString("tenantid"))
				.assetId(rs.getLong("assetId")).depreciationSum(rs.getBigDecimal("depreciationvaluesum")).build();
	}

}
