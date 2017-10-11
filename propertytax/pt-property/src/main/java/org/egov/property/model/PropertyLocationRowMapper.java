package org.egov.property.model;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.egov.models.Boundary;
import org.egov.models.PropertyLocation;
import org.springframework.jdbc.core.RowMapper;

/**
 * This class will create property location object from the given result set
 * 
 * @author prasad
 *
 */
public class PropertyLocationRowMapper implements RowMapper<Object> {

	@Override
	public Object mapRow(ResultSet rs, int rowNum) throws SQLException {

		PropertyLocation propertyLocation = new PropertyLocation();

		propertyLocation.setId(getLong(rs.getInt("id")));
		Boundary boundary = new Boundary();
		boundary.setCode(getString(rs.getString("revenueboundary")));
		propertyLocation.setRevenueBoundary(boundary);
		boundary = new Boundary();
		boundary.setCode(getString(rs.getString("locationboundary")));
		propertyLocation.setLocationBoundary(boundary);
		boundary = new Boundary();
		boundary.setCode(getString(rs.getString("adminboundary")));
		propertyLocation.setAdminBoundary(boundary);
		propertyLocation.setGuidanceValueBoundary(getString(rs.getString("guidanceValueBoundary")));
		propertyLocation.setNorthBoundedBy(getString(rs.getString("northboundedby")));
		propertyLocation.setEastBoundedBy(getString(rs.getString("eastboundedby")));
		propertyLocation.setWestBoundedBy(getString(rs.getString("westboundedby")));
		propertyLocation.setSouthBoundedBy(getString(rs.getString("southboundedby")));

		return propertyLocation;
	}

	/**
	 * This method will cast the given object to String
	 * 
	 * @param object
	 *            that need to be cast to string
	 * @return {@link String}
	 */
	private String getString(Object object) {
		return object == null ? null : object.toString().trim();
	}

	/**
	 * This method will cast the given object to Long
	 * 
	 * @param object
	 *            that need to be cast to Long
	 * @return {@link Long}
	 */
	private Long getLong(Object object) {
		return Integer.valueOf(object.toString()) == 0 ? null : Long.parseLong(object.toString().trim());
	}

}
