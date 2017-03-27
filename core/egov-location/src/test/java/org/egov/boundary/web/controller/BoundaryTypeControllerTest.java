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
import org.egov.boundary.domain.service.BoundaryTypeService;
import org.egov.boundary.persistence.entity.BoundaryType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(SpringRunner.class)
@WebMvcTest(BoundaryTypeController.class)
public class BoundaryTypeControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private BoundaryTypeService boundaryTypeService;


	@Test
	public void testShouldFetchAllBoundarieTypesForHierarchyTypeidAndtenantId() throws Exception {
		final BoundaryType expectedBoundaryType = BoundaryType.builder().id(1L).name("City").build();
		when(boundaryTypeService.getAllBoundarTypesByHierarchyTypeIdAndTenantId(any(Long.class), any(String.class)))
				.thenReturn(Collections.singletonList(expectedBoundaryType));
		mockMvc.perform(post("/boundarytypes/getByHierarchyType").param("hierarchyTypeId", "1").param("tenantId", "tenantId")
				.header("X-CORRELATION-ID", "someId")).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(content().json(getFileContents("boundaryTypeResponse.json")));

		assertEquals("someId", RequestContext.getId());
	}

	@Test
	public void testShouldReturnBadRequestWhenHierarchyTypeidAndTenmantIdIsEmpty() throws Exception {
		when(boundaryTypeService.getAllBoundarTypesByHierarchyTypeIdAndTenantId(any(Long.class), any(String.class)))
				.thenReturn(null);
		mockMvc.perform(post("/boundarytypes/getByHierarchyType").param("hierarchyTypeId", "")
				.param("tenantId", "").header("X-CORRELATION-ID", "someId")
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
