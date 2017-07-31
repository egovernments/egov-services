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
import org.egov.boundary.domain.service.BoundaryTypeService;
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
	private BoundaryTypeService boundaryTypeService;

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
	public void testShouldReturnBadRequestWhenTenantIsNotThere() throws Exception {
		mockMvc.perform(post("/boundarys/_search").header("X-CORRELATION-ID", "someId")
				.contentType(MediaType.APPLICATION_JSON_UTF8)).andExpect(status().isBadRequest());
	}

	@Test
	public void testShouldReturnBadRequestWhenTenantIsEmpty() throws Exception {
		mockMvc.perform(post("/boundarys/_search").param("tenantId", "").header("X-CORRELATION-ID", "someId")
				.contentType(MediaType.APPLICATION_JSON_UTF8)).andExpect(status().isBadRequest());
	}

	@Test
	public void testShouldReturnBoundariesWhenTenanIdNotNull() throws Exception {

		List<Boundary> boundaryList = getBoundaries();
		when(boundaryService.getAllBoundaryByTenantId("default")).thenReturn(boundaryList);

		mockMvc.perform(post("/boundarys/_search").param("tenantId", "default").header("X-CORRELATION-ID", "someId")
				.contentType(MediaType.APPLICATION_JSON_UTF8)).andExpect(status().isOk());
	}

	@Test
	public void testShouldReturnBoundariesWhenTenanIdAndBoundaryTypeNotNull() throws Exception {

		List<Long> list = new ArrayList<Long>();
	
		list.add(1l);
		
		List<Boundary> boundaryList = getBoundaries();
		
		when(boundaryTypeService.getAllBoundarytypesByNameAndTenant("City","default")).thenReturn(getBoundaryTypeList());
		
		when(boundaryService.getAllBoundaryByTenantIdAndTypeIds("default",list)).thenReturn(boundaryList);

		mockMvc.perform(post("/boundarys/_search").param("tenantId", "default").param("boundaryType", "City")
				.header("X-CORRELATION-ID", "someId")
				.contentType(MediaType.APPLICATION_JSON_UTF8)).andExpect(status().isOk());
	}
	
	@Test
	public void testShouldReturnBoundariesWhenTenanIdAndBoundaryIdsAndTypeNotNull() throws Exception {

		List<Long> list = new ArrayList<Long>();
	
		list.add(1l);
		
		List<Boundary> boundaryList = getBoundaries();
		
		when(boundaryService.getAllBoundariesByNumberAndType("default",1l,list)).thenReturn(boundaryList);

		mockMvc.perform(post("/boundarys/_search").param("tenantId", "default").param("boundaryIds", "1").param("boundaryNum", "1")
				.header("X-CORRELATION-ID", "someId")
				.contentType(MediaType.APPLICATION_JSON_UTF8)).andExpect(status().isOk());
	}
	
	@Test
	public void testShouldReturnBoundariesWhenTenanIdAndBoundaryIdsNotNull() throws Exception {

		List<Long> list = new ArrayList<Long>();
	
		list.add(1l);
		
		List<Boundary> boundaryList = getBoundaries();
		
		when(boundaryService.getAllBoundariesByBoundaryIdsAndTenant("default",list)).thenReturn(boundaryList);

		mockMvc.perform(post("/boundarys/_search").param("tenantId", "default").param("boundaryIds", "1")
				.header("X-CORRELATION-ID", "someId")
				.contentType(MediaType.APPLICATION_JSON_UTF8)).andExpect(status().isOk());
	}
	
	
	@Test
	public void testShouldReturnBoundariesWhenTenanIdAndBoundaryNumberNotNull() throws Exception {

		List<Long> list = new ArrayList<Long>();
	
		list.add(1l);
		
		List<Boundary> boundaryList = getBoundaries();
		
		when(boundaryService.getAllBoundaryByTenantIdAndNumber("default",1l)).thenReturn(boundaryList);

		mockMvc.perform(post("/boundarys/_search").param("tenantId", "default").param("boundaryNum", "1")
				.header("X-CORRELATION-ID", "someId")
				.contentType(MediaType.APPLICATION_JSON_UTF8)).andExpect(status().isOk());
	}
	
	
	@Test
	public void testShouldReturnBoundariesWhenTenanIdAndBoundaryTypeNotNullAndTypeNotExistInDb () throws Exception {

		List<Long> list = new ArrayList<Long>();
	
		list.add(1l);
		
		List<Boundary> boundaryList = getBoundaries();
		
		when(boundaryTypeService.getAllBoundarytypesByNameAndTenant("City","default")).thenReturn(new ArrayList<BoundaryType>());
		
		when(boundaryService.getAllBoundaryByTenantIdAndTypeIds("default",list)).thenReturn(boundaryList);

		mockMvc.perform(post("/boundarys/_search").param("tenantId", "default").param("boundaryType", "City")
				.header("X-CORRELATION-ID", "someId")
				.contentType(MediaType.APPLICATION_JSON_UTF8)).andExpect(status().isBadRequest());
	}
	

	private List<Boundary> getBoundaries() {

		List<Boundary> boundaryList = new ArrayList<Boundary>();

		Boundary boundary1 = new Boundary();
		
		boundary1.setId(1l);
		boundary1.setTenantId("default");
		boundary1.setName("TestBoundaryOne");

		Boundary boundary2 = new Boundary();

		boundary2.setId(2l);
		boundary2.setName("TestBoundaryTwo");
		boundary2.setTenantId("default");

		boundaryList.add(boundary1);
		boundaryList.add(boundary2);
		return boundaryList;

	}
	
	private List<BoundaryType> getBoundaryTypeList(){
		
		List<BoundaryType> boundaryTypeList = new ArrayList<BoundaryType>();
		
		BoundaryType type1 = new BoundaryType();
		
		type1.setId(1l);
		type1.setLocalName("TestOne");
		
		boundaryTypeList.add(type1);
		
		return boundaryTypeList;
	}

	private String getFileContents(String fileName) {
		try {
			return IOUtils.toString(this.getClass().getClassLoader().getResourceAsStream(fileName), "UTF-8");
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

}
