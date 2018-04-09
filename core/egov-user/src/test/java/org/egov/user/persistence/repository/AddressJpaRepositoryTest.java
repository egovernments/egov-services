package org.egov.user.persistence.repository;

import org.egov.user.TestConfiguration;
import org.egov.user.persistence.entity.Address;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@DataJpaTest
@Import(TestConfiguration.class)
public class AddressJpaRepositoryTest {

	@Autowired
	private AddressJpaRepository addressJpaRepository;

	@Test
	@Sql(scripts = {
			"/sql/clearUserRoles.sql",
			"/sql/clearAddresses.sql",
			"/sql/clearRoles.sql",
			"/sql/clearUsers.sql",
			"/sql/createUsers.sql",
			"/sql/createAddresses.sql"
	})
	public void test_should_fetch_addresses_for_given_user_id_and_tenant() {
		final List<Address> addresses = addressJpaRepository.findByUserIdAndTenantId(6L, "tenant2");

		assertNotNull(addresses);
		assertEquals(2 , addresses.size());
		assertEquals("address3", addresses.get(0).getAddress());
		assertEquals("city3", addresses.get(0).getCity());
		assertEquals("pincode3", addresses.get(0).getPinCode());
		assertEquals("tenant2", addresses.get(0).getTenantId());
		assertEquals(Long.valueOf(6L), addresses.get(0).getUserId());
		assertEquals("address4", addresses.get(1).getAddress());
	}

}