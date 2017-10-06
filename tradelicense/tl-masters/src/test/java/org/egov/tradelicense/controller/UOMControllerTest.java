package org.egov.tradelicense.controller;

import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import org.egov.tl.commons.web.contract.AuditDetails;
import org.egov.tl.commons.web.contract.RequestInfo;
import org.egov.tl.commons.web.contract.ResponseInfo;
import org.egov.tl.commons.web.contract.UOM;
import org.egov.tl.commons.web.requests.UOMRequest;
import org.egov.tl.commons.web.response.UOMResponse;
import org.egov.tradelicense.TradeLicenseApplication;
import org.egov.tradelicense.config.PropertiesManager;
import org.egov.tradelicense.domain.services.UOMService;
import org.egov.tradelicense.web.controller.UOMController;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(SpringRunner.class)
@WebMvcTest(UOMController.class)
@ContextConfiguration(classes = { TradeLicenseApplication.class })
public class UOMControllerTest {

	@MockBean
	private UOMService uomService;

	@MockBean
	private PropertiesManager propertiesManager;

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	KafkaTemplate kafkaTemplate;

	/**
	 * Description : Test method for createUom() method
	 */
	@Test
	public void testCreateUom() throws Exception {

		List<UOM> uoms = new ArrayList<>();
		UOM uom = new UOM();
		uom.setTenantId("default");

		AuditDetails auditDetails = new AuditDetails();
		uom.setAuditDetails(auditDetails);

		UOMResponse uomResponse = new UOMResponse();
		uoms.add(uom);

		uomResponse.setResponseInfo(new ResponseInfo());
		uomResponse.setUoms(uoms);

		try {

			when(uomService.createUomMaster(any(UOMRequest.class))).thenReturn(uomResponse);

			mockMvc.perform(post("/uom/v1/_create").param("tenantId", "default").contentType(MediaType.APPLICATION_JSON)
					.content(getFileContents("uomCreateRequest.json"))).andExpect(status().isOk())
					.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
					.andExpect(content().json(getFileContents("uomCreateResponse.json")));

		} catch (Exception e) {

			assertTrue(Boolean.FALSE);
		}

		assertTrue(Boolean.TRUE);

	}

	/**
	 * Description : Test method for updateUom() method
	 */
	@Test
	public void testUpdateUom() throws Exception {

		UOMResponse uomResponse = new UOMResponse();
		List<UOM> uoms = new ArrayList<>();
		UOM uom = new UOM();
		uom.setTenantId("default");

		AuditDetails auditDetails = new AuditDetails();
		uom.setAuditDetails(auditDetails);

		uoms.add(uom);

		uomResponse.setResponseInfo(new ResponseInfo());
		uomResponse.setUoms(uoms);

		try {

			when(uomService.updateUomMaster(any(UOMRequest.class))).thenReturn(uomResponse);
			mockMvc.perform(post("/uom/v1/_update").param("tenantId", "default").contentType(MediaType.APPLICATION_JSON)
					.content(getFileContents("uomUpdateRequest.json"))).andExpect(status().isOk())
					.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
					.andExpect(content().json(getFileContents("uomUpdateResponse.json")));

		} catch (Exception e) {

			assertTrue(Boolean.FALSE);
			e.printStackTrace();
		}

		assertTrue(Boolean.TRUE);

	}

	/**
	 * Description : Test method for searchUom() method
	 */
	@Test
	public void testSearchUom() throws Exception {

		UOMResponse uomResponse = new UOMResponse();
		List<UOM> uoms = new ArrayList<>();
		UOM uom = new UOM();
		uom.setTenantId("default");

		AuditDetails auditDetails = new AuditDetails();
		uom.setAuditDetails(auditDetails);

		uoms.add(uom);

		uomResponse.setResponseInfo(new ResponseInfo());
		uomResponse.setUoms(uoms);

		try {

			when(uomService.getUomMaster(any(RequestInfo.class), any(String.class), any(Integer[].class),
					any(String.class), any(String[].class), any(String.class), any(Integer.class), any(Integer.class)))
							.thenReturn(uomResponse);

			mockMvc.perform(post("/uom/v1/_search").param("tenantId", "default").contentType(MediaType.APPLICATION_JSON)
					.content(getFileContents("uomSearchRequest.json"))).andExpect(status().isOk())
					.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
					.andExpect(content().json(getFileContents("uomSearchResponse.json")));

		} catch (Exception e) {

			assertTrue(Boolean.FALSE);
			e.printStackTrace();
		}

		assertTrue(Boolean.TRUE);

	}

	private String getFileContents(String fileName) throws IOException {
		ClassLoader classLoader = getClass().getClassLoader();
		return new String(Files.readAllBytes(new File(classLoader.getResource(fileName).getFile()).toPath()));
	}
}