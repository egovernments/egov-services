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
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import org.egov.access.domain.model.Action;
import org.egov.access.persistence.repository.querybuilder.ActionQueryBuilder;
import org.egov.access.persistence.repository.rowmapper.RoleActionServiceRowMapper;
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

	public ActionService getAllActionsBasedOnRoles(ActionRequest actionRequest){
		
		
		StringBuilder queryBuilder = new StringBuilder();
		
				
	queryBuilder.append("SELECT service.id as serviceId,service.code as serviceCode, service.name as serviceNmae,service.contextroot as serviceContextRoot,"+ 
				"service.displayName as serviceDisplayName,service.parentmodule as serviceParentModule , "+
				"action.id as actionId,action.name as actionName,action.url as actionUrl,action.servicecode as actionServiceCode, "+ 
				"action.queryparams as actionQueryParams, action.parentmodule as actionParentModule,action.displayname as actionDisplayName "+
				"FROM service service LEFT JOIN eg_action action ON service.code = action.servicecode LEFT JOIN eg_roleaction roleaction ON roleaction.actionid = action.id where ");
				
	
		
		List<String> sqlStringifiedCodes = new ArrayList<>();
        for (String roleName : actionRequest.getRoleCodes()) {
            sqlStringifiedCodes.add(String.format("'%s'", roleName));
        }
        queryBuilder.append(" roleaction.rolecode IN (" + String.join(",", sqlStringifiedCodes) + ")");
        
        queryBuilder.append(" and service.tenantid = '"+ actionRequest.getTenantId()+"'");
        
        queryBuilder.append(" UNION ");
        
        queryBuilder.append(" SELECT service.id as serviceId,service.code as serviceCode, service.name as serviceNmae,service.contextroot as serviceContextRoot, "+
				"service.displayName as serviceDisplayName,service.parentmodule as serviceParentModule ,"+ 
				"subtable.id as actionId,subtable.name as actionName,subtable.url as actionUrl,subtable.servicecode as actionServiceCode, "+
				"subtable.queryparams as actionQueryParams, subtable.parentmodule as actionParentModule,subtable.displayname as actionDisplayName "+
				"FROM service service LEFT JOIN "+
				"(SELECT * FROM eg_action action LEFT JOIN eg_roleaction roleaction ON roleaction.actionid = action.id where ");
        
        queryBuilder.append(" roleaction.rolecode IN (" + String.join(",", sqlStringifiedCodes) + "))");
        
        queryBuilder.append(" subtable ON service.code = subtable.servicecode where service.tenantid = '"+actionRequest.getTenantId()+ "'"+
							" AND service.id IN (SELECT NULLIF(serv.parentmodule, '')::int FROM eg_action action LEFT JOIN eg_roleaction roleaction ON roleaction.actionid = action.id LEFT JOIN service serv ON "+ 
							"action.servicecode = serv.code where");
        
        queryBuilder.append(" roleaction.rolecode IN (" + String.join(",", sqlStringifiedCodes) + "))");
        
                   
      ///  queryBuilder.append("and  roleaction.tenantid = '"+ actionRequest.getTenantId()+"'");
        
       // queryBuilder.append(" order by action.id"); 
		
        RoleActionServiceRowMapper roleActionserviceRowMapper = new RoleActionServiceRowMapper();
        LOGGER.info("Query : " + queryBuilder.toString());
        jdbcTemplate.query(queryBuilder.toString(), roleActionserviceRowMapper);
        
        return prepareActionServiceList(roleActionserviceRowMapper);
	}


	private ActionService prepareActionServiceList(RoleActionServiceRowMapper rowMapper) {
	ActionService actionService = new ActionService();
		List<Module> moduleList = new ArrayList<>();
		Iterator<Entry<Long, Module>> moduleMapItr = rowMapper.moduleMap.entrySet().iterator();
		while(moduleMapItr.hasNext()){ 
			Entry<Long, Module> entry= moduleMapItr.next();
			moduleList.add(entry.getValue());
		}
		actionService.setModules(moduleList);
		return actionService; 
	}
	
}


