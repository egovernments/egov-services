package org.egov.boundary.domain.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.egov.boundary.persistence.entity.Boundary;
import org.egov.boundary.persistence.repository.CrossHierarchyRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class CrossHierarchyServiceTest {

	@Mock
	private CrossHierarchyRepository crossHierarchyRepository;

	@InjectMocks
	private CrossHierarchyService crossHierarchyService;

	@Test
	public void test_should_fetch_child_locations_for_boundary_id() {

		when(crossHierarchyRepository.findActiveBoundariesByIdAndTenantId(1L,"tenantId")).thenReturn(getExpectedBoundaryDetails());

		List<Boundary> boundaryList = crossHierarchyService.getActiveChildBoundariesByBoundaryIdAndTenantId(1L,"tenantId");

		assertEquals("Bank Road", boundaryList.get(0).getName());
	}

	private List<Boundary> getExpectedBoundaryDetails() {
		final List<Boundary> boundaryList = new ArrayList<>();
		final Boundary boundary = Boundary.builder().id(1L).name("Bank Road").tenantId("tenantId").build();
		boundaryList.add(boundary);
		return boundaryList;
	}
}
