package org.egov.user.repository.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.egov.user.domain.model.Role;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

@Component
public class UserRoleRowMapper implements RowMapper<Role> {

	@Override
	public Role mapRow(final ResultSet rs, final int rowNum) throws SQLException {
		final Role role = Role.builder().id(rs.getLong("roleid")).tenantId(rs.getString("roleidtenantid")).build();
		return role;
	}
}