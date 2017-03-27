package org.egov.boundary.domain.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.egov.boundary.persistence.entity.BoundaryType;
import org.egov.boundary.persistence.repository.BoundaryTypeRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class BoundaryTypeServiceTest {

	@Mock
	private BoundaryTypeRepository boundaryTypeRepository;

	@InjectMocks
	private BoundaryTypeService boundaryTypeService;

	@Test
	public void TestShouldFetchBoundarieTypesForHierarchytypeIdAndTenantId() {
		when(boundaryTypeRepository.findByHierarchyTypeIdAndTenantId(1L, "tenantId"))
				.thenReturn(getExpectedBoundaryTypeDetails());

		List<BoundaryType> boundarytypeList = boundaryTypeService.getAllBoundarTypesByHierarchyTypeIdAndTenantId(1L,
				"tenantId");

		assertEquals("City 1", boundarytypeList.get(0).getName());
	}

	private List<BoundaryType> getExpectedBoundaryTypeDetails() {
		final List<BoundaryType> boundaryTypeList = new ArrayList<>();
		final BoundaryType boundaryType1 = BoundaryType.builder().id(1L).name("City 1").build();
		final BoundaryType boundaryType2 = BoundaryType.builder().id(2L).name("City 2").build();
		boundaryTypeList.add(boundaryType1);
		boundaryTypeList.add(boundaryType2);
		return boundaryTypeList;
	}
}