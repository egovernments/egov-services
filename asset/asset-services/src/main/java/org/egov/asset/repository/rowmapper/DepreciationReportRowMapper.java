package org.egov.asset.repository.rowmapper;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.egov.asset.model.DepreciationReportCriteria;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

@Component
public class DepreciationReportRowMapper implements RowMapper<DepreciationReportCriteria> {
    @Override
    public DepreciationReportCriteria mapRow(final ResultSet rs, final int rowNum) throws SQLException {

        final DepreciationReportCriteria depreciation = new DepreciationReportCriteria();
        depreciation.setId(rs.getLong("depreciationId"));
        depreciation.setAssetName(rs.getString("assetname"));
        depreciation.setAssetCode(rs.getString("assetcode"));
        depreciation.setAssetId(rs.getLong("assetId"));
        depreciation.setFinancialYear(rs.getString("financialyear"));
        depreciation.setId(rs.getLong("depreciationId"));
        depreciation.setTenantId(rs.getString("tenantId"));

        final BigDecimal grossValue = rs.getBigDecimal("grossvalue");
        if (grossValue == BigDecimal.ZERO)
            depreciation.setGrossValue(null);
        else
            depreciation.setGrossValue(grossValue);
        depreciation.setDepartment((Long) rs.getObject("department"));
        depreciation.setAssetCategory((Long) rs.getObject("assetcategoryId"));
        depreciation.setAssetCategoryType(rs.getString("assetcategorytype"));
        depreciation.setAssetCategoryName(rs.getString("assetcategoryname"));
        depreciation.setParent((Long) rs.getObject("parentid"));
        depreciation.setDepreciationRate(rs.getDouble("depreciationrate"));
        depreciation.setDepreciationValue(rs.getBigDecimal("depreciationvalue"));
        depreciation.setValueAfterDepreciation(rs.getBigDecimal("valueafterdepreciation"));
        return depreciation;

    }
}
