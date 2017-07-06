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

package org.egov.access.persistence.repository.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.egov.access.domain.model.Action;
import org.egov.access.web.contract.action.ActionService;
import org.egov.access.web.contract.action.Module;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

@Component
public class RoleActionServiceRowMapper implements RowMapper<ActionService> {

	ActionService actionService = new ActionService();

	public Map<Long, Module> moduleMap = new HashMap<>();

	public Map<Long, Action> actionMap = new HashMap<>();

	public Map<Long, List<Long>> moduleActionMap = new HashMap<>();

	public Map<Long, Module> subModuleMap = new HashMap<>();

	@Override
	public ActionService mapRow(final ResultSet rs, final int rowNum) throws SQLException {
		Module module = new Module();
		String serviceParent = "0";
		if (null != rs.getString("serviceParentModule") && !rs.getString("serviceParentModule").isEmpty()) {
			serviceParent = rs.getString("serviceParentModule");
		}

		Long parentId = Long.parseLong(serviceParent);
		if (parentId <= 0) {
			if (!moduleMap.containsKey(rs.getLong("serviceId"))) {
				module = prepareModuleObject(rs);
				List<Action> actionList = new ArrayList<>();
				actionList.add(prepareActionObject(rs));
				module.setActionList(actionList);
				List<Module> subModuleList = new ArrayList<>();
				module.setSubModules(subModuleList);
				moduleMap.put(rs.getLong("serviceId"), module);
			} else {
				module = moduleMap.get(rs.getLong("serviceId"));
				module.getActionList().add(prepareActionObject(rs));
			}
		} else {
			if (moduleMap.containsKey(parentId)) {
				if (!subModuleMap.containsKey(rs.getLong("serviceId"))) {
					module = moduleMap.get(parentId);
					Module subModule = prepareModuleObject(rs);
					List<Module> subModuleList = new ArrayList<>();
					subModule.setSubModules(subModuleList);
					List<Action> actionList = new ArrayList<>();
					actionList.add(prepareActionObject(rs));
					subModule.setActionList(actionList);
					module.getSubModules().add(subModule);
					subModuleMap.put(rs.getLong("serviceId"), subModule);
				} else {
					Module subModule = new Module();
					subModule = subModuleMap.get(rs.getLong("serviceId"));
					subModule.getActionList().add(prepareActionObject(rs));
				}
			} else {
				Iterator<Entry<Long, Module>> innerItr = moduleMap.entrySet().iterator();
				Module parentModule = new Module();
				List<Module> subList = new ArrayList<>();
				parentModule.setSubModules(subList);
				while (innerItr.hasNext()) {
					Module eachModuleOfModuleMap = innerItr.next().getValue();
					Module myParent = getMyParent(eachModuleOfModuleMap, parentId);
					if (null != myParent.getId()) {
						parentModule = myParent;
					}
				}
				Module subModule = prepareModuleObject(rs);
				List<Module> subModuleList = new ArrayList<>();
				subModule.setSubModules(subModuleList);
				List<Action> actionList = new ArrayList<>();
				actionList.add(prepareActionObject(rs));
				subModule.setActionList(actionList);
				parentModule.getSubModules().add(subModule);
			}

		}
		return null;
	}

	private Module getMyParent(Module module, Long parentId) {
		Module parent = new Module();
		if (module.getSubModules().size() > 0) {
			for (int i = 0; i < module.getSubModules().size(); i++) {
				if (module.getSubModules().get(i).getId().equals(parentId)) {
					return module.getSubModules().get(i);
				} else {
					parent = getMyParent(module.getSubModules().get(i), parentId);
				}

			}
		}
		return parent;
	}

	private Module prepareModuleObject(ResultSet rs) throws SQLException {
		Module module = new Module();
		module.setId(rs.getLong("serviceId"));
		module.setCode(rs.getString("serviceCode"));
		module.setName(rs.getString("serviceNmae"));
		module.setContextRoot(rs.getString("serviceContextRoot"));
		module.setDisplayName(rs.getString("serviceDisplayName"));
		module.setParentModule(
				null == rs.getString("serviceParentModule") || rs.getString("serviceParentModule").isEmpty() ? "0"
						: rs.getString("serviceParentModule"));
		return module;
	}

	private Action prepareActionObject(ResultSet rs) throws SQLException {
		Action action = new Action();
		action.setId(rs.getLong("actionId"));
		action.setName(rs.getString("actionName"));
		action.setUrl(rs.getString("actionUrl"));
		action.setServiceCode(rs.getString("actionServiceCode"));
		action.setQueryParams(rs.getString("actionQueryParams"));
		action.setParentModule(rs.getString("actionParentModule"));
		action.setDisplayName(rs.getString("actionDisplayName"));
		return action;
	}

}
