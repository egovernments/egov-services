package org.egov.asset.repository.rowmapper;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;

@Component
public class DepreciationSumRowMapper implements ResultSetExtractor<Map<Long, BigDecimal>> {

	@Override
	public Map<Long, BigDecimal> extractData(ResultSet rs) throws SQLException {

		Map<Long, BigDecimal> map = new HashMap<>();
		while (rs.next()) {
			map.put(rs.getLong("assetid"), rs.getBigDecimal("totaldepreciationvalue"));
		}
		return map;
	}
}
