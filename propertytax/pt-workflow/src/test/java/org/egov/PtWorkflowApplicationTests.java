package org.egov;

import org.egov.propertyWorkflow.PtWorkflowApplication;
import org.egov.propertyWorkflow.consumer.WorkflowProducer;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes={PtWorkflowApplication.class})
public class PtWorkflowApplicationTests {

	@Autowired
	private WorkflowProducer producer;

	@Autowired
	Environment environment;

	/*//@Test
	public void testCreatingProperty() throws Exception {
		try {
			PropertyRequest propertyRequest = new PropertyRequest();
			List<Property> properties = new ArrayList<Property>();
			RequestInfo requestInfo = new RequestInfo();
			requestInfo.setApiId("1");
			requestInfo.setVer("1.0");
			requestInfo.setAuthToken("eb345c56-e9c3-4a2a-b520-579f0cd8101c");

			Property property = new Property();
			property.setUpicNo("test123");
			property.setCreationReason("testCreation");
			property.setAssessmentDate("18/05/2017");
			property.setChannel("eseva");
			property.setOccupancyDate("12/03/2018");
			property.setTenantId("default");
			PropertyDetail propertyDetail = new PropertyDetail();
			WorkflowDetails workflowDetails=new WorkflowDetails();
			workflowDetails.setAssignee(1);
			propertyDetail.setWorkFlowDetails(workflowDetails);
			propertyDetail.setChannel("eseva");
			Floor floor = new Floor();
			floor.setId(1);
			floor.setFloorNo("1");
			floor.setBuiltupArea(1234.34);
			floor.setStructure("flat");
			floor.setUsage("house");
			floor.setOccupancy("house");
			List<Floor> floors = new ArrayList<>();
			floors.add(floor);
			propertyDetail.setFloors(floors);
			propertyDetail.setTenantId("default");
			property.setPropertydetails(propertyDetail);
			List<User> users = new ArrayList<User>();
			User user = new User();
			user.setTenantId("User");
			user.setUserName("Anilkumar S");
			// user.getRoles().get(0).setName("testing");
			users.add(user);
			property.setOwners(users);
			properties.add(property);
			propertyRequest.setProperties(properties);
			propertyRequest.setRequestInfo(requestInfo);
			producer.send(environment.getProperty("property.start.workflow"),propertyRequest);
			//producer.send("userupdate", propertyRequest);
		} catch (Exception e) {

		}

	}*/
}