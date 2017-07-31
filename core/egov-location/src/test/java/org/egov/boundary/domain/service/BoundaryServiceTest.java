package org.egov.boundary.domain.service;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyListOf;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import org.egov.boundary.persistence.entity.Boundary;
import org.egov.boundary.persistence.entity.BoundaryType;
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
	public void test_should_check_shapefileexist() {

		assertTrue(boundaryService.checkTenantShapeFileExistOrNot("ap.addanki"));

	}

	@Test
	public void test_should_check_shapefileNotexist() {

		assertFalse(boundaryService.checkTenantShapeFileExistOrNot("maharashtra.addanki"));

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
	
	@Test
	public void testShouldGetAllBoundariesByNumberAndType(){
		
		when(boundaryJpaRepository.findAllBoundariesByNumberAndType(any(String.class),any(Long.class),anyListOf(Long.class)))
		.thenReturn(getBoundaries());
		
		List<Long> list = new ArrayList<Long>();
		list.add(1l);
		list.add(2l);
		
		List<Boundary> boundaryList = boundaryService.getAllBoundariesByNumberAndType("default", 1l,list);
		
		assertTrue(boundaryList.size() == 2);
		assertFalse(boundaryList.isEmpty());
		assertTrue(boundaryList != null);
		assertTrue(boundaryList.get(0).getName().equals("Srikakulam  Municipality"));
		assertTrue(boundaryList.get(0).getTenantId().equals("default"));
	}
	
	@Test
	public void testShouldGetAllBoundariesByBoundaryIdsAndTenant(){
		
		when(boundaryJpaRepository.findAllBoundariesByIdsAndTenant(any(String.class),anyListOf(Long.class)))
		.thenReturn(getBoundaries());
		
		List<Long> list = new ArrayList<Long>();
		list.add(1l);
		list.add(2l);
		
		List<Boundary> boundaryList = boundaryService.getAllBoundariesByBoundaryIdsAndTenant("default",list);
		
		assertTrue(boundaryList.size() == 2);
		assertFalse(boundaryList.isEmpty());
		assertTrue(boundaryList != null);
		assertTrue(boundaryList.get(0).getName().equals("Srikakulam  Municipality"));
		assertTrue(boundaryList.get(0).getTenantId().equals("default"));
	}
	
	@Test
	public void testshouldGetAllBoundaryByTenantIdAndNumber(){
		
		when(boundaryJpaRepository.getAllBoundaryByTenantIdAndNumber(any(String.class),any(Long.class)))
		.thenReturn(getBoundaries());
		
		List<Long> list = new ArrayList<Long>();
		list.add(1l);
		list.add(2l);
		
		List<Boundary> boundaryList = boundaryService.getAllBoundaryByTenantIdAndNumber("default",1l);
		
		assertTrue(boundaryList.size() == 2);
		assertFalse(boundaryList.isEmpty());
		assertTrue(boundaryList != null);
		assertTrue(boundaryList.get(0).getName().equals("Srikakulam  Municipality"));
		assertTrue(boundaryList.get(0).getTenantId().equals("default"));
	}
	
	
	@Test
	public void testshouldGetAllBoundaryByTenantIdAndTypeIds(){
		
		when(boundaryJpaRepository.getAllBoundaryByTenantIdAndTypeIds(any(String.class),anyListOf(Long.class)))
		.thenReturn(getBoundaries());
		
		List<Long> list = new ArrayList<Long>();
		list.add(1l);
		list.add(2l);
		
		List<Boundary> boundaryList = boundaryService.getAllBoundaryByTenantIdAndTypeIds("default",list);
		
		assertTrue(boundaryList.size() == 2);
		assertFalse(boundaryList.isEmpty());
		assertTrue(boundaryList != null);
		assertTrue(boundaryList.get(0).getName().equals("Srikakulam  Municipality"));
		assertTrue(boundaryList.get(0).getTenantId().equals("default"));
	}
	
	@Test
	public void testshouldGetAllBoundaryByTenantId(){
		
		when(boundaryJpaRepository.findAllByTenantId(any(String.class)))
		.thenReturn(getBoundaries());
		
		List<Long> list = new ArrayList<Long>();
		list.add(1l);
		list.add(2l);
		
		List<Boundary> boundaryList = boundaryService.getAllBoundaryByTenantId("default");
		
		assertTrue(boundaryList.size() == 2);
		assertFalse(boundaryList.isEmpty());
		assertTrue(boundaryList != null);
		assertTrue(boundaryList.get(0).getName().equals("Srikakulam  Municipality"));
		assertTrue(boundaryList.get(0).getTenantId().equals("default"));
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

}