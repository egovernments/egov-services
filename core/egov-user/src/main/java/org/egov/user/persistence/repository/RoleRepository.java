package org.egov.user.persistence.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.egov.user.domain.model.Role;
import org.egov.user.repository.builder.RoleQueryBuilder;
import org.egov.user.repository.rowmapper.RoleRowMapper;
import org.egov.user.repository.rowmapper.UserRoleRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class RoleRepository {

	private NamedParameterJdbcTemplate namedParameterJdbcTemplat;
	
	public RoleRepository(NamedParameterJdbcTemplate namedParameterJdbcTemplat){
		this.namedParameterJdbcTemplat = namedParameterJdbcTemplat;
	}

	public List<Role> getUserRoles(final long userId, final String tenantId) {

		final Map<String, Object> parametersMap = new HashMap<String, Object>();
		parametersMap.put("userId", userId);
		parametersMap.put("tenantId", tenantId);
		List<Role> roleList = namedParameterJdbcTemplat.query(RoleQueryBuilder.GET_ROLES_BY_ID_TENANTID, parametersMap,
				new UserRoleRowMapper());
		List<Long> roleIdList = new ArrayList<Long>();
		String tenantid = null;
		if (!roleList.isEmpty()) {
			for (Role role : roleList) {
				roleIdList.add(role.getId());
				tenantid = role.getTenantId();
			}
		}
		List<Role> roles = new ArrayList<Role>();
		if (!roleIdList.isEmpty()) {

			final Map<String, Object> Map = new HashMap<String, Object>();
			Map.put("id", roleIdList);
			Map.put("tenantId", tenantid);

			roles = namedParameterJdbcTemplat.query(RoleQueryBuilder.GET_ROLES_BY_ROLEIDS, Map, new RoleRowMapper());
		}

		return roles;
	}

	public Role findByTenantIdAndCode(String tenantId, String code) {

		final Map<String, Object> parametersMap = new HashMap<String, Object>();
		parametersMap.put("code", code);
		parametersMap.put("tenantId", tenantId);
		Role role = null;
		List<Role> roleList = namedParameterJdbcTemplat
				.query(RoleQueryBuilder.GET_ROLE_BYTENANT_ANDCODE, parametersMap, new RoleRowMapper());

		if (!roleList.isEmpty()) {
			role = roleList.get(0);
		}
		return role;
	}
}
