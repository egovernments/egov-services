package org.egov.boundary.persistence.repository.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.egov.boundary.domain.model.Boundary;
import org.egov.boundary.web.contract.BoundaryType;
import org.egov.boundary.web.contract.CrossHierarchy;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

@Component
public class CrossHierarchyRowMapper implements RowMapper<CrossHierarchy>{
	
	@Override
	public CrossHierarchy mapRow(final ResultSet rs, final int rowNum) throws SQLException {
		final CrossHierarchy crossHierarchy = new CrossHierarchy();
		
		crossHierarchy.setId(rs.getLong("id"));
		crossHierarchy.setTenantId(rs.getString("tenantid"));
		crossHierarchy.setCode(rs.getString("code"));
		
		if(rs.getLong("child")!=0){
			Boundary child = Boundary.builder().id(rs.getLong("child")).build();
			crossHierarchy.setChild(child);		
		}
		if(rs.getLong("parent")!=0){
			Boundary parent = Boundary.builder().id(rs.getLong("parent")).build();
			crossHierarchy.setParent(parent);		
		}
		if(rs.getLong("parenttype")!=0){
			BoundaryType parentType = BoundaryType.builder().id(String.valueOf(rs.getLong("parenttype"))).build();
			crossHierarchy.setParentType(parentType);	
		}
		if(rs.getLong("childtype")!=0){
			BoundaryType childType = BoundaryType.builder().id(String.valueOf(rs.getLong("childtype"))).build();
			crossHierarchy.setChildType(childType);	
		}
		return crossHierarchy; 
	}
	

}
