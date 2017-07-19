package org.egov.asset.repository.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;

@Component
public class AssetConfigurationKeyValuesRowMapper implements ResultSetExtractor<Map<String, List<String>>> {

    @Override
    public Map<String, List<String>> extractData(final ResultSet rs) throws SQLException, DataAccessException {
        final Map<String, List<String>> assetConfKeyValMap = new HashMap<>();

        while (rs.next()) {
            final String assetConfKey = rs.getString("key");

            if (!assetConfKeyValMap.containsKey(assetConfKey))
                assetConfKeyValMap.put(assetConfKey, new ArrayList<>());

            final List<String> assetConfKeyVal = assetConfKeyValMap.get(assetConfKey);
            assetConfKeyVal.add(rs.getString("value"));
        }

        return assetConfKeyValMap;
    }

}
