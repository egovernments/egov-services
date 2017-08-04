package org.egov.asset.repository.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.egov.asset.domain.CalculationAssetDetails;
import org.egov.asset.model.enums.DepreciationMethod;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

@Component
public class CalculationAssetDetailsRowMapper implements RowMapper<CalculationAssetDetails>{
	
	@Override
	public CalculationAssetDetails mapRow(ResultSet rs, int rowNum) throws SQLException {

		
		return CalculationAssetDetails.builder()
				.accumulatedDepreciation(rs.getBigDecimal("accumulateddepreciation"))
				.assetCategoryDepreciationRate(rs.getDouble("assetcategorydepreciationrate"))
				.assetCategoryId(rs.getLong("assetcategoryid"))
				.assetCategoryName(rs.getString("assetcategoryname"))
				.assetDepreciationRate(rs.getDouble("assetdepreciationrate"))
				.assetId(rs.getLong("assetid"))
				.assetReference(rs.getLong("assetreference"))
				.depreciationMethod(DepreciationMethod.fromValue(rs.getString("depreciationmethod")))
				.enableYearWiseDepreciation(rs.getBoolean("enableyearwisedepreciation"))
				.grossValue(rs.getBigDecimal("grossvalue"))
				.yearwisedepreciationrate((Double)rs.getObject("yearwisedepreciationrate"))
				.financialyear(rs.getString("financialyear"))
				.build();
	}
}
