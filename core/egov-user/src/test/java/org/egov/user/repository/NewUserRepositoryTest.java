package org.egov.user.repository;

import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.egov.user.model.Address;
import org.egov.user.model.TenantRole;
import org.egov.user.model.User;
import org.egov.user.model.UserDetails;
import org.egov.user.model.UserReq;
import org.egov.user.model.UserRes;
import org.egov.user.model.UserSearchCriteria;
import org.egov.user.model.enums.DbAction;
import org.egov.user.model.enums.Gender;
import org.egov.user.model.enums.Type;
import org.egov.user.repository.mapper.UserRowMapper;
import org.egov.user.repository.querybuilder.UserQueryBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
public class NewUserRepositoryTest {

	@Mock
	private UserQueryBuilder userQueryBuilder;

	@Mock
	private JdbcTemplate jdbcTemplate;
	
	@InjectMocks
	private NewUserRepository newUserRepository;
	
	@Test
	public void test_Should_Search() {
		List<User> users = new ArrayList<User>();
		users.add(getUser());
		String query = "";
		when(userQueryBuilder.getQuery(any(UserSearchCriteria.class), any(List.class))).thenReturn(query);
		when(jdbcTemplate.query(any(String.class), any(Object[].class), any(UserRowMapper.class))).thenReturn(users);

		assertTrue(users.equals(newUserRepository.search(new UserSearchCriteria())));
	}

	@Test
	public void testSaveUser(){
		List<User> users = new ArrayList<User>();
		List<Address> address = new ArrayList<>();
		Address add = getAddress();
		address.add(add);
		UserDetails uDetails = new UserDetails();
		uDetails.setAddresses(address);

		users.add(getUser());
		users.get(0).setUserDetails(uDetails);
		UserRes userRes = new UserRes();
		userRes.setUsers(users);
		
		int[] i={1};
		UserReq userReq=new UserReq();
		userReq.setUsers(users);
		
		when(jdbcTemplate.batchUpdate(any(String.class),any(BatchPreparedStatementSetter.class))).thenReturn(i);
		assertTrue("1".equals(users.size()+""));
		
		newUserRepository.saveUser(userReq);
	}
	
	@Test
	public void testUpdateUser(){
		List<User> users = new ArrayList<User>();
		List<Address> address = new ArrayList<>();
		Address add = getAddress();
		address.add(add);
		List<TenantRole> tenantRoles=new ArrayList<>();
		tenantRoles.add(getTenantRole());
		
		UserDetails uDetails = new UserDetails();
		uDetails.setAddresses(address);
		int[] i={1};
		users.add(getUser());
		users.get(0).setUserDetails(uDetails);
		users.get(0).setAdditionalroles(tenantRoles);
		
		UserReq userReq=new UserReq();
		userReq.setUsers(users);
		
		when(jdbcTemplate.batchUpdate(any(String.class),any(BatchPreparedStatementSetter.class))).thenReturn(i);
		newUserRepository.updateUser(userReq);
		
	}
	private TenantRole getTenantRole(){
		TenantRole tenantRole=new TenantRole();
		tenantRole.setTenantId("default");
		tenantRole.setUserId(12l);
		tenantRole.setDbAction(DbAction.valueOf("UPDATE"));
		return tenantRole;
	}
	
	private Address getAddress(){
		Address add=new Address();
		add.setTenantId("default");
		add.setId(12l);
		add.setDbAction(DbAction.valueOf("UPDATE"));
	return add;	
	}
	private User getUser(){
		
		User user=new User();
		user.setId(12l);
		user.setTenantId("default");
		user.setUsername("manas");
		user.setMobile("9865433212");
		user.setPassword("demo");
		user.setEmail("abc@xyz.com");
		user.setSalutation("MR.");
		user.setName("manas");
		user.setGender(Gender.fromValue("MALE"));
		user.setAadhaarNumber("123456788765");
		user.setActive(true);
		user.setPwdExpiryDate(4070908800000l);
		user.setLocale("en_IN");
		user.setType(Type.valueOf("EMPLOYEE"));
		user.setAccountLocked(false);
		
		return user;
	}
}
