package org.egov.boundary.persistence.repository.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.egov.boundary.web.contract.City;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

@Component
public class CityRowMapper implements RowMapper<City> {

	@Override
	public City mapRow(final ResultSet rs, final int rowNum) throws SQLException {

		City city = City.builder().domainURL(rs.getString("domainurl")).name(rs.getString("name"))
				.regionName(rs.getString("region_name")).id(rs.getString("id"))
				.code(rs.getString("code")).districtCode(rs.getString("district_code"))
				.districtName(rs.getString("district_name")).tenantId(rs.getString("tenantid"))
				.grade(rs.getString("grade")).build();
		return city;
	}
}
