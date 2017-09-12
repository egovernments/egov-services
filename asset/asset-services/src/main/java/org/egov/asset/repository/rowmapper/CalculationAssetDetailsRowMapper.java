package org.egov.asset.repository.rowmapper;

import java.math.BigDecimal;
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

		Double yearWiseDepreciaitionRate = null;
		Double assetCategoryDepreciaitionRate = null;
		Double assetDepreciaitionRate = null;
		
		BigDecimal yearWiseDepRate = rs.getBigDecimal("yearwisedepreciationrate");
		BigDecimal assetCatDepRate = rs.getBigDecimal("assetcategorydepreciationrate");
		BigDecimal assetDepRate    = rs.getBigDecimal("assetdepreciationrate");
		
		if(yearWiseDepRate != null)
			yearWiseDepreciaitionRate = yearWiseDepRate.doubleValue();
		if(assetCatDepRate != null)
			assetCategoryDepreciaitionRate = assetCatDepRate.doubleValue();
		if(assetDepRate != null)
			assetDepreciaitionRate = assetDepRate.doubleValue();
		
		return CalculationAssetDetails.builder()
				.accumulatedDepreciation(rs.getBigDecimal("accumulateddepreciation"))
				.assetCategoryDepreciationRate(assetCategoryDepreciaitionRate)
				.assetCategoryId(rs.getLong("assetcategoryid"))
				.assetCategoryName(rs.getString("assetcategoryname"))
				.assetDepreciationRate(assetDepreciaitionRate)
				.assetId(rs.getLong("assetid"))
				.departmentId(rs.getLong("department"))
				.assetReference(rs.getLong("assetreference"))
				.depreciationMethod(DepreciationMethod.fromValue(rs.getString("depreciationmethod")))
				.enableYearWiseDepreciation(rs.getBoolean("enableyearwisedepreciation"))
				.grossValue(rs.getBigDecimal("grossvalue"))
				.yearwisedepreciationrate(yearWiseDepreciaitionRate)
				.financialyear(rs.getString("financialyear"))
				.accumulatedDepreciationAccount(rs.getLong("accumulatedDepreciationAccount"))
				.depreciationExpenseAccount(rs.getLong("depreciationExpenseAccount"))
				.build();
	}
}
