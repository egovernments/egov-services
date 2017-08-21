package org.egov.asset.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

public class AssetRepositoryUtils {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public Integer checkDataExists(final String table, final List<String> cloumns, final List<Object> values,
            final String tenantId) {
        final String sql = "select * from egasset_assetcategory where name = ?";
        return jdbcTemplate.update(sql.toString(), values.toArray());

    }

}
