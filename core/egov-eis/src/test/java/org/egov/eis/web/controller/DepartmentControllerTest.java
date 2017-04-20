package org.egov.eis.web.controller;

import org.apache.commons.io.IOUtils;
import org.egov.eis.domain.service.DepartmentService;
import org.egov.eis.persistence.entity.Department;
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

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(DepartmentController.class)
public class DepartmentControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private DepartmentService departmentService;

	@Test
	public void testShouldFetchDepartmentForGivenCode() throws Exception {
		final String departmentCode = "departmentCode";
		final Department department = new Department();
		department.setId(1L);
		department.setCode("departmentCode");
		department.setName("departmentName");
		when(departmentService.find(departmentCode, null, "tenantId"))
				.thenReturn(Collections.singletonList(department));

		mockMvc.perform(get("/departments").header("X-CORRELATION-ID", "someId").param("tenantId", "tenantId")
				.param("code", departmentCode)).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(content().json(getFileContents("departmentsResponse.json")));
	}

	@Test
	public void testShouldFetchDepartmentForGivenId() throws Exception {
		final Department department = new Department();
		department.setId(1L);
		department.setCode("departmentCode");
		department.setName("departmentName");
		when(departmentService.find(null, 1L, "tenantId")).thenReturn(Collections.singletonList(department));

		mockMvc.perform(get("/departments").header("X-CORRELATION-ID", "someId").param("tenantId", "tenantId")
				.param("id", "1")).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(content().json(getFileContents("departmentsResponse.json")));
	}

	@Test
	public void testShouldFetchAllDepartments() throws Exception {
		final Department department = new Department();
		department.setId(1L);
		department.setCode("departmentCode");
		department.setName("departmentName");
		when(departmentService.find(null, null, "ap.public")).thenReturn(Collections.singletonList(department));

		mockMvc.perform(get("/departments").header("X-CORRELATION-ID", "someId").param("tenantId", "ap.public"))
				.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(content().json(getFileContents("departmentsResponse.json")));
	}

	private String getFileContents(String fileName) {
		try {
			return IOUtils.toString(this.getClass().getClassLoader().getResourceAsStream(fileName), "UTF-8");
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

}