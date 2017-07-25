package org.egov.boundary.web.controller;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyListOf;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.egov.boundary.domain.service.BoundaryService;
import org.egov.boundary.domain.service.CrossHierarchyService;
import org.egov.boundary.persistence.entity.Boundary;
import org.egov.boundary.persistence.entity.BoundaryType;
import org.egov.boundary.web.contract.factory.ResponseInfoFactory;
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

	@MockBean
	private ResponseInfoFactory responseInfoFactory;

	@Test
	public void testShouldFetchAllLocationsForGivenWard() throws Exception {
		final Boundary expectedBoundary = Boundary.builder().id(1L).name("Bank Road").build();
		when(crossHierarchyService.getActiveChildBoundariesByBoundaryIdAndTenantId(any(Long.class), any(String.class)))
				.thenReturn(Collections.singletonList(expectedBoundary));
		mockMvc.perform(
				post("/boundarys/childLocationsByBoundaryId").param("boundaryId", "1").param("tenantId", "ap.public"))
				.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(content().json(getFileContents("boundaryResponse.json")));
	}

	@Test
	public void testShouldReturnBadRequestWhenWardIsEmpty() throws Exception {
		when(crossHierarchyService.getActiveChildBoundariesByBoundaryIdAndTenantId(any(Long.class), any(String.class)))
				.thenReturn(null);
		mockMvc.perform(
				post("/boundarys/childLocationsByBoundaryId").param("boundaryId", "").param("tenantId", "ap.public")
						.header("X-CORRELATION-ID", "someId").contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(status().isBadRequest());
	}

	@Test
	public void testShouldFetchAllBoundariesForBoundarytypenameAndHierarchytypename() throws Exception {
		final Boundary expectedBoundary = Boundary.builder().id(1L).name("Bank Road").build();
		when(boundaryService.getBoundariesByBndryTypeNameAndHierarchyTypeNameAndTenantId(any(String.class),
				any(String.class), any(String.class))).thenReturn(Collections.singletonList(expectedBoundary));
		mockMvc.perform(post("/boundarys/boundariesByBndryTypeNameAndHierarchyTypeName").param("tenantId", "ap.public")
				.param("boundaryTypeName", "Ward").param("hierarchyTypeName", "Admin")
				.header("X-CORRELATION-ID", "someId")).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(content().json(getFileContents("boundaryResponse.json")));
	}

	@Test
	public void testShouldReturnBadRequestWhenBoundarytypenameAndHierarchytypenameIsEmpty() throws Exception {
		when(boundaryService.getBoundariesByBndryTypeNameAndHierarchyTypeNameAndTenantId(any(String.class),
				any(String.class), any(String.class))).thenReturn(null);
		mockMvc.perform(post("/boundarys/boundariesByBndryTypeNameAndHierarchyTypeName").param("tenantId", "ap.public")
				.param("boundaryTypeName", "").param("hierarchyTypeName", "").header("X-CORRELATION-ID", "someId")
				.contentType(MediaType.APPLICATION_JSON_UTF8)).andExpect(status().isBadRequest());
	}

	@Test
	public void testShouldFetchAllBoundariesForBoundarytypeidAndtenantId() throws Exception {
		final Boundary expectedBoundary = Boundary.builder().id(1L).name("Bank Road").build();
		when(boundaryService.getAllBoundariesByBoundaryTypeIdAndTenantId(any(Long.class), any(String.class)))
				.thenReturn(Collections.singletonList(expectedBoundary));
		mockMvc.perform(post("/boundarys/getByBoundaryType").param("boundaryTypeId", "7").param("tenantId", "tenantId"))
				.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(content().json(getFileContents("boundaryResponse.json")));
	}

	@Test
	public void testShouldReturnBadRequestWhenBoundarytypeidAndTenmantIdIsEmpty() throws Exception {
		when(boundaryService.getAllBoundariesByBoundaryTypeIdAndTenantId(any(Long.class), any(String.class)))
				.thenReturn(null);
		mockMvc.perform(post("/boundarys/getByBoundaryType").param("boundaryTypeId", "").param("tenantId", "")
				.header("X-CORRELATION-ID", "someId").contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(status().isBadRequest());
	}

	@Test
	public void testShouldReturnBadRequestWhenTenantIdAndBoundaryIdsNotProvided() throws Exception {
		when(boundaryService.getAllBoundariesByBoundaryIdsAndTenant(any(String.class), anyListOf(Long.class)))
				.thenReturn(null);
		mockMvc.perform(post("/boundarys/_search").contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(status().isBadRequest());
	}

	@Test
	public void testShouldReturnBadRequestWhenTenantIdAndBoundaryIdsAreEmpty() throws Exception {
		when(boundaryService.getAllBoundariesByBoundaryIdsAndTenant(any(String.class), anyListOf(Long.class)))
				.thenReturn(null);
		mockMvc.perform(post("/boundarys/_search").param("tenantId", "").param("boundaryIds", "")
				.contentType(MediaType.APPLICATION_JSON_UTF8)).andExpect(status().isBadRequest());
	}

	@Test
	public void testShouldBoundarySearchReturnSuccesresponse() throws Exception {

		List<Boundary> boundaries = getBoundaries();

		when(boundaryService.getAllBoundariesByBoundaryIdsAndTenant(any(String.class), anyListOf(Long.class)))
				.thenReturn(boundaries);
		mockMvc.perform(post("/boundarys/_search").param("tenantId", "default").param("boundaryIds", "1,2")
				.contentType(MediaType.APPLICATION_JSON_UTF8)).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(content().json(getFileContents("boundarySearchResponse.json")));
	}

	private List<Boundary> getBoundaries() {

		List<Boundary> boundaries = new ArrayList<Boundary>();

		Boundary boundary1 = new Boundary();

		boundary1.setId(1l);
		boundary1.setName("Srikakulam  Municipality");
		boundary1.setBoundaryNum(1l);
		boundary1.setTenantId("default");

		BoundaryType bt1 = new BoundaryType();

		bt1.setId(1l);
		bt1.setName("City");
		bt1.setHierarchy(1l);
		bt1.setTenantId("default");
		bt1.setVersion(0l);
		boundary1.setBoundaryType(bt1);

		Boundary boundary2 = new Boundary();

		boundary2.setId(2l);
		boundary2.setName("Zone-1");
		boundary2.setBoundaryNum(1l);
		boundary2.setTenantId("default");

		boundary2.setParent(boundary1);

		BoundaryType bt2 = new BoundaryType();

		bt2.setId(3l);
		bt2.setName("Zone");
		bt2.setHierarchy(3l);
		bt2.setTenantId("default");
		bt2.setVersion(0l);
		boundary2.setBoundaryType(bt2);

		boundaries.add(boundary1);
		boundaries.add(boundary2);

		return boundaries;
	}

	private String getFileContents(String fileName) {
		try {
			return IOUtils.toString(this.getClass().getClassLoader().getResourceAsStream(fileName), "UTF-8");
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

}
