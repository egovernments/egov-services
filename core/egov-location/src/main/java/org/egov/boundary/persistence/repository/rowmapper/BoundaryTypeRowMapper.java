package org.egov.boundary.persistence.repository.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.egov.boundary.web.contract.BoundaryType;
import org.egov.boundary.web.contract.HierarchyType;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

@Component
public class BoundaryTypeRowMapper implements RowMapper<BoundaryType>{

	@Override
	public BoundaryType mapRow(final ResultSet rs, final int rowNum) throws SQLException {
		
		BoundaryType boundaryType = BoundaryType.builder().id(String.valueOf(rs.getLong("btId"))).hierarchy(rs.getLong("btHierarchy"))
				.name(rs.getString("btName")).createdBy(rs.getLong("btCreatedBy"))
				.lastModifiedBy(rs.getLong("btLastModifiedBy")).localName(rs.getString("btLocalName"))
				.code(rs.getString("btCode")).tenantId(rs.getString("btTenantId")).build();
		
		HierarchyType hierarchyType = new HierarchyType();
		hierarchyType.setId(rs.getLong("htId"));
		hierarchyType.setName(rs.getString("htName"));
		hierarchyType.setCode(rs.getString("htCode"));
		hierarchyType.setTenantId(rs.getString("htTenantId"));
		hierarchyType.setLocalName(rs.getString("htLocalName"));
		hierarchyType.setCreatedBy(rs.getLong("htCreatedBy"));
		hierarchyType.setLastModifiedBy(rs.getLong("htLastModifiedBy"));
		hierarchyType.setCreatedDate(rs.getDate("htCreatedDate").getTime());
		hierarchyType.setLastModifiedDate(rs.getDate("htLastModifiedDate").getTime());
		boundaryType.setHierarchyType(hierarchyType);
		
		if(rs.getDate("btCreatedDate")!=null){
			boundaryType.setCreatedDate(rs.getDate("btCreatedDate").getTime());
		}
		if(rs.getDate("btLastModifiedDate")!=null){
			boundaryType.setLastModifiedDate(rs.getDate("btLastModifiedDate").getTime());
		}
		
		if (rs.getLong("btParent") != 0) {
			BoundaryType parent = BoundaryType.builder().id(String.valueOf(rs.getLong("btParent"))).build();
			boundaryType.setParent(parent);
		}
		
		return boundaryType;
	}

}
