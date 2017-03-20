package org.egov.pgr.repository;

import static org.junit.Assert.assertEquals;

import org.egov.pgr.entity.ReceivingMode;
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
public class ReceivingModeRepositoryTest {

	@Autowired
	private ReceivingModeRepository receivingModeRepository;

	@Test
	@Sql(scripts = {"/sql/createReceivingModeTable.sql", "/sql/clearReceivingMode.sql", "/sql/InsertReceivingModeData.sql" })
	public void shouldFetchReceivingCenterById() {
		final ReceivingMode actualResult = receivingModeRepository.findByCode("WEBSITE");
		assertEquals(Long.valueOf(1), actualResult.getId());
		assertEquals("Website", actualResult.getName());
	}
}