package org.egov.boundary.domain.service;

import static org.mockito.Mockito.verify;

import javax.persistence.EntityManager;

import org.egov.boundary.persistence.repository.BoundaryJpaRepository;
import org.egov.boundary.persistence.repository.BoundaryRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@RunWith(MockitoJUnitRunner.class)
public class BoundaryServiceTest {

	@Mock
	private BoundaryJpaRepository boundaryJpaRepository;

	@Mock
	private BoundaryRepository boundaryRepository;

	@Mock
	private CrossHierarchyService crossHierarchyService;

	@Mock
	private BoundaryTypeService boundaryTypeService;

	private BoundaryService boundaryService;

	@Autowired
	private EntityManager entityManager;

	@Before
	public void before() {
		boundaryService = new BoundaryService(boundaryJpaRepository, entityManager, boundaryTypeService,
				crossHierarchyService, boundaryRepository);
	}

	@Test
	@Transactional
	public void test_should_fetch_boundaries_for_boundarytype_and_hierarchytype_name() {

		boundaryService.getBoundariesByBndryTypeNameAndHierarchyTypeNameAndTenantId("Ward", "ADMINISTRATION",
				"tenantId");

		verify(boundaryRepository).getBoundariesByBndryTypeNameAndHierarchyTypeNameAndTenantId("Ward", "ADMINISTRATION",
				"tenantId");
	}

	@Test
	@Transactional
	public void test_should_fetch_boundaries_for_boundarytype_and_tenantid() {

		boundaryService.getAllBoundariesByBoundaryTypeIdAndTenantId(1l, "tenantId");

		verify(boundaryRepository).getAllBoundariesByBoundaryTypeIdAndTenantId(1l, "tenantId");
	}
	
	
	@Test
	@Transactional
	public void test_should_fetch_boundaries_for_id_and_tenantid() {

		boundaryService.getBoundariesByIdAndTenantId(1l, "tenantId");

		verify(boundaryRepository).getBoundariesByIdAndTenantId(1l, "tenantId");
	}

}