package org.egov.asset.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.egov.asset.model.AssetConfigurationCriteria;
import org.egov.asset.repository.builder.AssetConfigurationQueryBuilder;
import org.egov.asset.repository.rowmapper.AssetConfigurationKeyValuesRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class AssetConfigurationRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private AssetConfigurationKeyValuesRowMapper assetConfigurationKeyValuesRowMapper;

    @Autowired
    private AssetConfigurationQueryBuilder assetConfigurationQueryBuilder;

    public Map<String, List<String>> findForCriteria(final AssetConfigurationCriteria assetConfigurationCriteria) {
        final List<Object> preparedStatementValues = new ArrayList<Object>();
        final String queryStr = assetConfigurationQueryBuilder.getQuery(assetConfigurationCriteria,
                preparedStatementValues);
        final Map<String, List<String>> assetConfigurationKeyValues = jdbcTemplate.query(queryStr,
                preparedStatementValues.toArray(), assetConfigurationKeyValuesRowMapper);
        return assetConfigurationKeyValues;
    }
}
