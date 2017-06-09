package org.egov.property.model;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.egov.models.Boundary;
import org.egov.models.PropertyLocation;
import org.springframework.jdbc.core.RowMapper;

public class PropertyLocationRowMapper implements RowMapper<Object> {

	@Override
	public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
	
		PropertyLocation propertyLocation = new PropertyLocation();
		
		
		propertyLocation.setId(Long.valueOf(rs.getInt("id")));
		Boundary boundary = new Boundary();
		boundary.setId(Long.valueOf(rs.getInt("revenueboundary")));
		propertyLocation.setRevenueBoundary(boundary);
		boundary = new Boundary();
		boundary.setId(Long.valueOf(rs.getInt("locationboundary")));
		propertyLocation.setLocationBoundary(boundary);
		boundary = new Boundary();
		boundary.setId(Long.valueOf(rs.getInt("adminboundary")));
		propertyLocation.setAdminBoundary(boundary);
		propertyLocation.setNorthBoundedBy(rs.getString("northboundedby"));
		propertyLocation.setEastBoundedBy(rs.getString("eastboundedby"));
		propertyLocation.setWestBoundedBy(rs.getString("westboundedby"));
		propertyLocation.setSouthBoundedBy("southboundedby");
		
		return propertyLocation;
	}

}
