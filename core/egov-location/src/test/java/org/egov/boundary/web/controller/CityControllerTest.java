package org.egov.boundary.web.controller;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;

import org.apache.commons.io.IOUtils;
import org.egov.boundary.domain.model.RequestContext;
import org.egov.boundary.domain.service.CityService;
import org.egov.boundary.web.contract.City;
import org.egov.boundary.web.contract.CityRequest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(SpringRunner.class)
@WebMvcTest(CityController.class)
public class CityControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private CityService cityService;

	@Test
	public void test_should_fetch_city_for_given_cityrequest() throws Exception {

		final org.egov.boundary.persistence.entity.City expectedCity = org.egov.boundary.persistence.entity.City
				.builder().code("KC").name("Kurnool").domainURL("Localhost").grade("Grade A")
				.districtName("Kurnool District").districtCode("KC01").regionName("Kurnool Region").build();

		when(cityService.getCityByCityReq(any(CityRequest.class))).thenReturn(expectedCity);
		mockMvc.perform(post("/city/getCitybyCityRequest?city.id=1").header("X-CORRELATION-ID", "someId")
				.content(getFileContents("cityRequest.json")).contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(content().json(getFileContents("cityResponse.json")));
		assertEquals("someId", RequestContext.getId());
	}

	@Test
	public void test_should_return_bad_request_when_city_request_is_empty() throws Exception {
		when(cityService.getCityByCityReq(any(CityRequest.class))).thenReturn(null);
		mockMvc.perform(post("/city/getCitybyCityRequest").header("X-CORRELATION-ID", "someId")
				.contentType(MediaType.APPLICATION_JSON_UTF8)).andExpect(status().isBadRequest());
	}

	private String getFileContents(String fileName) {
		try {
			return IOUtils.toString(this.getClass().getClassLoader().getResourceAsStream(fileName), "UTF-8");
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

}