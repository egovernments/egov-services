package org.egov.boundary.web.controller;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;
import java.util.Collections;

import org.apache.commons.io.IOUtils;
import org.egov.boundary.domain.model.RequestContext;
import org.egov.boundary.domain.service.BoundaryService;
import org.egov.boundary.domain.service.CrossHierarchyService;
import org.egov.boundary.persistence.entity.Boundary;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(SpringRunner.class)
@WebMvcTest(BoundaryController.class)
public class BoundaryControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private BoundaryService boundaryService;

	@MockBean
	private CrossHierarchyService crossHierarchyService;

	@Test
	public void test_should_fetch_all_designations_for_given_department() throws Exception {
		final Boundary expectedBoundary = Boundary.builder().id(1L).name("Bank Road").build();
		when(crossHierarchyService.getActiveChildBoundariesByBoundaryId(any(Long.class)))
				.thenReturn(Collections.singletonList(expectedBoundary));
		mockMvc.perform(post("/boundarys/childLocationsByBoundaryId").param("boundaryId", "1")
				.header("X-CORRELATION-ID", "someId")).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(content().json(getFileContents("boundaryResponse.json")));

		assertEquals("someId", RequestContext.getId());
	}

	@Test
	public void test_should_return_bad_request_when_department_is_empty() throws Exception {
		when(crossHierarchyService.getActiveChildBoundariesByBoundaryId(any(Long.class))).thenReturn(null);
		mockMvc.perform(post("/boundarys/childLocationsByBoundaryId").param("boundaryId", "")
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
