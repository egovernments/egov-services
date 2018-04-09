package org.egov.user.persistence.repository;

import org.egov.user.domain.model.Address;
import org.egov.user.domain.model.enums.AddressType;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.util.CollectionUtils.isEmpty;

@Service
public class AddressRepository {
	private AddressJpaRepository addressJpaRepository;

	public AddressRepository(AddressJpaRepository addressJpaRepository) {
		this.addressJpaRepository = addressJpaRepository;
	}

	public Address create(Address address, Long userId, String tenantId) {
		final org.egov.user.persistence.entity.Address entityAddress =
				new org.egov.user.persistence.entity.Address(address);
		entityAddress.setCreatedDate(new Date());
		entityAddress.setCreatedBy(userId);
		entityAddress.setTenantId(tenantId);
		entityAddress.setUserId(userId);
		addressJpaRepository.save(entityAddress);
		return entityAddress.toDomain();
	}

	public void update(List<Address> domainAddresses, Long userId, String tenantId) {
		final List<org.egov.user.persistence.entity.Address> entityAddresses =
				addressJpaRepository.findByUserIdAndTenantId(userId, tenantId);

		if (isEmpty(domainAddresses) && isEmpty(entityAddresses)) {
			return;
		}

		conditionallyDeleteAllAddresses(domainAddresses, entityAddresses);
		deleteRemovedAddresses(domainAddresses, entityAddresses);
		createNewAddresses(domainAddresses, entityAddresses, userId, tenantId);
		updateAddresses(domainAddresses, entityAddresses, userId);
	}

	public List<Address> find(Long userId, String tenantId) {
		return addressJpaRepository.findByUserIdAndTenantId(userId, tenantId).stream()
				.map(org.egov.user.persistence.entity.Address::toDomain)
				.collect(Collectors.toList());
	}

	private void updateAddresses(List<Address> domainAddresses,
								 List<org.egov.user.persistence.entity.Address> entityAddresses,
								 Long userId) {
		Map<String, org.egov.user.persistence.entity.Address> typeToEntityAddressMap = toMap(entityAddresses);
		domainAddresses.forEach(address -> updateAddress(typeToEntityAddressMap, address, userId));
	}

	private void updateAddress(Map<String, org.egov.user.persistence.entity.Address> typeToEntityAddressMap,
							   Address address,
							   Long userId) {
		final org.egov.user.persistence.entity.Address matchingEntityAddress =
				typeToEntityAddressMap.getOrDefault(address.getType().name(), null);

		if (matchingEntityAddress == null) {
			return;
		}

		matchingEntityAddress.setCity(address.getCity());
		matchingEntityAddress.setAddress(address.getAddress());
		matchingEntityAddress.setPinCode(address.getPinCode());
		matchingEntityAddress.setLastModifiedBy(userId);
		matchingEntityAddress.setLastModifiedDate(new Date());
		addressJpaRepository.save(matchingEntityAddress);
	}

	private Map<String, org.egov.user.persistence.entity.Address> toMap(
			List<org.egov.user.persistence.entity.Address> entityAddresses) {
		return entityAddresses.stream()
				.collect(Collectors.toMap(org.egov.user.persistence.entity.Address::getType, address -> address));
	}

	private void conditionallyDeleteAllAddresses(List<Address> domainAddresses,
												 List<org.egov.user.persistence.entity.Address> entityAddresses) {
		if (domainAddresses == null) {
			addressJpaRepository.delete(entityAddresses);
		}
	}

	private void deleteRemovedAddresses(List<Address> domainAddresses,
										List<org.egov.user.persistence.entity.Address> entityAddresses) {
		final List<String> addressTypesToRetain = getAddressTypesToRetain(domainAddresses);
		entityAddresses.stream()
				.filter(address -> !addressTypesToRetain.contains(address.getType()))
				.forEach(address -> addressJpaRepository.delete(address));
	}

	private void createNewAddresses(List<Address> domainAddresses,
									List<org.egov.user.persistence.entity.Address> entityAddresses,
									Long userId,
									String tenantId) {
		final List<AddressType> addressTypesToCreate = getAddressTypesToCreate(domainAddresses, entityAddresses);
		domainAddresses.stream()
				.filter(address -> addressTypesToCreate.contains(address.getType()))
				.forEach(address -> create(address, userId, tenantId));
	}

	private List<String> getAddressTypesToRetain(List<Address> domainAddresses) {
		return domainAddresses.stream()
				.map(address -> address.getType().name())
				.collect(Collectors.toList());
	}

	private List<AddressType> getAddressTypesToCreate(List<Address> domainAddresses,
												 List<org.egov.user.persistence.entity.Address> entityAddresses) {
		final List<String> entityAddressTypes = entityAddresses.stream()
				.map(org.egov.user.persistence.entity.Address::getType)
				.collect(Collectors.toList());

		return domainAddresses.stream()
				.filter(address -> !entityAddressTypes.contains(address.getType().name()))
				.map(Address::getType)
				.collect(Collectors.toList());
	}

}
