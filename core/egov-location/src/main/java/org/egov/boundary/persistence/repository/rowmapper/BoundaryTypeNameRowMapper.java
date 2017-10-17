package org.egov.boundary.persistence.repository.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.egov.boundary.web.contract.BoundaryType;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

@Component
public class BoundaryTypeNameRowMapper implements RowMapper<BoundaryType> {

	@Override
	public BoundaryType mapRow(final ResultSet rs, final int rowNum) throws SQLException {
		
		BoundaryType boundaryType = BoundaryType.builder().id(String.valueOf(rs.getLong("id"))).hierarchy(rs.getLong("hierarchy"))
				.name(rs.getString("name")).createdBy(rs.getLong("createdby"))
				.lastModifiedBy(rs.getLong("lastmodifiedby")).createdDate(rs.getDate("createddate").getTime())
				.lastModifiedDate(rs.getDate("lastmodifieddate").getTime()).localName(rs.getString("localname"))
				.code(rs.getString("code")).tenantId(rs.getString("tenantid")).build();
		
		return boundaryType;
	}
}
