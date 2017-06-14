package org.egov.commons.repository.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.egov.commons.model.BusinessCategory;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

@Component
public class BusinessCategoryRowMapper implements RowMapper<BusinessCategory>{
	@Override
	public BusinessCategory mapRow(ResultSet rs, int rowNum)throws SQLException{
		BusinessCategory businessCategory=new BusinessCategory();
		businessCategory.setId(rs.getLong("id"));
		businessCategory.setCode(rs.getString("code"));
		businessCategory.setName(rs.getString("name"));
		businessCategory.setIsactive(((Boolean)rs.getObject("active")));
		businessCategory.setTenantId(rs.getString("tenantId"));
		businessCategory.setCreatedBy(rs.getLong("createdBy"));
		businessCategory.setCreatedDate(rs.getTimestamp("createdDate"));
		businessCategory.setLastModifiedBy(rs.getLong("lastModifiedBy"));
		businessCategory.setLastModifiedDate(rs.getTimestamp("lastModifiedDate"));
		return businessCategory;
		}
}
