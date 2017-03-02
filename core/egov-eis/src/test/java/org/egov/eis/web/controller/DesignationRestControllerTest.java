package org.egov.eis.web.controller;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;
import java.util.Collections;
import java.util.Date;

import org.apache.commons.io.IOUtils;
import org.egov.eis.domain.model.RequestContext;
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
		when(designationService.getAllDesignationByDepartment(any(Long.class), any(Date.class)))
				.thenReturn(Collections.singletonList(expectedDesignation));

		mockMvc.perform(post("/designationByDepartmentId").param("id", "1").header("X-CORRELATION-ID", "someId"))
				.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(content().json(getFileContents("designationResponse.json")));

		assertEquals("someId", RequestContext.getId());
	}

	private String getFileContents(String fileName) {
		try {
			return IOUtils.toString(this.getClass().getClassLoader().getResourceAsStream(fileName), "UTF-8");
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}