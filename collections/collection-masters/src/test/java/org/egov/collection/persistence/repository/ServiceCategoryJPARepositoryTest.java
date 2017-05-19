package org.egov.collection.persistence.repository;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;

import org.egov.collection.domain.model.ServiceCategorySearchCriteria;
import org.egov.collection.persistence.entity.ServiceCategory;
import org.egov.collection.persistence.specification.ServiceCategorySpecification;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ServiceCategoryJPARepositoryTest {

	@Autowired
	ServiceCategoryJpaRepository serviceCategoryJpaRepository;

	@Test
	@Sql(scripts = { "/sql/clearServiceCategory.sql", "/sql/createServiceCategory.sql" })
	public void test_to_find_serviceCategory_By_Code_And_TenantId() {
		final ServiceCategory entityServiceCategory = serviceCategoryJpaRepository.findByCodeAndTenantId("TL",
				"default");
		assertEquals(entityServiceCategory.getCode(), "TL");
		assertEquals(entityServiceCategory.getName(), "Trade Licence");
		assertEquals(entityServiceCategory.getTenantId(), "default");
	}

	@Test
	@Sql(scripts = { "/sql/clearServiceCategory.sql", "/sql/createServiceCategory.sql" })
	public void test_to_find_all_service_categories() {
		Sort sort = new Sort(Direction.DESC, "code");
		final List<ServiceCategory> entityServiceCategory = serviceCategoryJpaRepository
				.findAll(getServiceCategorySpecification(), sort);
		assertEquals(entityServiceCategory.size(), 3);
		assertEquals(entityServiceCategory.get(0).getName(), "Trade Licence");
		assertEquals(entityServiceCategory.get(1).getName(), "Sewerage Tax");
		assertEquals(entityServiceCategory.get(2).getName(), "Advertisement Tax");

	}

	private ServiceCategorySpecification getServiceCategorySpecification() {
		List<Long> Ids = Arrays.asList(1L, 2L, 3L);
		ServiceCategorySearchCriteria serviceCriteria = ServiceCategorySearchCriteria.builder().isactive(true)
				.tenantId("default").ids(Ids).build();
		return new ServiceCategorySpecification(serviceCriteria);
	}

}
