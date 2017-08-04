package org.egov.asset.repository.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.egov.asset.domain.CalculationCurrentValue;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

@Component
public class CalculationCurrentValueRowMapper  implements RowMapper<CalculationCurrentValue>{

	@Override
	public CalculationCurrentValue mapRow(ResultSet rs, int rowNum) throws SQLException {

		return CalculationCurrentValue.builder()
				.assetId(rs.getLong("assetid"))
				.currentAmountAfterSeptember(rs.getBigDecimal("afterseptembercurrentvalue"))
				.currentAmountBeforeSeptember(rs.getBigDecimal("beforeseptembercurrentvalue"))
				.tenantId(rs.getString("tenantId")).build();
	}
}
