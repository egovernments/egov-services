package org.egov.mr.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.egov.mr.model.AuditDetails;
import org.egov.mr.model.Location;
import org.egov.mr.model.RegistrationUnit;
import org.egov.mr.repository.RegistrationUnitRepository;
import org.egov.mr.utils.FileUtils;
import org.egov.mr.web.contract.RegistrationUnitSearchCriteria;
import org.egov.mr.web.contract.RegnUnitResponse;
import org.egov.tracer.kafka.LogAwareKafkaTemplate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(MockitoJUnitRunner.class)
public class RegistrationUnitServiceTest {

	@Mock
	private RegistrationUnitRepository registrationUnitRepository;

	@Mock
	private LogAwareKafkaTemplate<String, Object> kafkaTemplate;

	@InjectMocks
	private RegistrationUnitService registrationUnitService;

	@Test
	public void testForSearch() {
		RegnUnitResponse regnUnitResponse = null;
		// Accessing the Response
		try {
			regnUnitResponse = getRegistrationUnitResponse("org/egov/mr/service/registrationUnitsListForSearch.json");
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
		// Setting Criteria(tenantId)
		RegistrationUnitSearchCriteria registrationUnitSearchCriteria = RegistrationUnitSearchCriteria.builder()
				.tenantId("KA.BANGALORE").build();

		when(registrationUnitRepository.search(any(RegistrationUnitSearchCriteria.class)))
				.thenReturn(getRegnUnitsFromDB());

		List<RegistrationUnit> actualRegistrationUnitsList = registrationUnitService
				.search(registrationUnitSearchCriteria);

		List<RegistrationUnit> expectedRegistrationUnits = new ArrayList();
		expectedRegistrationUnits.add(regnUnitResponse.getRegnUnits().get(0));
		expectedRegistrationUnits.add(regnUnitResponse.getRegnUnits().get(1));
		expectedRegistrationUnits.add(regnUnitResponse.getRegnUnits().get(2));

		assertTrue(registrationUnitSearchCriteria.getTenantId().equals("KA.BANGALORE"));
		assertEquals(expectedRegistrationUnits.get(0), actualRegistrationUnitsList.get(0));
		assertEquals(expectedRegistrationUnits.get(1), actualRegistrationUnitsList.get(1));
		assertEquals(expectedRegistrationUnits.get(2), actualRegistrationUnitsList.get(2));
	}

	// Accessing the RegistrationUnitResponse Data from the JSON
	private RegnUnitResponse getRegistrationUnitResponse(String filePath) throws IOException {
		String regnUnitJson = new FileUtils().getFileContents(filePath);
		return new ObjectMapper().readValue(regnUnitJson, RegnUnitResponse.class);
	}

	// Virtual DB
	private List<RegistrationUnit> getRegnUnitsFromDB() {
		List<RegistrationUnit> regnunitsList = new ArrayList();
		AuditDetails auditDetails = AuditDetails.builder().createdBy("123").lastModifiedBy("156").createdTime(159L)
				.lastModifiedTime(147L).build();
		// Data For Search
		regnunitsList.add(RegistrationUnit.builder().id(26L).name("Koramangala")
				.address(Location.builder().locality(60L).zone(147896325L).revenueWard(150L).block(14788L)
						.street(159632478L).electionWard(123456789L).doorNo("132").pinCode(560103).build())
				.isMainRegistrationUnit(true).isActive(true).tenantId("KA.BANGALORE").auditDetails(auditDetails)
				.build());
		regnunitsList.add(RegistrationUnit.builder().id(27L).name("JP Nagar")
				.address(Location.builder().locality(60L).zone(147896325L).revenueWard(150L).block(14788L)
						.street(159632478L).electionWard(123456789L).doorNo("132").pinCode(560103).build())
				.isMainRegistrationUnit(true).isActive(true).tenantId("KA.BANGALORE").auditDetails(auditDetails)
				.build());
		regnunitsList.add(RegistrationUnit.builder().id(28L).name("Jaya Nagar")
				.address(Location.builder().locality(60L).zone(147896325L).revenueWard(150L).block(14788L)
						.street(159632478L).electionWard(123456789L).doorNo("132").pinCode(560103).build())
				.isMainRegistrationUnit(true).isActive(true).tenantId("KA.BANGALORE").auditDetails(auditDetails)
				.build());
		// Data For CreateAsync
		// regnunitsList.add(RegistrationUnit.builder().id(29L).name("hyderabad")
		// .address(Location.builder().locality(60L).zone(147896325L).revenueWard(150L).block(14788L)
		// .street(159632478L).electionWard(123456789L).doorNo("132").pinCode(560103L).build())
		// .isActive(true).tenantId("AP.HYDERABAD").createdBy(123L).createdDate(159L).lastModifiedBy(156L)
		// .lastModifiedDate(147L).build());
		// Data For UpdateAsync
		return regnunitsList;
	}

}
