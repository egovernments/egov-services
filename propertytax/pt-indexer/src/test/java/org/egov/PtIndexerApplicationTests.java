package org.egov;

import java.util.ArrayList;
import java.util.List;

import org.egov.models.Floor;
import org.egov.models.Property;
import org.egov.models.PropertyDetail;
import org.egov.propertyIndexer.PtIndexerApplication;
import org.egov.propertyIndexer.indexerConsumer.Producer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes={PtIndexerApplication.class})

public class PtIndexerApplicationTests {

@Autowired
Environment environment;

@Autowired
private Producer producer;

	@Test
	public void testCreatingProperty() {
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
		floor.setId("floor123");
		floor.setFloorNo("1");
		floor.setBuiltupArea(1234.34);
		floor.setStructure("flat");
		floor.setUsage("house");
		floor.setOccupancy("house");
		List<Floor> floors=new ArrayList<>();
		floors.add(floor);
		propertyDetail.setFloors(floors);
		property.setPropertydetails(propertyDetail);
		producer.send(environment.getProperty("propertyIndexer.create"),property);
		}catch(Exception e){
			
		}
	}


}
