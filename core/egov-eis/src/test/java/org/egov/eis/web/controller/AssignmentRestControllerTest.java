package org.egov.eis.web.controller;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;
import java.util.Collections;

import org.apache.commons.io.IOUtils;
import org.egov.eis.domain.model.RequestContext;
import org.egov.eis.domain.service.AssignmentService;
import org.egov.eis.persistence.entity.Assignment;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(SpringRunner.class)
@WebMvcTest(AssignmentRestController.class)
public class AssignmentRestControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private AssignmentService assignmentService;

	@Test
	public void testShouldFetchAssignmentForGivenId() throws Exception {
		final Assignment assignment = new Assignment();
		assignment.setId(1L);
		assignment.setFund(2L);
		when(assignmentService.getAssignmentById(1L)).thenReturn(assignment);

		mockMvc.perform(get("/assignments").header("X-CORRELATION-ID", "someId").param("id", "1"))
				.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(content().json(getFileContents("assignmentsResponse.json")));

		assertEquals("someId", RequestContext.getId());
	}

	@Test
	public void testShouldFetchAllAssignments() throws Exception {
		final Assignment assignment = new Assignment();
		assignment.setId(1L);
		assignment.setFund(2L);
		when(assignmentService.getAll()).thenReturn(Collections.singletonList(assignment));

		mockMvc.perform(get("/assignments").header("X-CORRELATION-ID", "someId")).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(content().json(getFileContents("assignmentsResponse.json")));

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