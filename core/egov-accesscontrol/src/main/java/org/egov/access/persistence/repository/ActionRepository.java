/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 * accountability and the service delivery of the government  organizations.
 *
 *  Copyright (C) 2016  eGovernments Foundation
 *
 *  The updated version of eGov suite of products as by eGovernments Foundation
 *  is available at http://www.egovernments.org
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program. If not, see http://www.gnu.org/licenses/ or
 *  http://www.gnu.org/licenses/gpl.html .
 *
 *  In addition to the terms of the GPL license to be adhered to in using this
 *  program, the following additional terms are to be complied with:
 *
 *      1) All versions of this program, verbatim or modified must carry this
 *         Legal Notice.
 *
 *      2) Any misrepresentation of the origin of the material is prohibited. It
 *         is required that all modified versions of this material be marked in
 *         reasonable ways as different from the original version.
 *
 *      3) This license does not grant any rights to any user of the program
 *         with regards to rights under trademark law for use of the trade names
 *         or trademarks of eGovernments Foundation.
 *
 *  In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */

package org.egov.access.persistence.repository;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.egov.access.domain.model.Action;
import org.egov.access.persistence.repository.querybuilder.ActionQueryBuilder;
import org.egov.access.persistence.repository.rowmapper.ActionSearchRowMapper;
import org.egov.access.persistence.repository.rowmapper.ModuleSearchRowMapper;
import org.egov.access.web.contract.action.ActionRequest;
import org.egov.access.web.contract.action.ActionService;
import org.egov.access.web.contract.action.Module;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class ActionRepository {

	public static final Logger LOGGER = LoggerFactory.getLogger(ActionRepository.class);

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public List<Action> createAction(final ActionRequest actionRequest) {

		LOGGER.info("Create Action Repository::" + actionRequest);
		final String actionInsert = ActionQueryBuilder.insertActionQuery();

		List<Action> actions = actionRequest.getActions();

		jdbcTemplate.batchUpdate(actionInsert, new BatchPreparedStatementSetter() {

			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				Action action = actions.get(i);
				ps.setString(1, action.getName());
				ps.setString(2, action.getUrl());
				ps.setString(3, action.getServiceCode());
				ps.setString(4, action.getQueryParams());
				ps.setString(5, action.getParentModule());
				if (action.getOrderNumber() != null) {
					ps.setInt(6, action.getOrderNumber());
				} else {
					ps.setInt(6, 0);
				}
				ps.setString(7, action.getDisplayName());
				ps.setBoolean(8, action.isEnabled());
				ps.setLong(9, Long.valueOf(actionRequest.getRequestInfo().getUserInfo().getId()));
				ps.setLong(10, Long.valueOf(actionRequest.getRequestInfo().getUserInfo().getId()));
				ps.setDate(11, new Date(new java.util.Date().getTime()));
				ps.setDate(12, new Date(new java.util.Date().getTime()));
			}

			@Override
			public int getBatchSize() {
				return actions.size();
			}
		});

		return actions;
	}

	public List<Action> updateAction(final ActionRequest actionRequest) {

		LOGGER.info("update Action Repository::" + actionRequest);
		final String actionUpdate = ActionQueryBuilder.updateActionQuery();

		List<Action> actions = actionRequest.getActions();

		jdbcTemplate.batchUpdate(actionUpdate, new BatchPreparedStatementSetter() {

			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				Action action = actions.get(i);

				ps.setString(1, action.getUrl());
				ps.setString(2, action.getServiceCode());
				ps.setString(3, action.getQueryParams());
				ps.setString(4, action.getParentModule());
				if (action.getOrderNumber() != null) {
					ps.setInt(5, action.getOrderNumber());
				} else {
					ps.setInt(5, 0);
				}
				ps.setString(6, action.getDisplayName());
				ps.setBoolean(7, action.isEnabled());
				ps.setLong(8, Long.valueOf(actionRequest.getRequestInfo().getUserInfo().getId()));
				ps.setDate(9, new Date(new java.util.Date().getTime()));
				ps.setString(10, action.getName());
			}

			@Override
			public int getBatchSize() {
				return actions.size();
			}
		});
		return actions;
	}

	public boolean checkActionNameExit(String actionName) {

		String Query = ActionQueryBuilder.checkActionNameExit();

		Integer cnt = null;
		try {
			cnt = jdbcTemplate.queryForObject(Query, Integer.class, actionName);
		} catch (EmptyResultDataAccessException ex) {
			ex.hashCode();
		}

		if (cnt != null && cnt > 0) {

			return true;
		}

		return false;
	}

	public boolean checkCombinationOfUrlAndqueryparamsExist(String url, String queryParams) {

		String Query = ActionQueryBuilder.checkCombinationOfUrlAndqueryparamsExist();

		Integer cnt = null;

		try {
			cnt = jdbcTemplate.queryForObject(Query, Integer.class, url, queryParams);
		} catch (EmptyResultDataAccessException ex) {
			ex.hashCode();
		}

		if (cnt != null && cnt > 0) {

			return true;
		}

		return false;

	}

	public ActionService getAllActionsBasedOnRoles(ActionRequest actionRequest, Boolean enabled) {

		StringBuilder allservicesQueryBuilder = new StringBuilder();
		StringBuilder actionQueryBuilder = new StringBuilder();
		StringBuilder servicesQueryBuilder = new StringBuilder();

		ActionService service = new ActionService();

		service.setModules(new ArrayList<Module>());
		ModuleSearchRowMapper moduleRowMapper = new ModuleSearchRowMapper();

		ActionSearchRowMapper actionRowMapper = new ActionSearchRowMapper();

		List<Module> moduleList = null;

		List<String> sqlStringifiedCodes = new ArrayList<>();
		for (String roleName : actionRequest.getRoleCodes()) {
			sqlStringifiedCodes.add(String.format("'%s'", roleName));
		}

		actionQueryBuilder.append(
				"select id,name,displayname,servicecode,url,queryparams from eg_action action where id IN(select actionid from eg_roleaction roleaction where");

		actionQueryBuilder.append(" roleaction.rolecode IN ( select code from eg_ms_role where code in ("
				+ String.join(",", sqlStringifiedCodes) + "))" + "and roleaction.tenantid = '"
				+ actionRequest.getTenantId() + "' ) ");

		if (enabled != null)
			actionQueryBuilder.append(" and enabled='" + enabled + "'");

		jdbcTemplate.query(actionQueryBuilder.toString(), actionRowMapper);
		Map<String, List<Action>> actionMap = actionRowMapper.actionMap;

		if (actionMap.size() > 0) {

			servicesQueryBuilder.append(
					"select id,name,code,parentmodule,displayname from service service where service.code in (select DISTINCT(action.servicecode) from eg_roleaction roleaction,eg_action action where ");
			servicesQueryBuilder.append(" roleaction.rolecode IN (" + String.join(",", sqlStringifiedCodes) + ")"
					+ "and roleaction.tenantid = '" + actionRequest.getTenantId() + "'");
			if (enabled != null)
				servicesQueryBuilder.append(" and enabled='" + enabled + "'");

			servicesQueryBuilder.append(" and action.id = roleaction.actionid) and service.tenantid = '"
					+ actionRequest.getTenantId() + "'");
			if (enabled != null)
				servicesQueryBuilder.append("  and service.enabled='" + enabled + "'");

			moduleList = jdbcTemplate.query(servicesQueryBuilder.toString(), moduleRowMapper);

		}

		List<String> moduleCodes = null;

		if (moduleList != null && moduleList.size() > 0) {

			moduleCodes = new ArrayList<>();
			for (Module module : moduleList) {
				moduleCodes.add(String.format("'%s'", module.getId()));
			}
		}

		List<Module> allServiceList = null;

		if (moduleCodes != null && moduleCodes.size() > 0) {

			allservicesQueryBuilder.append("(WITH RECURSIVE nodes(id,code,name,parentmodule,displayname) AS ("
					+ " SELECT s1.id,s1.code, s1.name, s1.parentmodule,s1.displayname" + " FROM service s1 WHERE "
					+ " id IN (" + String.join(",", moduleCodes) + ") UNION ALL"
					+ " SELECT s1.id,s1.code, s1.name, s1.parentmodule,s1.displayname"
					+ " FROM nodes s2, service s1 WHERE CAST(s1.parentmodule as bigint) = s2.id");

			if (enabled != null) {

				allservicesQueryBuilder.append(" and s1.tenantid = '" + actionRequest.getTenantId()
						+ "' and s1.enabled ='" + actionRequest.getEnabled() + "')");

			} else {
				allservicesQueryBuilder.append(" and s1.tenantid = '" + actionRequest.getTenantId() + "')");
			}

			allservicesQueryBuilder.append(" SELECT * FROM nodes)" + " UNION"
					+ " (WITH RECURSIVE nodes(id,code,name,parentmodule,displayname) AS ("
					+ " SELECT s1.id,s1.code, s1.name, s1.parentmodule,s1.displayname" + " FROM service s1 WHERE "
					+ " id IN (" + String.join(",", moduleCodes) + ") UNION ALL"
					+ " SELECT s1.id,s1.code, s1.name, s1.parentmodule,s1.displayname"
					+ " FROM nodes s2, service s1 WHERE CAST(s2.parentmodule as bigint) = s1.id"
					+ " and s1.tenantid = '" + actionRequest.getTenantId() + "')" + " SELECT * FROM nodes);");

			allServiceList = jdbcTemplate.query(allservicesQueryBuilder.toString(), moduleRowMapper);
		}

		LOGGER.info("Action Query : " + actionQueryBuilder.toString());
		LOGGER.info("services Query : " + servicesQueryBuilder.toString());
		LOGGER.info("All Services Query : " + allservicesQueryBuilder.toString());

		if (allServiceList != null && allServiceList.size() > 0) {

			List<Module> rootModules = prepareListOfServices(allServiceList, actionMap);

			for (Module module : rootModules) {

				getSubmodule(module, allServiceList, actionMap);

			}

			removeMainModuleDoesnotExistActions(rootModules);

			service.setModules(rootModules);
		}

		return service;
	}

	private List<Module> prepareListOfServices(List<Module> moduleList, Map<String, List<Action>> actionMap) {

		List<Module> mainModules = new ArrayList<Module>();

		for (Module module : moduleList) {

			if (module.getParentModule() == null || module.getParentModule().isEmpty()) {

				List<Module> subModule = new ArrayList<Module>();
				if (actionMap.containsKey(module.getCode())) {

					module.setActionList(actionMap.get(module.getCode()));
				}

				module.setSubModules(subModule);

				mainModules.add(module);

			}
		}

		return mainModules;
	}

	private Module getSubmodule(Module module, List<Module> allModules, Map<String, List<Action>> actionMap) {

		if (module.getSubModules().size() != 0) {

			List<Module> subModuleList = new ArrayList<Module>();

			module.setSubModules(subModuleList);
		}

		for (Module module1 : allModules) {

			if (module.getId().toString().equals(module1.getParentModule())) {

				if (actionMap.containsKey(module.getCode())) {

					module.setActionList(actionMap.get(module.getCode()));
				}

				module.getSubModules().add(module1);

			}

		}

		if (module.getSubModules().size() != 0) {

			for (Module sub : module.getSubModules()) {

				List<Module> subModuleList = new ArrayList<Module>();

				sub.setSubModules(subModuleList);
				getSubmodule(sub, allModules, actionMap);
			}
		}

		return module;
	}

	private void removeMainModuleDoesnotExistActions(List<Module> modules) {

		if (modules.size() > 0) {

			for (int i = 0; i < modules.size(); i++) {

				if (modules.get(i).getSubModules() != null && modules.get(i).getSubModules().size() == 0
						&& modules.get(i).getActionList() == null) {

					modules.remove(i);
				}

			}
		}

	}

}
