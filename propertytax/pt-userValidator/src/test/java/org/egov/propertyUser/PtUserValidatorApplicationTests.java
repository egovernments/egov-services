//package org.egov.propertyUser;
//
//import static org.junit.Assert.*;
//
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.junit4.SpringRunner;
//
//@RunWith(SpringRunner.class)
//@SpringBootTest
//public class PtUserValidatorApplicationTests {
//
//	@Test
//	public void contextLoads() {
//
//		assertEquals("10 x 0 must be 0", 0, 0);
//		assertEquals("0 x 10 must be 0", 0, 0);
//		assertEquals("0 x 0 must be 0", 0, 0);
//
//	}
//
//}

package org.egov.propertyUser;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;


@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = { PtUserValidatorApplication.class })

public class PtUserValidatorApplicationTests {
/*
	@Autowired
	Environment environment;
	
	@Autowired
	Producer producer;*/
	
	
	@Test
	public void contextLoads() {
	}


/*	@Test
	public void testCreatingProperty() throws Exception {
		try {
			PropertyRequest propertyRequest = new PropertyRequest();
			List<Property> properties = new ArrayList<Property>();
			RequestInfo requestInfo = new RequestInfo();
			requestInfo.setApiId("1");
			requestInfo.setVer("1.0");
			requestInfo.setAuthToken("08db73a8-945e-4164-94e3-63ccef7856d4");

			Property property = new Property();
			property.setUpicNo("test123");
			property.setCreationReason("testCreation");
			property.setAssessmentDate("18/05/2017");
			property.setChannel("eseva");
			property.setOccupancyDate("12/03/2018");
			property.setTenantId("default");
			PropertyDetail propertyDetail = new PropertyDetail();
			propertyDetail.setTenantId("default");
			propertyDetail.setChannel("eseva");
			Floor floor = new Floor();
			floor.setFloorNo("1");
			floor.setBuiltupArea(1234.34);
			floor.setStructure("flat");
			floor.setUsage("house");
			floor.setOccupancy("house");
			List<Floor> floors = new ArrayList<>();
			floors.add(floor);
			propertyDetail.setFloors(floors);
			property.setPropertydetails(propertyDetail);
			List<User> users = new ArrayList<User>();
			User user = new User();
			user.setTenantId("default");
			user.setUserName("TestForUserNaren");
			user.setSalutation("Mr");
			user.setName("TestNaren");
			user.setGender("MALE");
			user.setMobileNumber("9999999999");
			user.setEmailId("pranav@egovernments.org");
			user.setActive(true);
			user.setLocale("en_US");
			user.setType("CITIZEN");
			user.setAccountLocked(false);
			Role role=new Role();
			role.setCode("CITIZEN");
			List<Role> roles=new ArrayList<Role>();
			roles.add(role);
			user.setRoles(roles);
			users.add(user);
			property.setOwners(users);
			properties.add(property);
			propertyRequest.setProperties(properties);
			propertyRequest.setRequestInfo(requestInfo);
			producer.send(environment.getProperty("validate.user"), propertyRequest);
		} catch (Exception e) {
		}
	}*/

}