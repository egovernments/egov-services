package org.egov.access.persistence.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.egov.access.domain.model.Action;
import org.egov.access.domain.model.Role;
import org.egov.access.domain.model.RoleAction;
import org.egov.access.web.contract.action.RoleActionsRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

@Repository
public class RoleActionRepository {

	public static final Logger LOGGER = LoggerFactory.getLogger(RoleActionRepository.class);

	@Autowired
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	public List<RoleAction> createRoleActions(final RoleActionsRequest actionRequest) {

		LOGGER.info("Create Role Actions Repository::" + actionRequest);
		final String roleActionsInsert = "insert into eg_roleaction(rolecode,actionid,tenantid) values (:rolecode,:actionid,:tenantid)";

		List<String> sqlStringifiedCodes = new ArrayList<>();
		for (Action actionName : actionRequest.getActions())
			sqlStringifiedCodes.add(actionName.getName());

		final Map<String, Object> parametersMap = new HashMap<String, Object>();

		parametersMap.put("sqlStringifiedCodes", sqlStringifiedCodes);

		final String getActionIdsBasedOnName = "select id from eg_action where name IN (:sqlStringifiedCodes)";

		SqlRowSet sqlrowset = namedParameterJdbcTemplate.queryForRowSet(getActionIdsBasedOnName, parametersMap);

		List<Integer> actionList = new ArrayList<Integer>();

		while (sqlrowset.next()) {

			actionList.add(sqlrowset.getInt("id"));
		}

		Role role = actionRequest.getRole();

		List<RoleAction> roleActionList = new ArrayList<RoleAction>();

		List<Map<String, Object>> batchValues = new ArrayList<>(actionList.size());
		for (Integer actionid : actionList) {
			batchValues.add(new MapSqlParameterSource("rolecode", role.getCode()).addValue("actionid", actionid)
					.addValue("tenantid", actionRequest.getTenantId()).getValues());

			RoleAction roleAction = new RoleAction();

			roleAction.setActionId(actionid);
			roleAction.setRoleCode(role.getCode());
			roleAction.setTenantId(actionRequest.getTenantId());

			roleActionList.add(roleAction);
		}

		namedParameterJdbcTemplate.batchUpdate(roleActionsInsert, batchValues.toArray(new Map[actionList.size()]));

		return roleActionList;
	}

	public boolean checkActionNamesAreExistOrNot(final RoleActionsRequest actionRequest) {

		LOGGER.info("checkActionNamesAreExistOrNot::" + actionRequest);

		if (actionRequest.getActions().size() > 0) {

			List<String> sqlStringifiedCodes = new ArrayList<>();
			for (Action actionName : actionRequest.getActions())
				sqlStringifiedCodes.add(actionName.getName());

			final Map<String, Object> parametersMap = new HashMap<String, Object>();

			parametersMap.put("sqlStringifiedCodes", sqlStringifiedCodes);

			final String getActionIdsBasedOnName = "select id from eg_action where name IN (:sqlStringifiedCodes)";

			SqlRowSet sqlrowset = namedParameterJdbcTemplate.queryForRowSet(getActionIdsBasedOnName, parametersMap);

			List<Integer> actionList = new ArrayList<Integer>();

			while (sqlrowset.next()) {

				actionList.add(sqlrowset.getInt("id"));
			}

			if (actionList.size() == actionRequest.getActions().size()) {

				return true;
			}

		}

		return false;
	}

}
