package org.egov.boundary.persistence.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.egov.boundary.persistence.entity.City;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringRunner.class)
@DataJpaTest
@Transactional(propagation = Propagation.NOT_SUPPORTED)
public class CityRepositoryTest {

	@Autowired
	private CityRepository cityRepository;

	@Test
	@Sql(scripts = { "/sql/clearCity.sql", "/sql/createCity.sql" })
	public void shouldFetchCityForGivenCityCode() {
		final City city = cityRepository.findByCodeAndTenantId("0001","ap.public");
		assertEquals("0001", city.getCode());
	}

	@Test
	@Sql(scripts = { "/sql/clearCity.sql", "/sql/createCity.sql" })
	public void shouldSaveCity() {
		final City city = City.builder().code("0002").name("Kurnool Municipal Corporation").domainURL("kurnool")
				.districtName("Kurnool").districtCode("KMC").localName("Kurnool").tenantId("ap.public").build();
		final City savedCity = cityRepository.save(city);
		assertTrue("Id generated for city", savedCity.getId() > 0);
		assertEquals("0002", cityRepository.findByCodeAndTenantId("0002","ap.public").getCode());
	}
}
