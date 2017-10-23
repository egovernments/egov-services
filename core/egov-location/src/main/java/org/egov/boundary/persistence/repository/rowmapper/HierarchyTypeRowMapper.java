package org.egov.boundary.persistence.repository.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.egov.boundary.web.contract.HierarchyType;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

@Component
public class HierarchyTypeRowMapper implements RowMapper<HierarchyType> {

	@Override
	public HierarchyType mapRow(final ResultSet rs, final int rowNum) throws SQLException {
		HierarchyType hierarchyType = new HierarchyType();
		hierarchyType.setId(rs.getLong("id"));
		hierarchyType.setName(rs.getString("name"));
		hierarchyType.setCode(rs.getString("code"));
		hierarchyType.setLocalName(rs.getString("localname"));
		hierarchyType.setTenantId(rs.getString("tenantid"));
		hierarchyType.setCreatedBy(rs.getLong("createdby"));
		hierarchyType.setLastModifiedBy(rs.getLong("lastmodifiedby"));
		if(rs.getDate("createddate")!=null){
			hierarchyType.setCreatedDate(rs.getDate("createddate").getTime());
		}
		if(rs.getDate("lastmodifieddate")!=null){
			hierarchyType.setLastModifiedDate(rs.getDate("lastmodifieddate").getTime());
		}
		if(hierarchyType.getId() == null){
			hierarchyType.setNew(true);
		}
		
		return hierarchyType;
	}
}
