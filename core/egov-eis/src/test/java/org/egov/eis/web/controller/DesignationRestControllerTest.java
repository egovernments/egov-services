package org.egov.eis.web.controller;

import org.apache.commons.io.IOUtils;
import org.egov.eis.domain.service.DesignationService;
import org.egov.eis.persistence.entity.Designation;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.io.IOException;
import java.util.Collections;
import java.util.Date;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(DesignationRestController.class)
public class DesignationRestControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private DesignationService designationService;

	@Test
	public void test_should_fetch_all_designations_for_given_department() throws Exception {
		final Designation expectedDesignation = Designation.builder().id(1L).code("designationCode")
				.name("designationName").build();
		when(designationService.getAllDesignationByDepartment(any(Long.class), any(Date.class), any(String.class)))
				.thenReturn(Collections.singletonList(expectedDesignation));

		mockMvc.perform(post("/designationByDepartmentId").param("id", "1").param("tenantId", "ap.public")
				.header("X-CORRELATION-ID", "someId")).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(content().json(getFileContents("designationResponse.json")));
	}

	@Test
	public void test_should_return_bad_request_when_department_is_empty() throws Exception {
		when(designationService.getAllDesignationByDepartment(any(Long.class), any(Date.class), any(String.class)))
				.thenReturn(null);
		mockMvc.perform(post("/designationByDepartmentId").param("id", "").param("tenantId", "ap.public")
				.header("X-CORRELATION-ID", "someId").contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(status().isBadRequest());
	}

	private String getFileContents(String fileName) {
		try {
			return IOUtils.toString(this.getClass().getClassLoader().getResourceAsStream(fileName), "UTF-8");
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}