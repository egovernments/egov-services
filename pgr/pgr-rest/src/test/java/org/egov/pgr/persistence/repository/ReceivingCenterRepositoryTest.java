package org.egov.pgr.persistence.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.util.List;

import org.egov.pgr.persistence.entity.ReceivingCenter;
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
public class ReceivingCenterRepositoryTest {

	@Autowired
	private ReceivingCenterRepository receivingCenterRepository;

	@Test
	@Sql(scripts = { "/sql/clearReceivingCenter.sql", "/sql/InsertReceivingCenterData.sql" })
	public void shouldFetchAllReceingCenters() {
		final List<ReceivingCenter> actualResult = receivingCenterRepository.findAll();
		assertFalse(actualResult.isEmpty());
	}

	@Test
	@Sql(scripts = { "/sql/clearReceivingCenter.sql", "/sql/InsertReceivingCenterData.sql" })
	public void shouldFetchReceivingCenterById() {
		final ReceivingCenter actualResult = receivingCenterRepository.findById(1L);
		assertEquals(Long.valueOf(1), actualResult.getId());
		assertEquals("Complaint Cell", actualResult.getName());
	}
}