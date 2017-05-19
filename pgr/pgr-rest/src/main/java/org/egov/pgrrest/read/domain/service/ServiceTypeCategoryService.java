package org.egov.pgrrest.read.domain.service;

import java.util.List;

import org.egov.pgrrest.common.entity.ServiceTypeCategory;
import org.egov.pgrrest.read.persistence.repository.ComplaintTypeCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class ServiceTypeCategoryService {

    private final ComplaintTypeCategoryRepository complaintTypeCategoryRepository;

    @Autowired
    public ServiceTypeCategoryService(final ComplaintTypeCategoryRepository complaintTypeCategoryRepository) {
        this.complaintTypeCategoryRepository = complaintTypeCategoryRepository;
    }

    public ServiceTypeCategory findById(final Long categoryId) {
        return complaintTypeCategoryRepository.findOne(categoryId);
    }

    @Transactional
    public ServiceTypeCategory createComplaintTypeCategory(final ServiceTypeCategory complaintTypeCategory) {
        return complaintTypeCategoryRepository.save(complaintTypeCategory);
    }

    public List<ServiceTypeCategory> getAll(final String tenantId) {
        return complaintTypeCategoryRepository.findAllByTenantIdOrderByNameAsc(tenantId);
    }

    public ServiceTypeCategory findByName(final String categoryName) {
        return complaintTypeCategoryRepository.findByName(categoryName);
    }
}
