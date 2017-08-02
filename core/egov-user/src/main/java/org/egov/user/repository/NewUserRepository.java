package org.egov.user.repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.egov.common.contract.request.RequestInfo;
import org.egov.user.model.Address;
import org.egov.user.model.Role;
import org.egov.user.model.TenantRole;
import org.egov.user.model.User;
import org.egov.user.model.UserDetails;
import org.egov.user.model.UserReq;
import org.egov.user.model.UserSearchCriteria;
import org.egov.user.model.enums.DbAction;
import org.egov.user.model.enums.Gender;
import org.egov.user.repository.mapper.UserRowMapper;
import org.egov.user.repository.querybuilder.UserQueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import lombok.extern.slf4j.Slf4j;

@Repository
@Slf4j
public class NewUserRepository {

	@Autowired
	private UserQueryBuilder userQueryBuilder;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public List<User> search(UserSearchCriteria userSearchCriteria) {
		final List<Object> preparedStatementValues = new ArrayList<>();
		String queryStr = userQueryBuilder.getQuery(userSearchCriteria, preparedStatementValues);
		List<User> users = null;
		try {
			log.info("queryStr::" + queryStr + "preparedStatementValues::" + preparedStatementValues.toString());
			users = jdbcTemplate.query(queryStr, preparedStatementValues.toArray(), new UserRowMapper());
			log.info("UserRepository users::" + users);
		} catch (final Exception ex) {
			ex.printStackTrace();
			log.info("the exception from findforcriteria : " + ex);
		}
		return users;
	}

	@Transactional
	public void saveUser(UserReq userReq) {
		log.info("NewUserRepository saveUser"+userReq);
		RequestInfo requestInfo = userReq.getRequestInfo();
		List<User> users = userReq.getUsers();
		List<Address> addresses = new ArrayList<>();
		List<Role> primaryrole = new ArrayList<>();
		List<TenantRole> additionalroles = new ArrayList<>();
		for (User user : users) {
			UserDetails userDetails = user.getUserDetails();
			
			for (Address address : userDetails.getAddresses()) 
				addresses.add(address);
			
			for (Role role : user.getPrimaryrole()) 
				primaryrole.add(role);
			
			for (TenantRole tenantRole : user.getAdditionalroles()) 
				additionalroles.add(tenantRole);
			
		}

		jdbcTemplate.batchUpdate(userQueryBuilder.USER_INSERT_QUERY, new BatchPreparedStatementSetter() {

			@Override
			public void setValues(PreparedStatement ps, int index) throws SQLException {
				User user = users.get(index);
				UserDetails userDetails = user.getUserDetails();

				ps.setString(1, null);// title
				ps.setString(2, user.getSalutation());
				ps.setDate(3, new java.sql.Date(userDetails.getDob().getTime()));
				ps.setString(4, user.getLocale());
				ps.setString(5, user.getUsername());
				ps.setString(6, user.getPassword());
				ps.setDate(7, new java.sql.Date(user.getPwdExpiryDate()));
				ps.setString(8, user.getMobile());
				ps.setString(9, userDetails.getAltContactNumber());
				ps.setString(10, user.getEmail());
				ps.setDate(11, new java.sql.Date(new Date().getTime()));
				ps.setDate(12, new java.sql.Date(new Date().getTime()));
				ps.setLong(13, requestInfo.getUserInfo().getId());
				ps.setLong(14, requestInfo.getUserInfo().getId());
				ps.setBoolean(15, true);
				ps.setString(16, user.getName());
				ps.setInt(17, Gender.valueOf(user.getGender().toString()).ordinal());
				ps.setString(18, userDetails.getPan());
				ps.setString(19, user.getAadhaarNumber());
				ps.setString(20, user.getType().toString());
				ps.setInt(21, 1);// version
				ps.setString(22, null);// guardian
				ps.setString(23, null);// guardianrelation
				ps.setString(24, userDetails.getSignature());
				ps.setBoolean(25, user.getAccountLocked());
				ps.setString(26, null);// bloodgroup
				ps.setString(27, userDetails.getPhoto());
				ps.setString(28, userDetails.getIdentificationMark());
				ps.setString(29, user.getTenantId());
				ps.setLong(30, user.getId());
			}

			@Override
			public int getBatchSize() {
				return users.size();
			}
		});

		saveUserAddress(addresses, requestInfo);
		saveUserRole(primaryrole, requestInfo);
		saveUserTenant(additionalroles, requestInfo);
	}

