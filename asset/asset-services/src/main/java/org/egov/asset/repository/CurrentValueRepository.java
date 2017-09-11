package org.egov.asset.repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Set;

import org.egov.asset.model.AssetCurrentValue;
import org.egov.asset.model.AuditDetails;
import org.egov.asset.model.enums.AssetConfigurationKeys;
import org.egov.asset.repository.builder.CurrentValueQueryBuilder;
import org.egov.asset.repository.rowmapper.CurrentValueRowMapper;
import org.egov.asset.service.AssetConfigurationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import lombok.extern.slf4j.Slf4j;

@Repository
@Slf4j
public class CurrentValueRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private CurrentValueQueryBuilder currentValueQueryBuilder;

    @Autowired
    private CurrentValueRowMapper currentValueRowMapper;

    @Autowired
    private AssetConfigurationService assetConfigurationService;

    public List<AssetCurrentValue> getCurrentValues(final Set<Long> assetIds, final String tenantId) {

        final String sql = currentValueQueryBuilder.getCurrentValueQuery(assetIds, tenantId);

        log.debug("the query for fetching currentValues : " + sql);
        return jdbcTemplate.query(sql, currentValueRowMapper);
    }

    public void create(final List<AssetCurrentValue> assetCurrentValues) {

        final String sql = currentValueQueryBuilder.getInsertQuery();
        final String tenantId = assetCurrentValues.get(0).getTenantId();
        final int batchSize = Integer.parseInt(assetConfigurationService
                .getAssetConfigValueByKeyAndTenantId(AssetConfigurationKeys.ASSETBATCHSIZE, tenantId));
        
        log.debug("Batch Size :: " + batchSize);

        for (int j = 0; j < assetCurrentValues.size(); j += batchSize) {

            final List<AssetCurrentValue> batchList = assetCurrentValues.subList(j,
                    j + batchSize > assetCurrentValues.size() ? assetCurrentValues.size() : j + batchSize);

            jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {

                @Override
                public void setValues(final PreparedStatement ps, final int rowNum) throws SQLException {

                    final AssetCurrentValue assetCurrentValue = assetCurrentValues.get(rowNum);
                    final AuditDetails auditDetails = assetCurrentValue.getAuditDetails();
                    ps.setLong(1, assetCurrentValue.getId());
                    ps.setLong(2, assetCurrentValue.getAssetId());
                    ps.setString(3, assetCurrentValue.getTenantId());
                    ps.setString(4, assetCurrentValue.getAssetTranType().toString());
                    ps.setBigDecimal(5, assetCurrentValue.getCurrentAmount());
                    ps.setString(6, auditDetails.getCreatedBy());
                    ps.setLong(7, auditDetails.getCreatedDate());
                    ps.setString(8, auditDetails.getLastModifiedBy());
                    ps.setLong(9, auditDetails.getLastModifiedDate());
                }

                @Override
                public int getBatchSize() {
                    return batchList.size();
                }
            });
        }
    }
}
