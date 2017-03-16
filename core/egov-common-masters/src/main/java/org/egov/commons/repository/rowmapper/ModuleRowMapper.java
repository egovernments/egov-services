package org.egov.commons.repository.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.egov.commons.model.Module;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

@Component
public class ModuleRowMapper implements RowMapper<Module> {
	@Override
	public Module mapRow(ResultSet rs, int rowNum) throws SQLException {
		Module module = new Module();
		module.setId(rs.getLong("id"));
		module.setName(rs.getString("name"));
		module.setEnabled(rs.getBoolean("enabled"));
		module.setContextRoot(rs.getString("contextroot"));
		module.setParentModule(rs.getLong("parentmodule"));
		module.setDisplayName(rs.getString("displayname"));
		module.setOrderNumber(rs.getLong("ordernumber"));
		return module;
	}
}
