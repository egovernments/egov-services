package org.egov.commons.repository.rowmapper;

import static org.springframework.util.ObjectUtils.isEmpty;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.egov.commons.model.BusinessCategory;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

@Component
public class BusinessCategoryRowMapper implements RowMapper<BusinessCategory> {
	@Override
	public BusinessCategory mapRow(ResultSet rs, int rowNum) throws SQLException {
		final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		BusinessCategory businessCategory = new BusinessCategory();
		businessCategory.setId(rs.getLong("id"));
		businessCategory.setCode(rs.getString("code"));
		businessCategory.setName(rs.getString("name"));
		businessCategory.setIsactive(((Boolean) rs.getObject("active")));
		businessCategory.setTenantId(rs.getString("tenantId"));
		businessCategory.setCreatedBy(rs.getLong("createdBy"));

		businessCategory.setLastModifiedBy(rs.getLong("lastModifiedBy"));
		businessCategory.setLastModifiedDate(rs.getLong("lastModifiedDate"));
		try {
			Date date = isEmpty(rs.getDate("createdDate")) ? null : sdf.parse(sdf.format(rs.getDate("createdDate")));
			businessCategory.setCreatedDate(date.getTime());
			date = isEmpty(rs.getDate("lastModifiedDate")) ? null
					: sdf.parse(sdf.format(rs.getDate("lastModifiedDate")));
			businessCategory.setLastModifiedDate(date.getTime());
		} catch (ParseException e) {
			e.printStackTrace();
			throw new SQLException("Parse exception while parsing date");
		}
		return businessCategory;
	}
}
