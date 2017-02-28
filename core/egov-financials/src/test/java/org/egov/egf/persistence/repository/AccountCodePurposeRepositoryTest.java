package org.egov.egf.persistence.repository;

import static org.junit.Assert.assertEquals;
import org.egov.egf.persistence.entity.AccountCodePurpose;
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

public class AccountCodePurposeRepositoryTest {
	@Autowired
	private AccountCodePurposeRepository accountCodePurposeRepository;

	@Test
	@Sql(scripts = { "/sql/createAccountCodePurpose.sql","/sql/clearAccountCodePurpose.sql", "/sql/insertAccountCodePorpose.sql" })
	public void shouldFetchNameFromGivenName() {
		final AccountCodePurpose actualAccountCode = accountCodePurposeRepository.findByName("abc");
		assertEquals("abc", actualAccountCode.getName());
	}
}
