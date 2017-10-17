package org.egov.boundary.persistence.repository.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.egov.boundary.domain.model.Boundary;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

@Component
public class BoundaryIdRowMapper implements RowMapper<Boundary> {

	@Override
	public Boundary mapRow(final ResultSet rs, final int rowNum) throws SQLException {
		
		final Boundary boundary = Boundary.builder().id(rs.getLong("id")).name(rs.getString("name"))
				.boundaryNum(rs.getLong("boundarynum")).tenantId(rs.getString("tenantid")).build();
		
		return boundary; 
	}
	
}
