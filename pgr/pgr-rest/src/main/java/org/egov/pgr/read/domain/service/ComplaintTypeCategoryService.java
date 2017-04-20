package org.egov.pgr.read.domain.service;

import org.egov.pgr.common.entity.ComplaintTypeCategory;
import org.egov.pgr.read.persistence.repository.ComplaintTypeCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class ComplaintTypeCategoryService {

    private final ComplaintTypeCategoryRepository complaintTypeCategoryRepository;

    @Autowired
    public ComplaintTypeCategoryService(final ComplaintTypeCategoryRepository complaintTypeCategoryRepository) {
        this.complaintTypeCategoryRepository = complaintTypeCategoryRepository;
    }

    public ComplaintTypeCategory findById(final Long categoryId) {
        return complaintTypeCategoryRepository.findOne(categoryId);
    }

    @Transactional
    public ComplaintTypeCategory createComplaintTypeCategory(final ComplaintTypeCategory complaintTypeCategory) {
        return complaintTypeCategoryRepository.save(complaintTypeCategory);
    }

    public List<ComplaintTypeCategory> getAll(final String tenantId) {
        return complaintTypeCategoryRepository.findAllByTenantIdOrderByNameAsc(tenantId);
    }

    public ComplaintTypeCategory findByName(final String categoryName) {
        return complaintTypeCategoryRepository.findByName(categoryName);
    }
}
