package org.egov;

import org.egov.propertyIndexer.PtIndexerApplication;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

/*
 * This class has test cases for indexer module of property
 */

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes={PtIndexerApplication.class})
public class PtIndexerApplicationTests {

	/*@Autowired
	Environment environment;

	@Autowired
	private Producer producer;
*/

	/*
	 * test case for inserting data into indexer in elastic search
	 */
	//@Test
	/*public void testCreatingProperty() {
		try{
			Property property=new Property();
			property.setUpicNo("test123");
			property.setCreationReason("testCreation");
			property.setAssessmentDate("18/05/2017");
			property.setChannel("eseva");
			property.setOccupancyDate("12/03/2018");
			property.setTenantId("default");
			PropertyDetail propertyDetail=new PropertyDetail();
			propertyDetail.setChannel("eseva");
			Floor floor=new Floor();
			//floor.setId(1);
			floor.setFloorNo("1");
			floor.setBuiltupArea(1234.34);
			floor.setStructure("flat");
			floor.setUsage("house");
			floor.setOccupancy("house");
			List<Floor> floors=new ArrayList<>();
			floors.add(floor);
			propertyDetail.setFloors(floors);
			property.setPropertydetails(propertyDetail);
			PropertyRequest propertyRequest=new PropertyRequest();
			List<Property> properties=new ArrayList<Property>();
			properties.add(property);
			producer.send(environment.getProperty("propertyIndexer.create"),propertyRequest);
		}catch(Exception e){

		}
	}*/


}
