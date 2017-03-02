package org.egov.pgr.persistence.repository;

import static org.junit.Assert.assertFalse;

import java.util.List;

import org.egov.pgr.persistence.entity.ReceivingMode;
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
	@Sql(scripts = { "/sql/clearReceivingMode.sql", "/sql/InsertReceivingModeData.sql" })
	public void shouldFetchAllReceingModes() {
		final List<ReceivingMode> actualResult = receivingModeRepository.findAll();
		assertFalse(actualResult.isEmpty());
	}
}