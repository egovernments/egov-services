package org.egov.boundary.persistence.repository;

import static org.junit.Assert.assertEquals;

import java.util.List;

import javax.persistence.EntityManager;

import org.egov.boundary.persistence.entity.Boundary;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class BoundaryRepositoryTest {

	@Autowired
	private EntityManager entityManager;

	private BoundaryRepository boundaryRepository;

	@Before
	public void before() {
		boundaryRepository = new BoundaryRepository(entityManager);
	}

	@Test
	@Sql(scripts = { "/sql/clearBoundary.sql", "/sql/createBoundary.sql" })
	@Transactional
	public void test_should_fetch_boundaries_for_boundarytype_and_hierarchytype_name() {
		final List<Boundary> boundarys = boundaryRepository
				.getBoundariesByBndryTypeNameAndHierarchyTypeNameAndTenantId("City", "ADMINISTRATION", "default");
		assertEquals("Srikakulam  Municipality", boundarys.get(0).getName());
	}

	@Test
	@Sql(scripts = { "/sql/clearBoundary.sql", "/sql/createBoundary.sql" })
	@Transactional
	public void test_should_fetch_boundaries_for_boundarytype_and_tenantid() {
		final List<Boundary> boundarys = boundaryRepository.getAllBoundariesByBoundaryTypeIdAndTenantId(1l, "default");
		assertEquals("Srikakulam  Municipality", boundarys.get(0).getName());
	}

	@Test
	@Sql(scripts = { "/sql/clearBoundary.sql", "/sql/createBoundary.sql" })
	@Transactional
	public void test_should_fetch_boundaries_for_id_and_tenantid() {
		final List<Boundary> boundarys = boundaryRepository.getBoundariesByIdAndTenantId(1l, "default");
		assertEquals("Srikakulam  Municipality", boundarys.get(0).getName());
	}

}
