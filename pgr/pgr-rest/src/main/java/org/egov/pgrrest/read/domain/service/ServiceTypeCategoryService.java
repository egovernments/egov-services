package org.egov.pgrrest.read.domain.service;

import java.util.List;

import org.egov.pgrrest.common.persistence.entity.ServiceTypeCategory;
import org.egov.pgrrest.read.persistence.repository.ServiceTypeCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class ServiceTypeCategoryService {

    private final ServiceTypeCategoryRepository serviceTypeCategoryRepository;

    @Autowired
    public ServiceTypeCategoryService(final ServiceTypeCategoryRepository serviceTypeCategoryRepository) {
        this.serviceTypeCategoryRepository = serviceTypeCategoryRepository;
    }

    public ServiceTypeCategory findById(final Long categoryId) {
        return serviceTypeCategoryRepository.findOne(categoryId);
    }

    @Transactional
    public ServiceTypeCategory createComplaintTypeCategory(final ServiceTypeCategory complaintTypeCategory) {
        return serviceTypeCategoryRepository.save(complaintTypeCategory);
    }

    public List<ServiceTypeCategory> getAll(final String tenantId) {
        return serviceTypeCategoryRepository.findAllByTenantIdOrderByNameAsc(tenantId);
    }

    public ServiceTypeCategory findByName(final String categoryName) {
        return serviceTypeCategoryRepository.findByName(categoryName);
    }
}
