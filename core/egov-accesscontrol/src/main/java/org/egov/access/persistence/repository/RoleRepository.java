package org.egov.access.persistence.repository;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import org.egov.access.domain.model.Role;
import org.egov.access.persistence.repository.querybuilder.RoleQueryBuilder;
import org.egov.access.web.contract.role.RoleRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class RoleRepository {

	public static final Logger LOGGER = LoggerFactory.getLogger(RoleRepository.class);

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public List<Role> createRole(final RoleRequest roleRequest) {

		LOGGER.info("Create Role Repository::" + roleRequest);
		final String roleInsert = RoleQueryBuilder.insertRoleQuery();

		List<Role> roles = roleRequest.getRoles();

		jdbcTemplate.batchUpdate(roleInsert, new BatchPreparedStatementSetter() {

			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				Role role = roles.get(i);

				ps.setString(1, role.getName());
				ps.setString(2, role.getCode());
				ps.setString(3, role.getDescription());
				ps.setLong(4, roleRequest.getRequestInfo().getUserInfo().getId());
				ps.setDate(5, new Date(new java.util.Date().getTime()));
				ps.setLong(6, roleRequest.getRequestInfo().getUserInfo().getId());
				ps.setDate(7, new Date(new java.util.Date().getTime()));

			}

			@Override
			public int getBatchSize() {
				return roles.size();
			}
		});

		return roles;

	}

	public List<Role> updateRole(final RoleRequest roleRequest) {

		LOGGER.info("update Role Repository::" + roleRequest);
		final String roleUpdate = RoleQueryBuilder.updateRoleQuery();

		List<Role> roles = roleRequest.getRoles();
		jdbcTemplate.batchUpdate(roleUpdate, new BatchPreparedStatementSetter() {

			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				Role role = roles.get(i);

				ps.setString(1, role.getCode());
				ps.setString(2, role.getDescription());
				ps.setLong(3, roleRequest.getRequestInfo().getUserInfo().getId());
				ps.setDate(4, new Date(new java.util.Date().getTime()));
				ps.setString(5, role.getName());
			}

			@Override
			public int getBatchSize() {
				return roles.size();
			}
		});

		return roles;
	}
	
	public boolean checkRoleNameDuplicationValidationErrors(String roleName){
		
		final String query = RoleQueryBuilder.checkRoleNameDuplicationValidationErrors();
       
		String cnt =null;
		try{
		 cnt = jdbcTemplate.queryForObject(query, String.class, roleName);
		}catch(EmptyResultDataAccessException ex){
			ex.hashCode();
		}
		
		if(cnt!=null && cnt!=""){
			
			return true;
		}
		
		 return false;
	}

}
