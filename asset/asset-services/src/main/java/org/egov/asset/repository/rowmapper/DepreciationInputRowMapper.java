package org.egov.asset.repository.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.egov.asset.model.DepreciationInputs;
import org.egov.asset.model.enums.DepreciationMethod;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

@Component
public class DepreciationInputRowMapper implements RowMapper<DepreciationInputs> {

    @Override
    public DepreciationInputs mapRow(final ResultSet rs, final int rowNum) throws SQLException {

        return DepreciationInputs.builder().lastDepreciationDate((Long) rs.getObject("lastdepreciationdate"))
                .enableYearwiseDepreciation(rs.getBoolean("enableyearwisedepreciation")).department(rs.getLong("department"))
                .dateOfCreation((Long) rs.getObject("dateofcreation")).currentValue(rs.getBigDecimal("currentvalue"))
                .assetCategory((Long) rs.getObject("assetcategory")).assetaccount("assetaccount").assetCategoryName(rs.getString("assetcategoryname"))
                .accumulatedDepreciationAccount(rs.getLong("accumulateddepreciationaccount"))
                .revaluationreserveaccount("revaluationreserveaccount").department(rs.getLong("department"))
                .depreciationMethod(DepreciationMethod.fromValue(rs.getString("depreciationMethod")))
                .depreciationExpenseAccount(rs.getLong("depreciationexpenseaccount"))
                .accumulatedDepreciation(rs.getBigDecimal("accumulatedDepreciation")).tenantId(rs.getString("tenantid"))
                .depreciationRate(rs.getDouble("assetcategory_depreciationrate"))
                .yearwiseDepreciationRate(rs.getDouble("yearwisedepreciationrate"))
                .grossValue(rs.getBigDecimal("grossValue")).assetCode(rs.getString("assetcode")).assetName(rs.getString("assetname"))
                .assetId((Long) rs.getObject("assetid")).depreciationSum(rs.getBigDecimal("depreciationvaluesum")).build();
    }

}
