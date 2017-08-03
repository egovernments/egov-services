package org.egov.user.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.egov.tracer.kafka.LogAwareKafkaTemplate;
import org.egov.user.model.Address;
import org.egov.user.model.User;
import org.egov.user.model.UserDetails;
import org.egov.user.model.UserReq;
import org.egov.user.model.UserRes;
import org.egov.user.model.UserSearchCriteria;
import org.egov.user.model.enums.Gender;
import org.egov.user.model.enums.Type;
import org.egov.user.repository.NewUserRepository;
import org.egov.user.utils.UserConfigurationUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.security.crypto.password.PasswordEncoder;

@RunWith(MockitoJUnitRunner.class)
public class NewUserServiceTest {

	@InjectMocks
	private NewUserService newUserService;
	
	@Mock
	private NewUserRepository userRepository;
	
	@Mock
	private UserIdPopuater userIdPopuater;
	
	@Mock
	private PasswordEncoder passwordEncoder;
	
	@Mock
	private UserConfigurationUtil userConfigurationUtil; 
	
	@Mock
	private LogAwareKafkaTemplate<String, Object> kafkaTemplate;
	
	@Test
	public void test_Should_Search_User() {
		
		List<User> users=new ArrayList<User>();
		users.add(getUser());
		UserRes userRes =new UserRes();
		userRes.setUsers(users);
		
		when(userRepository.search(Matchers.any(UserSearchCriteria.class)))
		.thenReturn(users);
		
		UserSearchCriteria userCriteria=UserSearchCriteria.builder().tenantId("default").build();
		assertEquals(userRes, newUserService.search(userCriteria));
	}
	
	@Test
	public void test_Should_Create_User() throws Exception{
		List<User> users = new ArrayList<User>();
		users.add(getUser());
		UserRes userRes = new UserRes();
		userRes.setUsers(users);

		UserReq userReq = new UserReq();
		userReq.setUsers(users);

		doNothing().when(userRepository).saveUser(any(UserReq.class));

		when(passwordEncoder.encode(Matchers.any(String.class))).thenReturn("demo");

		assertTrue(userRes.equals(newUserService.create(userReq)));
	}
	
	@Test
	public void test_Should_Update_User(){
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

		UserReq userReq = new UserReq();
		userReq.setUsers(users);

		when(userRepository.search(Matchers.any(UserSearchCriteria.class))).thenReturn(users);

		when(passwordEncoder.encode(Matchers.any(String.class))).thenReturn("demo");

		doNothing().when(userIdPopuater).populateAddressAndUserTenantId(any(List.class), any(List.class));

		assertTrue(userRes.equals(newUserService.updateAsync(userReq)));
	}
	private Address getAddress(){
		Address add=new Address();
		add.setTenantId("default");
		add.setId(12l);
	return add;	
	}
	
	private User getUser(){

		User user = new User();
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
