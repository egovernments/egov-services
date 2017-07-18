package org.egov.access.persistence.repository.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.egov.access.domain.model.Action;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

@Component
public class ActionSearchRowMapper implements RowMapper<Action> {

	
	
	@Override
	public Action mapRow(final ResultSet rs, final int rowNum) throws SQLException {

		Action action = new Action();
		action.setId(rs.getLong("id"));
		action.setUrl(rs.getString("url"));
		action.setName(rs.getString("name"));
		action.setServiceCode(rs.getString("servicecode"));
		action.setDisplayName(rs.getString("displayname"));
		action.setEnabled(rs.getBoolean("enabled"));
		action.setParentModule(rs.getString("parentmodule"));	
		action.setQueryParams(rs.getString("queryparams"));
		action.setOrderNumber(rs.getInt("ordernumber"));

		return action;
	}
}