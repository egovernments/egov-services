package org.egov.boundary.persistence.repository.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.egov.boundary.domain.model.Boundary;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

@Component
public class BoundaryMapper implements RowMapper<Boundary> {

	@Override
	public Boundary mapRow(final ResultSet rs, final int rowNum) throws SQLException {

		final Boundary boundary = Boundary.builder().id(rs.getLong("id")).name(rs.getString("name"))
				.boundaryNum(rs.getLong("boundarynum")).fromDate(rs.getDate("fromdate")).toDate(rs.getDate("todate"))
				.bndryId(rs.getLong("bndryid")).materializedPath(rs.getString("materializedpath"))
				.isHistory(rs.getBoolean("ishistory")).createdDate(rs.getDate("createddate").getTime())
				.lastModifiedDate(rs.getDate("lastmodifieddate").getTime()).createdBy(rs.getLong("createdby"))
				.lastModifiedBy(rs.getLong("lastmodifiedby")).tenantId(rs.getString("tenantid")).build();

		if (rs.getFloat("longitude") != 0) {
			boundary.setLongitude(rs.getFloat("longitude"));
		}
		if (rs.getFloat("latitude") != 0) {
			boundary.setLongitude(rs.getFloat("latitude"));
		}
		if (rs.getLong("parent") != 0) {
			Boundary parent = Boundary.builder().id(rs.getLong("parent")).build();
			boundary.setParent(parent);
		}
		return boundary;
	}
}
