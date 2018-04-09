package org.egov.user.persistence.repository;

import org.egov.user.domain.model.Address;
import org.egov.user.domain.model.enums.AddressType;
import org.hamcrest.CustomMatcher;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AddressRepositoryTest {

	@Mock
	private AddressJpaRepository addressJpaRepository;

	@InjectMocks
	private AddressRepository addressRepository;

	@Test
	public void test_should_save_new_address() {
		final Address domainAddress = Address.builder()
				.city("city")
				.address("address")
				.pinCode("pinCode")
				.type(AddressType.CORRESPONDENCE)
				.build();

		final Address createdAddress = addressRepository.create(domainAddress, 1L, "tenant");

		final org.egov.user.persistence.entity.Address expectedAddress =
				org.egov.user.persistence.entity.Address.builder()
						.city("city")
						.address("address")
						.pinCode("pinCode")
						.type("CORRESPONDENCE")
						.userId(1L)
						.tenantId("tenant")
						.build();
		expectedAddress.setCreatedBy(1L);

		assertNotNull(createdAddress);
		assertEquals("address", createdAddress.getAddress());
		assertEquals("city", createdAddress.getCity());
		assertEquals("pinCode", createdAddress.getPinCode());
		assertEquals(AddressType.CORRESPONDENCE, createdAddress.getType());
		verify(addressJpaRepository).save(argThat(new AddressMatcher(expectedAddress, true)));
	}

	@Test
	public void test_should_return_addresses_for_given_user_id_and_tenant() {
		final org.egov.user.persistence.entity.Address address1 =
				org.egov.user.persistence.entity.Address.builder()
						.type("PERMANENT")
						.build();
		final org.egov.user.persistence.entity.Address address2 =
				org.egov.user.persistence.entity.Address.builder()
						.type("CORRESPONDENCE")
						.build();
		when(addressJpaRepository.findByUserIdAndTenantId(1L, "tenant"))
				.thenReturn(Arrays.asList(address1, address2));

		final List<Address> actualAddresses = addressRepository.find(1L, "tenant");

		assertNotNull(actualAddresses);
		assertEquals(2, actualAddresses.size());
	}

	@Test
	public void test_should_delete_all_associated_addresses() {
		final org.egov.user.persistence.entity.Address entityAddress1 =
				org.egov.user.persistence.entity.Address.builder()
						.type("PERMANENT")
						.build();
		final org.egov.user.persistence.entity.Address entityAddress2 =
				org.egov.user.persistence.entity.Address.builder()
						.type("CORRESPONDENCE")
						.build();
		final Address domainAddress1 = Address.builder().build();
		final Address domainAddress2 = Address.builder().build();
		final List<Address> domainAddresses = Collections.emptyList();
		final List<org.egov.user.persistence.entity.Address> entityAddresses =
				Arrays.asList(entityAddress1, entityAddress2);
		when(addressJpaRepository.findByUserIdAndTenantId(1L, "tenant")).thenReturn(entityAddresses);

		addressRepository.update(domainAddresses, 1L, "tenant");

		verify(addressJpaRepository).delete(entityAddress1);
		verify(addressJpaRepository).delete(entityAddress2);
	}

	@Test
	public void test_should_delete_addresses_that_are_not_specified() {
		final org.egov.user.persistence.entity.Address entityAddress1 =
				org.egov.user.persistence.entity.Address.builder()
						.type("PERMANENT")
						.build();
		final org.egov.user.persistence.entity.Address entityAddress2 =
				org.egov.user.persistence.entity.Address.builder()
						.type("CORRESPONDENCE")
						.build();
		final Address domainAddress1 = Address.builder()
				.type(AddressType.CORRESPONDENCE)
				.build();
		final List<Address> domainAddresses = Collections.singletonList(domainAddress1);
		final List<org.egov.user.persistence.entity.Address> entityAddresses =
				Arrays.asList(entityAddress1, entityAddress2);
		when(addressJpaRepository.findByUserIdAndTenantId(1L, "tenant")).thenReturn(entityAddresses);

		addressRepository.update(domainAddresses, 1L, "tenant");

		verify(addressJpaRepository).delete(entityAddress1);
	}

	@Test
	public void test_should_save_new_addresses() {
		final org.egov.user.persistence.entity.Address entityAddress1 =
				org.egov.user.persistence.entity.Address.builder()
						.type("PERMANENT")
						.build();
		final Address domainAddress1 = Address.builder()
				.type(AddressType.CORRESPONDENCE)
				.pinCode("pinCode")
				.city("city")
				.address("address")
				.build();
		final Address domainAddress2 = Address.builder()
				.type(AddressType.PERMANENT)
				.build();
		final List<Address> domainAddresses = Arrays.asList(domainAddress1, domainAddress2);
		final List<org.egov.user.persistence.entity.Address> entityAddresses =
				Collections.singletonList(entityAddress1);
		when(addressJpaRepository.findByUserIdAndTenantId(1L, "tenant")).thenReturn(entityAddresses);

		addressRepository.update(domainAddresses, 1L, "tenant");

		final org.egov.user.persistence.entity.Address expectedAddress =
				org.egov.user.persistence.entity.Address.builder()
						.type("CORRESPONDENCE")
						.userId(1L)
						.tenantId("tenant")
						.pinCode("pinCode")
						.city("city")
						.address("address")
						.build();
		expectedAddress.setCreatedBy(1L);
		verify(addressJpaRepository).save(argThat(new AddressMatcher(expectedAddress, true)));
	}

	@Test
	public void test_should_update_existing_addresses() {
		final org.egov.user.persistence.entity.Address entityAddress1 =
				org.egov.user.persistence.entity.Address.builder()
						.type("PERMANENT")
						.tenantId("tenant")
						.userId(1L)
						.build();
		entityAddress1.setCreatedBy(1L);
		entityAddress1.setCreatedDate(new Date());
		final Address domainAddress1 = Address.builder()
				.type(AddressType.PERMANENT)
				.pinCode("new pinCode")
				.city("new city")
				.address("new address")
				.build();
		final List<Address> domainAddresses = Collections.singletonList(domainAddress1);
		final List<org.egov.user.persistence.entity.Address> entityAddresses =
				Collections.singletonList(entityAddress1);
		when(addressJpaRepository.findByUserIdAndTenantId(1L, "tenant")).thenReturn(entityAddresses);

		addressRepository.update(domainAddresses, 1L, "tenant");

		final org.egov.user.persistence.entity.Address expectedAddress =
				org.egov.user.persistence.entity.Address.builder()
						.type("PERMANENT")
						.userId(1L)
						.tenantId("tenant")
						.pinCode("new pinCode")
						.city("new city")
						.address("new address")
						.build();
		expectedAddress.setCreatedBy(1L);
		verify(addressJpaRepository).save(argThat(new AddressMatcher(expectedAddress, true)));
	}

	private class AddressMatcher extends CustomMatcher<org.egov.user.persistence.entity.Address> {

		private org.egov.user.persistence.entity.Address expectedAddress;
		private boolean isNewAddress;

		AddressMatcher(org.egov.user.persistence.entity.Address expectedAddress, boolean isNewAddress) {
			super("Address matcher");
			this.expectedAddress = expectedAddress;
			this.isNewAddress = isNewAddress;
		}

		@Override
		public boolean matches(Object o) {
			final org.egov.user.persistence.entity.Address actualAddress = (org.egov.user.persistence.entity
					.Address) o;
			return expectedAddress.getAddress().equals(actualAddress.getAddress())
					&& expectedAddress.getCity().equals(actualAddress.getCity())
					&& expectedAddress.getPinCode().equals(actualAddress.getPinCode())
					&& expectedAddress.getTenantId().equals(actualAddress.getTenantId())
					&& expectedAddress.getUserId().equals(actualAddress.getUserId())
					&& expectedAddress.getCreatedBy().equals(actualAddress.getCreatedBy())
					&& isNewAddressFieldValid(actualAddress)
					&& isUpdateAddressFieldValid(actualAddress);
		}

		private boolean isUpdateAddressFieldValid(org.egov.user.persistence.entity.Address actualAddress) {
			if (isNewAddress) {
				return true;
			}
			return actualAddress.getLastModifiedDate() != null
					&& actualAddress.getCreatedDate() != null
					&& actualAddress.getLastModifiedBy() != null;
		}

		private boolean isNewAddressFieldValid(org.egov.user.persistence.entity.Address actualAddress) {
			if (!isNewAddress) {
				return true;
			}
			return actualAddress.getCreatedDate() != null;
		}
	}

}