package org.egov.pgrrest.read.persistence.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.util.List;

import org.egov.pgrrest.TestConfiguration;
import org.egov.pgrrest.common.entity.ReceivingMode;
import org.egov.pgrrest.common.repository.ReceivingModeJpaRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@DataJpaTest
@Import(TestConfiguration.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ReceivingModeJpaRepositoryTest {

	@Autowired
	private ReceivingModeJpaRepository receivingModeJpaRepository;

	@Test
	@Sql(scripts = { "/sql/clearReceivingMode.sql", "/sql/InsertReceivingModeData.sql" })
	public void should_fetch_all_receiving_modes_with_tenantId() {
		final List<ReceivingMode> actualResult = receivingModeJpaRepository.findAllByTenantId("ap.public");

		assertEquals(6, actualResult.size());
	}

	@Test
    @Sql(scripts = { "/sql/clearReceivingMode.sql", "/sql/InsertReceivingModeData.sql" })
    public void test_should_find_receivingmode_when_code_isProvided(){
       ReceivingMode receivingmode= receivingModeJpaRepository.findByCode("WEBSITE");
        assertEquals("Website",receivingmode.getName());
}
}