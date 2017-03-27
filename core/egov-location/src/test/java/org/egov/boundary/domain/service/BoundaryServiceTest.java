package org.egov.boundary.domain.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.egov.boundary.persistence.entity.Boundary;
import org.egov.boundary.persistence.repository.BoundaryRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class BoundaryServiceTest {

	@Mock
	private BoundaryRepository boundaryRepository;

	@InjectMocks
	private BoundaryService boundaryService;

	@Test
	public void TestShouldFetchBoundariesForBoundarytypeAndHierarchytypeName() {
		when(boundaryRepository.findBoundariesByBndryTypeNameAndHierarchyTypeName("Ward", "ADMINISTRATION"))
				.thenReturn(getExpectedBoundaryDetails());

		List<Boundary> boundaryList = boundaryService.getBoundariesByBndryTypeNameAndHierarchyTypeName("Ward",
				"ADMINISTRATION");

		assertEquals("Election Ward No 1", boundaryList.get(0).getName());
	}

	@Test
	public void TestShouldFetchBoundariesForBoundarytypeAndTenantId() {
		when(boundaryRepository.findBoundariesByBoundaryType_IdAndTenantId(7L, "tenantId"))
				.thenReturn(getExpectedBoundaryDetails());

		List<Boundary> boundaryList = boundaryService.getAllBoundariesByBoundaryTypeIdAndTenantId(7L, "tenantId");

		assertEquals("Election Ward No 1", boundaryList.get(0).getName());
	}

	private List<Boundary> getExpectedBoundaryDetails() {
		final List<Boundary> boundaryList = new ArrayList<>();
		final Boundary boundary1 = Boundary.builder().id(1L).name("Election Ward No 1").build();
		final Boundary boundary2 = Boundary.builder().id(2L).name("Election Ward No 2").build();
		boundaryList.add(boundary1);
		boundaryList.add(boundary2);
		return boundaryList;
	}
}