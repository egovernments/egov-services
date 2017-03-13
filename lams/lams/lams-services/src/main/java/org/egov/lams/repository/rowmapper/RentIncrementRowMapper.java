package org.egov.lams.repository.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.egov.lams.model.RentIncrementType;
import org.springframework.jdbc.core.RowMapper;

public class RentIncrementRowMapper implements RowMapper<RentIncrementType> {

	@Override
	public RentIncrementType mapRow(ResultSet rs, int rowNum) throws SQLException {

		RentIncrementType rentIncrementType = new RentIncrementType();
		rentIncrementType.setId(rs.getLong("id"));
		rentIncrementType.setType(rs.getString("type"));
		return rentIncrementType;
	}

}
