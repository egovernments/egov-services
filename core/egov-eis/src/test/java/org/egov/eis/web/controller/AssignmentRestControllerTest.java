package org.egov.eis.web.controller;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;
import java.util.Collections;
import java.util.Date;

import org.apache.commons.io.IOUtils;
import org.egov.eis.domain.model.RequestContext;
import org.egov.eis.domain.service.AssignmentService;
import org.egov.eis.persistence.entity.Assignment;
import org.egov.eis.persistence.entity.Department;
import org.egov.eis.persistence.entity.Designation;
import org.egov.eis.persistence.entity.Position;
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

	@Test
	public void test_Should_Fetch_Assignments_For_Department_Or_Designation_Id() throws Exception {
		Department department = Department.builder().name("Electrical").build();
		Designation designation = Designation.builder().name("Assistant engineer").build();
		final Assignment assignment = Assignment.builder().id(5L).fund(1L).department(department)
				.designation(designation).build();
		when(assignmentService.getPositionsByDepartmentAndDesignationForGivenRange(any(Long.class), any(Long.class),
				any(Date.class))).thenReturn(Collections.singletonList(assignment));

		mockMvc.perform(post("/assignmentsByDeptOrDesignId").param("deptId", "1").param("desgnId", "2")
				.header("X-CORRELATION-ID", "someId")).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(content().json(getFileContents("successAssignmentResponse.json")));

		assertEquals("someId", RequestContext.getId());
	}

	@Test
	public void test_should_return_bad_request_when_dept_desg_is_empty() throws Exception {
		when(assignmentService.getPositionsByDepartmentAndDesignationForGivenRange(any(Long.class), any(Long.class),
				any(Date.class))).thenReturn(null);
		mockMvc.perform(post("/assignmentsByDeptOrDesignId").header("X-CORRELATION-ID", "someId")
				.contentType(MediaType.APPLICATION_JSON_UTF8)).andExpect(status().isBadRequest());
	}

	private String getFileContents(String fileName) {
		try {
			return IOUtils.toString(this.getClass().getClassLoader().getResourceAsStream(fileName), "UTF-8");
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Test
	public void test_should_return_assignment_when_employee_id_given() throws  Exception {
		Department department = Department.builder().name("Electrical").build();
		Assignment assignment = Assignment.builder()
				.id(2L)
				.department(department)
				.build();

		when(assignmentService.getPrimaryAssignmentForEmployee(any(Long.class)))
				.thenReturn(assignment);

		mockMvc.perform(post("/_assignmentByEmployeeId").param("employeeId","18")
				.header("X-CORRELATION-ID", "someId")).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(content().json(getFileContents("assignmentByEmployeeIdResponse.json")));

		assertEquals("someId", RequestContext.getId());
	}

}