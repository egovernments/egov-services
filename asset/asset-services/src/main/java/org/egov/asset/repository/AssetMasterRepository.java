package org.egov.asset.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.egov.asset.model.AssetStatus;
import org.egov.asset.model.AssetStatusCriteria;
import org.egov.asset.model.StatusValue;
import org.egov.asset.repository.builder.AssetStatusQueryBuilder;
import org.egov.asset.repository.rowmapper.AssetStatusRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import lombok.extern.slf4j.Slf4j;

@Repository
@Slf4j
public class AssetMasterRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private AssetStatusQueryBuilder assetStatusQueryBuilder;

    @Autowired
    private AssetStatusRowMapper assetStatusRowMapper;

    public List<AssetStatus> search(final AssetStatusCriteria assetStatusCriteria) {
        final List<Object> preparedStatementValues = new ArrayList<>();
        final String queryStr = assetStatusQueryBuilder.getQuery(assetStatusCriteria, preparedStatementValues);
        List<AssetStatus> assetStatus = new ArrayList<AssetStatus>();
        try {
            log.info("queryStr::" + queryStr + "preparedStatementValues::" + preparedStatementValues.toString());
            assetStatus = jdbcTemplate.query(queryStr, preparedStatementValues.toArray(), assetStatusRowMapper);
            log.info("AssetStatusRepository::" + assetStatus);
        } catch (final Exception ex) {
            log.info("the exception from findforcriteria : " + ex);
        }
        return getAssetStatuses(assetStatus);
    }

    private List<AssetStatus> getAssetStatuses(final List<AssetStatus> assetStatus) {
        final List<AssetStatus> assetStatuses = new ArrayList<AssetStatus>();
        if (!assetStatus.isEmpty()) {
            final Map<String, List<StatusValue>> assetStatusMap = new HashMap<String, List<StatusValue>>();
            for (final AssetStatus as : assetStatus) {
                final List<StatusValue> statusValues = assetStatusMap.get(as.getObjectName());
                if (statusValues == null)
                    assetStatusMap.put(as.getObjectName(), as.getStatusValues());
                else {
                    statusValues.addAll(as.getStatusValues());
                    assetStatusMap.put(as.getObjectName(), statusValues);
                }
            }

            final List<String> objectName = new ArrayList<String>();
            for (final AssetStatus as : assetStatus)
                if (!objectName.contains(as.getObjectName())) {
                    final AssetStatus asStatus = new AssetStatus();
                    asStatus.setObjectName(as.getObjectName());
                    asStatus.setStatusValues(assetStatusMap.get(as.getObjectName()));
                    asStatus.setAuditDetails(as.getAuditDetails());
                    asStatus.setTenantId(as.getTenantId());
                    assetStatuses.add(asStatus);
                    objectName.add(as.getObjectName());
                }
        }
        return assetStatuses;
    }

}