	public void saveUserAddress(List<Address> addresses, RequestInfo requestInfo) {

		jdbcTemplate.batchUpdate(userQueryBuilder.USER_ADDRESS_INSERT_QUERY, new BatchPreparedStatementSetter() {

			@Override
			public void setValues(PreparedStatement ps, int index) throws SQLException {
				Address address = addresses.get(index);
				ps.setLong(1, address.getId());
				ps.setInt(2, 1);
				ps.setDate(3, new java.sql.Date(new Date().getTime()));
				ps.setDate(4, new java.sql.Date(new Date().getTime()));
				ps.setLong(5, requestInfo.getUserInfo().getId());
				ps.setLong(6, requestInfo.getUserInfo().getId());
				ps.setString(7, address.getAddressType().toString());
				ps.setString(8, address.getAddressLine1());
				ps.setString(9, address.getCity());
				ps.setString(10, address.getPincode());
				ps.setLong(11, address.getUserId());
				ps.setString(12, address.getTenantId());
			}

			@Override
			public int getBatchSize() {
				return addresses.size();
			}
		});
	}

	public void saveUserRole(List<Role> roles, RequestInfo requestInfo) {
		
		log.info("NewUserRepository saveUserRole roles: "+roles);
		jdbcTemplate.batchUpdate(userQueryBuilder.USERROLE_INSERT_QUERY, new BatchPreparedStatementSetter() {
			
			@Override
			public void setValues(PreparedStatement ps, int index) throws SQLException {
				Role role = roles.get(index);
				ps.setLong(1, role.getId());
				ps.setString(2, role.getTenantId());
				ps.setLong(3, role.getUserId());
				ps.setString(4, role.getTenantId());
				ps.setDate(5, new java.sql.Date(new Date().getTime()));
			}

			@Override
			public int getBatchSize() {
				return roles.size();
			}
		});

	}

	public void saveUserTenant(List<TenantRole> tenantRoles, RequestInfo requestInfo) {
		log.info("saveUserTenant tenantRoles:"+tenantRoles);
		jdbcTemplate.batchUpdate(userQueryBuilder.USER_TENANT_INSERT_QUERY, new BatchPreparedStatementSetter() {

			@Override
			public void setValues(PreparedStatement ps, int index) throws SQLException {
				TenantRole tenantRole = tenantRoles.get(index);
				ps.setLong(1, tenantRole.getId());
				ps.setLong(2, tenantRole.getUserId());
				ps.setString(3, tenantRole.getTenantId());
				ps.setString(4, requestInfo.getUserInfo().getId().toString());
				ps.setLong(5, new Date().getTime());
				ps.setString(6, requestInfo.getUserInfo().getId().toString());
				ps.setLong(7, new Date().getTime());
			}

			@Override
			public int getBatchSize() {
				return tenantRoles.size();
			}
		});

	}

