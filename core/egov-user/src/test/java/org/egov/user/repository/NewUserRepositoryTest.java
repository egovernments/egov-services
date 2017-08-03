package org.egov.user.repository;

import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.egov.user.model.Address;
import org.egov.user.model.User;
import org.egov.user.model.UserSearchCriteria;
import org.egov.user.model.enums.Gender;
import org.egov.user.model.enums.Type;
import org.egov.user.repository.mapper.UserRowMapper;
import org.egov.user.repository.querybuilder.UserQueryBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
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
