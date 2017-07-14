package org.egov.access.persistence.repository;

import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.egov.access.domain.model.Role;
import org.egov.access.persistence.repository.querybuilder.RoleQueryBuilder;
import org.egov.access.web.contract.role.RoleRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

@Repository
public class RoleRepository {

	public static final Logger LOGGER = LoggerFactory.getLogger(RoleRepository.class);

	@Autowired
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	public List<Role> createRole(final RoleRequest roleRequest) {

		LOGGER.info("Create Role Repository::" + roleRequest);
		final String roleInsert = RoleQueryBuilder.insertRoleQuery();

		List<Role> roles = roleRequest.getRoles();

		List<Map<String, Object>> batchValues = new ArrayList<>(roles.size());
		for (Role role : roles) {
			batchValues.add(new MapSqlParameterSource("name", role.getName()).addValue("code", role.getCode())
					.addValue("description", role.getDescription())
					.addValue("createdby", Long.valueOf(roleRequest.getRequestInfo().getUserInfo().getId()))
					.addValue("createddate", new Date(new java.util.Date().getTime()))
					.addValue("lastmodifiedby", Long.valueOf(roleRequest.getRequestInfo().getUserInfo().getId()))
					.addValue("lastmodifieddate", new Date(new java.util.Date().getTime())).getValues());
		}

		namedParameterJdbcTemplate.batchUpdate(roleInsert, batchValues.toArray(new Map[roles.size()]));

		return roles;

	}

	public List<Role> updateRole(final RoleRequest roleRequest) {

		LOGGER.info("update Role Repository::" + roleRequest);
		final String roleUpdate = RoleQueryBuilder.updateRoleQuery();

		List<Role> roles = roleRequest.getRoles();

		List<Map<String, Object>> batchValues = new ArrayList<>(roles.size());

		for (Role role : roles) {
			batchValues.add(new MapSqlParameterSource("name", role.getName()).addValue("code", role.getCode())
					.addValue("description", role.getDescription())
					.addValue("lastmodifiedby", Long.valueOf(roleRequest.getRequestInfo().getUserInfo().getId()))
					.addValue("lastmodifieddate", new Date(new java.util.Date().getTime())).getValues());
		}

		namedParameterJdbcTemplate.batchUpdate(roleUpdate, batchValues.toArray(new Map[roles.size()]));

		return roles;
	}

	public boolean checkRoleNameDuplicationValidationErrors(String roleName) {

		final String query = RoleQueryBuilder.checkRoleNameDuplicationValidationErrors();

		final Map<String, Object> parametersMap = new HashMap<String, Object>();

		parametersMap.put("name", roleName);
		SqlRowSet sqlRowSet = namedParameterJdbcTemplate.queryForRowSet(query, parametersMap);

		if (sqlRowSet.next() && sqlRowSet.getString("code") != null && sqlRowSet.getString("code") != "") {

			return true;
		}

		return false;
	}

}