	@Transactional
	public void updateUser(UserReq userReq){
		
		RequestInfo requestInfo = userReq.getRequestInfo();
		List<User> users = userReq.getUsers();
		List<Address> updateAddressList = new ArrayList<>();
		List<Address> insertAddressList = new ArrayList<>();
		List<TenantRole> updateTenantRoleList = new ArrayList<>();
		List<TenantRole> insertTenantRoleList = new ArrayList<>();
		for(User user : users){
			List<Address> addresses = user.getUserDetails().getAddresses();
			System.out.println("updateUser addresses:"+addresses);
			if(addresses != null && addresses.size() > 0){
				Map<DbAction, List<Address>> addMap = addresses.stream().collect(
						Collectors.groupingBy(Address::getDbAction, Collectors.toList()));
				System.out.println("updateUser addMap:"+addMap);
				if(addMap.get(DbAction.INSERT) != null)
				insertAddressList.addAll(addMap.get(DbAction.INSERT));
				
				if(addMap.get(DbAction.UPDATE) != null)
				updateAddressList.addAll(addMap.get(DbAction.UPDATE));
			}
			List<TenantRole> tenantRoles = user.getAdditionalroles();
			
			if(tenantRoles != null && tenantRoles.size() > 0){
				Map<DbAction, List<TenantRole>> tenantRolesMap = tenantRoles.stream().collect(
					Collectors.groupingBy(TenantRole::getDbAction, Collectors.toList()));
				System.out.println("updateUser tenantRolesMap:"+tenantRolesMap);
				
				if(tenantRolesMap.get(DbAction.INSERT) != null)
				insertTenantRoleList.addAll(tenantRolesMap.get(DbAction.INSERT));
				
				if(tenantRolesMap.get(DbAction.UPDATE) != null)
				updateTenantRoleList.addAll(tenantRolesMap.get(DbAction.UPDATE));
			}
		}
		jdbcTemplate.batchUpdate(userQueryBuilder.USER_UPDATE_QUERY, new BatchPreparedStatementSetter() {

			@Override
			public void setValues(PreparedStatement ps, int index) throws SQLException {
				User user = users.get(index);
				UserDetails userDetails = user.getUserDetails();
				
				ps.setString(1, null);// title
				ps.setString(2, user.getSalutation());
				ps.setDate(3, new java.sql.Date(userDetails.getDob().getTime()));
				ps.setString(4, user.getLocale());
				ps.setString(5, user.getUsername());
				ps.setString(6, user.getPassword());// password
				ps.setDate(7, new java.sql.Date(user.getPwdExpiryDate()));
				ps.setString(8, user.getMobile());
				ps.setString(9, userDetails.getAltContactNumber());
				ps.setString(10, user.getEmail());
			//	ps.setDate(11, new java.sql.Date(new Date().getTime()));
				ps.setDate(11, new java.sql.Date(new Date().getTime()));
			//	ps.setLong(13, requestInfo.getUserInfo().getId());
				ps.setLong(12, requestInfo.getUserInfo().getId());
				ps.setBoolean(13, true);
				ps.setString(14, user.getName());
				ps.setInt(15, Gender.valueOf(user.getGender().toString()).ordinal());
				ps.setString(16, userDetails.getPan());
				ps.setString(17, user.getAadhaarNumber());
				ps.setString(18, user.getType().toString());
				ps.setInt(19, 1);// version
				ps.setString(20, null);// guardian
				ps.setString(21, null);// guardianrelation
				ps.setString(22, userDetails.getSignature());
				ps.setBoolean(23, user.getAccountLocked());
				ps.setString(24, null);// bloodgroup
				ps.setString(25, userDetails.getPhoto());
				ps.setString(26, userDetails.getIdentificationMark());
				ps.setLong(27, user.getId());
				ps.setString(28, user.getTenantId());
			}

			@Override
			public int getBatchSize() {
				return users.size();
			}
		});
	
		saveUserAddress(insertAddressList, requestInfo);
		updateUserAddress(updateAddressList, requestInfo);
		saveUserTenant(insertTenantRoleList, requestInfo);
		updateUserTenant(updateTenantRoleList, requestInfo);
	}
	
	public void updateUserAddress(List<Address> addresses, RequestInfo requestInfo) {

		jdbcTemplate.batchUpdate(userQueryBuilder.USER_ADDRESS_UPDATE_QUERY, new BatchPreparedStatementSetter() {
    
			@Override
			public void setValues(PreparedStatement ps, int index) throws SQLException {
				Address address = addresses.get(index);
					
				ps.setInt(1, 1);
				ps.setDate(2, new java.sql.Date(new Date().getTime()));
				ps.setLong(3, requestInfo.getUserInfo().getId());
				ps.setString(4, address.getAddressType().toString());
				ps.setString(5, address.getAddressLine1());
				ps.setString(6, address.getCity());
				ps.setString(7, address.getPincode());
				ps.setLong(8, address.getId());
				ps.setString(9, address.getTenantId());
				
			}

			@Override
			public int getBatchSize() {
				return addresses.size();
			}
		});
	}
	
	/*public void updateUserRole(List<Role> roles, RequestInfo requestInfo) {

		jdbcTemplate.batchUpdate(userQueryBuilder.USER_ADDRESS_INSERT_QUERY, new BatchPreparedStatementSetter() {

			@Override
			public void setValues(PreparedStatement ps, int index) throws SQLException {
				Role role = roles.get(index);
				ps.setLong(1, role.getId());
				ps.setString(2, role.getTenantId());
				ps.setLong(3, role.getUserId());
				ps.setString(4, role.getTenantId());
				ps.setDate(5, new java.sql.Date(new Date().getTime()));
			}

			@Override
			public int getBatchSize() {
				return roles.size();
			}
		});

	}*/
	
	public void updateUserTenant(List<TenantRole> tenantRoles, RequestInfo requestInfo) {

		jdbcTemplate.batchUpdate(userQueryBuilder.USER_TENANT_UPDATE_QUERY, new BatchPreparedStatementSetter() {
			 

			@Override
			public void setValues(PreparedStatement ps, int index) throws SQLException {
				TenantRole tenantRole = tenantRoles.get(index);
				ps.setString(1, tenantRole.getTenantId());
				ps.setString(2, requestInfo.getUserInfo().getId().toString());
				ps.setLong(3, new Date().getTime());
				ps.setLong(4, tenantRole.getId());
			}

			@Override
			public int getBatchSize() {
				return tenantRoles.size();
			}
		});

	}
}
