package org.egov.access.persistence.repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.egov.access.domain.model.Action;
import org.egov.access.domain.model.Role;
import org.egov.access.domain.model.RoleAction;
import org.egov.access.web.contract.action.RoleActionsRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class RoleActionRepository {

	public static final Logger LOGGER = LoggerFactory.getLogger(RoleActionRepository.class);

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public List<RoleAction> createRoleActions(final RoleActionsRequest actionRequest) {

		LOGGER.info("Create Role Actions Repository::" + actionRequest);
		final String roleActionsInsert = "insert into eg_roleaction values (?,?,?)";

		List<String> sqlStringifiedCodes = new ArrayList<>();
        for (Action actionName : actionRequest.getActions()) 
            sqlStringifiedCodes.add(String.format("'%s'", actionName.getName()));
        
		
		final String getActionIdsBasedOnName = "select id from eg_action where name IN ("+ String.join(",", sqlStringifiedCodes) + ")";
		
		List<Integer>  actionList = jdbcTemplate.queryForList(getActionIdsBasedOnName, Integer.class);
		
		Role role = actionRequest.getRole();

		List<RoleAction> roleActionList = new ArrayList<RoleAction>();

		jdbcTemplate.batchUpdate(roleActionsInsert, new BatchPreparedStatementSetter() {

			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				
				int action = actionList.get(i);
				ps.setString(1, role.getCode());
				ps.setLong(2, action);
				ps.setString(3, actionRequest.getTenantId());
                
				RoleAction roleAction = new RoleAction();

				roleAction.setActionId(action);
				roleAction.setRoleCode(role.getCode());
				roleAction.setTenantId(actionRequest.getTenantId());
				
				roleActionList.add(roleAction);
			}

			@Override
			public int getBatchSize() {
				return actionList.size();
			}
		});

		return roleActionList;
	}

}
