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

		return DepreciationInputs.builder().lastDepreciationDate((Long) rs.getObject("lastdepreciationdate"))
				.openingDate((Long) rs.getObject("openingdate")).currentValue(rs.getBigDecimal("currentValue"))
				.assetCategory((Long) rs.getObject("assetCategory")).assetaccount(rs.getString("assetaccount"))
				.accumulatedDepreciation(rs.getBigDecimal("accumulatedDepreciation")).tenantId(rs.getString("tenantid"))
				.accumulateddepreciationaccount(rs.getString("accumulateddepreciationaccount"))
				.originalValue(rs.getBigDecimal("originalValue")).grossValue(rs.getBigDecimal("grossValue"))
				.depreciationexpenseaccount(rs.getString("depreciationexpenseaccount"))
				.revaluationreserveaccount(rs.getString("revaluationreserveaccount")).assetCode(rs.getString("assetcode"))
				.assetId((Long) rs.getObject("assetId")).depreciationSum(rs.getBigDecimal("depreciationvaluesum"))
				.build();
	}

}
