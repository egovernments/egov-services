package org.egov.boundary.persistence.repository.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.egov.boundary.domain.model.Boundary;
import org.egov.boundary.web.contract.CrossHierarchy;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

@Component
public class CrossHierarchyLocationNameRowMapper implements RowMapper<CrossHierarchy> {

	@Override
	public CrossHierarchy mapRow(final ResultSet rs, final int rowNum) throws SQLException {
		final CrossHierarchy crossHierarchy = new CrossHierarchy();

		crossHierarchy.setId(rs.getLong("id"));
		Boundary child = Boundary.builder().build();
		child.setName(rs.getString("childName"));
		Boundary childParentparent = Boundary.builder().build();
		childParentparent.setName(rs.getString("childParentname"));
		child.setParent(childParentparent);
		crossHierarchy.setChild(child);
		Boundary parent = Boundary.builder().build();
		parent.setName(rs.getString("parentName"));
		crossHierarchy.setParent(parent);
		return crossHierarchy;
	}
}
