package org.egov.collection.persistence.repository;

import java.util.List;

import org.egov.collection.persistence.entity.ServiceCategory;
import org.egov.collection.persistence.specification.ServiceCategorySpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class ServiceCategoryRepository {

	@Autowired
	ServiceCategoryJpaRepository serviceCategoryJpaRepository;

	public org.egov.collection.domain.model.ServiceCategory create(final ServiceCategory serviceCategory) {
		return serviceCategoryJpaRepository.save(serviceCategory).toDomain();
	}

	public org.egov.collection.domain.model.ServiceCategory update(final ServiceCategory serviceCategory) {
		return serviceCategoryJpaRepository.save(serviceCategory).toDomain();
	}

	public ServiceCategory findByCodeAndTenantId(String code, String tenantId) {
		return serviceCategoryJpaRepository.findByCodeAndTenantId(code, tenantId);
	}

	public List<ServiceCategory> findAll(ServiceCategorySpecification businessCategorySpecification, Sort sort) {
		return serviceCategoryJpaRepository.findAll(businessCategorySpecification, sort);
	}
}
