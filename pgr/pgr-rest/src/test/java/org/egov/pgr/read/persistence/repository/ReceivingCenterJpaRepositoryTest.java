package org.egov.pgr.read.persistence.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.egov.pgr.TestConfiguration;
import org.egov.pgr.common.entity.ReceivingCenter;
import org.egov.pgr.common.repository.ReceivingCenterJpaRepository;
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
public class ReceivingCenterJpaRepositoryTest {

	@Autowired
	private ReceivingCenterJpaRepository receivingCenterJpaRepository;

	@Test
	@Sql(scripts = { "/sql/clearReceivingCenter.sql", "/sql/InsertReceivingCenterData.sql" })
	public void should_fetch_all_receiving_centers() {
		final List<ReceivingCenter> actualResult = receivingCenterJpaRepository.findAllByTenantId("ap.public");
		assertEquals(10,actualResult.size());
        assertEquals("Mayor/Chairperson Office",actualResult.get(0).getName());
        assertEquals("Office of RDMA",actualResult.get(9).getName());
	}

	@Test
	@Sql(scripts = { "/sql/clearReceivingCenter.sql", "/sql/InsertReceivingCenterData.sql" })
	public void shouldFetchReceivingCenterById() {
		final ReceivingCenter actualResult = receivingCenterJpaRepository.findByIdAndTenantId(1L, "ap.public");
		assertEquals(Long.valueOf(1), actualResult.getId());
		assertEquals("Complaint Cell", actualResult.getName());
	}
}