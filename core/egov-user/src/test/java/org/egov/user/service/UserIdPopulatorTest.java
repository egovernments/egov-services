package org.egov.user.service;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.egov.user.model.Address;
import org.egov.user.model.TenantRole;
import org.egov.user.model.User;
import org.egov.user.model.UserDetails;
import org.egov.user.model.UserReq;
import org.egov.user.model.UserRes;
import org.egov.user.model.enums.Gender;
import org.egov.user.model.enums.Type;
import org.egov.user.utils.SequenceGenService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

/*@TestPropertySource(properties = {
	    "egov.seq.name.user=seq_eg_user",
	   "egov.seq.name.address=seq_eg_user_address",
	   "egov.seq.name.usertenant=seq_eg_usertenantrole",
	})*/
@RunWith(MockitoJUnitRunner.class)
public class UserIdPopulatorTest {

	@InjectMocks
	private UserIdPopuater userIdPopuater;
	
	@Mock
	private SequenceGenService sequenceGenService;
	
	/*@Before
	public void setUp() throws Exception {
		
		ReflectionTestUtils.setField(UserIdPopuater.class, "userSeq", "seq_eg_user");
	}*/

	@Test
	public void testpopulateId() {
		List<User> users = new ArrayList<User>();
		List<Address> address = new ArrayList<>();
		Address add = getAddress();
		address.add(add);
		UserDetails uDetails = new UserDetails();
		uDetails.setAddresses(address);

		users.add(getUser());
		users.get(0).setUserDetails(uDetails);
		List<Long> ids=new ArrayList<>();
		ids.add(12l);
		UserReq userReq=new UserReq();
		userReq.setUsers(users);
		
		when(sequenceGenService.getIds(Matchers.any(int.class), Matchers.any(String.class)))
		.thenReturn(ids);
		
		assertTrue(ids.equals(sequenceGenService.getIds(1,"seq_eg_user")));
		
		userIdPopuater.populateId(userReq);
	}
	
	@Test
	public void testpopulateAddressAndUserTenantId(){
		List<Address> address = new ArrayList<>();
		Address add = getAddress();
		address.add(add);
		List<TenantRole> tenantRoles=new ArrayList<>();
		tenantRoles.add(getTenantRole());
		List<Long> ids=new ArrayList<>();
		ids.add(12l);
		
		when(sequenceGenService.getIds(Matchers.any(int.class), Matchers.any(String.class)))
		.thenReturn(ids);
		userIdPopuater.populateAddressAndUserTenantId(address, tenantRoles);
	}
	private TenantRole getTenantRole(){
		TenantRole tenantRole=new TenantRole();
		tenantRole.setTenantId("default");
		tenantRole.setUserId(12l);
		return tenantRole;
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
