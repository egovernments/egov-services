package org.egov.mr.web.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.egov.common.contract.response.ResponseInfo;
import org.egov.mr.TestConfiguration;
import org.egov.mr.model.AuditDetails;
import org.egov.mr.model.Location;
import org.egov.mr.model.RegistrationUnit;
import org.egov.mr.service.RegistrationUnitService;
import org.egov.mr.utils.FileUtils;
import org.egov.mr.validator.RegistrationUnitValidator;
import org.egov.mr.web.contract.RegistrationUnitSearchCriteria;
import org.egov.mr.web.contract.RegnUnitRequest;
import org.egov.mr.web.contract.RegnUnitResponse;
import org.egov.mr.web.errorhandler.ErrorHandler;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(SpringRunner.class)
@WebMvcTest(RegistrationUnitController.class)
@Import(TestConfiguration.class)
public class RegistrationUnitControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private RegistrationUnitService registrationUnitService;

	@MockBean
	private RegnUnitResponse regnUnitResponse;

	@MockBean
	private ErrorHandler ErrorHandler;

	@MockBean
	private ResponseInfo responseInfo;

	@MockBean
	private RegistrationUnitValidator registrationUnitValidator;

	@InjectMocks
	private RegistrationUnitController registrationUnitController;

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testCreate() throws IOException, Exception {

		List<RegistrationUnit> expectedRegistrationUnit = new ArrayList<>();
		expectedRegistrationUnit.add(getExpectedRegistrationUnitForCreateAndUpdate().get(0));

		when(registrationUnitService.createAsync(Matchers.any(RegnUnitRequest.class)))
				.thenReturn(expectedRegistrationUnit);
		try {
			mockMvc.perform(post("/regnUnits/_create").content(getFileContents("ReqnUnitRequest.json"))
					.contentType(MediaType.APPLICATION_JSON_UTF8)).andExpect(status().isOk())
					.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
					.andExpect(content().json(getFileContents("ReqnUnitResponse.json")));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testForSearch() throws IOException, Exception {
		List<RegistrationUnit> expectedRegistrationUnit = new ArrayList<>();
		expectedRegistrationUnit.add(getRegnUnitsFromDB().get(0));
		expectedRegistrationUnit.add(getRegnUnitsFromDB().get(1));
		expectedRegistrationUnit.add(getRegnUnitsFromDB().get(2));

		when(registrationUnitService.search(Matchers.any(RegistrationUnitSearchCriteria.class)))
				.thenReturn(expectedRegistrationUnit);
		try {
			mockMvc.perform(post("/regnUnits/_search").content(getFileContents("RequestInfo.json"))
					.contentType(MediaType.APPLICATION_JSON_UTF8)).andExpect(status().isOk())
					// .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
					.andExpect(content().json(getFileContents("RegistrationUnits.json")));
		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}

	@Test
	public void testUpdate() throws IOException, Exception {

		System.out.println(getFileContents("ReqnUnitRequest.json"));
		System.out.println(getFileContents("ReqnUnitResponse.json"));
		List<RegistrationUnit> expectedRegistrationUnit = new ArrayList<>();
		expectedRegistrationUnit.add(getExpectedRegistrationUnitForCreateAndUpdate().get(0));

		when(registrationUnitService.updateAsync(Matchers.any(RegnUnitRequest.class)))
				.thenReturn(expectedRegistrationUnit);
		System.out.println(expectedRegistrationUnit.get(0));
		try {
			mockMvc.perform(post("/regnUnits/_update").content(getFileContents("ReqnUnitRequest.json"))
					.contentType(MediaType.APPLICATION_JSON_UTF8)).andExpect(status().isOk())
					.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
					.andExpect(content().json(getFileContents("ReqnUnitResponse.json")));
		} catch (Exception e) {
			System.out.println(e.getMessage());
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// Virtual DB
	private List<RegistrationUnit> getRegnUnitsFromDB() {
		List<RegistrationUnit> regnunitsList = new ArrayList<>();
		AuditDetails auditDetails = AuditDetails.builder().createdBy("123").lastModifiedBy("159").createdTime(156L)
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
		return regnunitsList;
	}

	private List<RegistrationUnit> getExpectedRegistrationUnitForCreateAndUpdate() {
		// expected RegistrationUnit from the DB
		List<RegistrationUnit> expectedRegistrationUnitList = new ArrayList<>();
		AuditDetails auditDetails = AuditDetails.builder().createdBy("123").lastModifiedBy("159").createdTime(156L)
				.lastModifiedTime(147L).build();

		Location location = Location.builder().locality(60L).zone(147896325L).revenueWard(150L).block(14788L)
				.street(159632478L).electionWard(123456789L).doorNo("132").pinCode(560103).build();

		expectedRegistrationUnitList
				.add(RegistrationUnit.builder().id(33L).name("hyderabad").address(location).isActive(true)
						.tenantId("AP.HYDERABAD").isMainRegistrationUnit(true).auditDetails(auditDetails).build());
		return expectedRegistrationUnitList;
	}

	private String getFileContents(String filePath) throws IOException {
		return new FileUtils().getFileContents("org/egov/mr/web/controller/" + filePath);
	}

}
