package org.egov.asset.repository.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.egov.asset.model.YearWiseDepreciation;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

@Component
public class YearWiseDepreciationRowMapper implements RowMapper<YearWiseDepreciation> {

	@Override
	public YearWiseDepreciation mapRow(final ResultSet rs, final int rowNum) throws SQLException {
		final YearWiseDepreciation yearWiseDepreciation = new YearWiseDepreciation();
		yearWiseDepreciation.setDepreciationRate(rs.getDouble("depreciationrate"));
		yearWiseDepreciation.setFinancialYear(rs.getString("financialyear"));
		yearWiseDepreciation.setUsefulLifeInYears(Long.valueOf(rs.getString("usefullifeinyears")));
		return yearWiseDepreciation;
	}

}
