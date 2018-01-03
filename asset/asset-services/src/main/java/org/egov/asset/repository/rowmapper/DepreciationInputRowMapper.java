package org.egov.asset.repository.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.egov.asset.model.DepreciationInputs;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

@Component
public class DepreciationInputRowMapper implements RowMapper<DepreciationInputs> {

	@Override
	public DepreciationInputs mapRow(ResultSet rs, int rowNum) throws SQLException {

		return DepreciationInputs.builder().lastDepreciationDate((Long) rs.getObject("lastdepreciationdate")).enableYearwiseDepreciation(rs.getBoolean("enableyearwisedepreciation"))
				.dateOfCreation((Long) rs.getObject("dateofcreation")).currentValue(rs.getBigDecimal("currentvalue"))
				.assetCategory((Long) rs.getObject("assetcategory")).assetaccount("assetaccount").accumulateddepreciationaccount("accumulateddepreciationaccount")
				.depreciationexpenseaccount("revaluationreserveaccount").depreciationexpenseaccount("depreciationexpenseaccount").accumulatedDepreciation(rs.getBigDecimal("accumulatedDepreciation")).tenantId(rs.getString("tenantid"))
				.depreciationRate(rs.getDouble("depreciationrate")).yearwiseDepreciationRate(rs.getDouble("yearwisedepreciationrate"))
				.grossValue(rs.getBigDecimal("grossValue"))
				.assetCode(rs.getString("assetcode"))
				.assetId((Long) rs.getObject("assetid")).depreciationSum(rs.getBigDecimal("depreciationvaluesum"))
				.build();
	}

}
