package org.egov.pgr.read.domain.service;

import org.egov.pgr.common.entity.ComplaintType;
import org.egov.pgr.read.domain.model.ComplaintTypeSearchCriteria;
import org.egov.pgr.read.persistence.repository.ComplaintTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ComplaintTypeService {

    private final ComplaintTypeRepository complaintTypeRepository;

    @Autowired
    public ComplaintTypeService(final ComplaintTypeRepository complaintTypeRepository) {
        this.complaintTypeRepository = complaintTypeRepository;
    }

    public ComplaintType getComplaintType(String complaintTypeCode, String tenantId) {
        return complaintTypeRepository.getComplaintType(complaintTypeCode, tenantId);
    }

    public List<ComplaintType> findByCriteria(ComplaintTypeSearchCriteria searchCriteria) {
        searchCriteria.validate();
        return findComplaintTypesByCriteria(searchCriteria);
    }

    private List<ComplaintType> findComplaintTypesByCriteria(ComplaintTypeSearchCriteria searchCriteria) {
        if (searchCriteria.isCategorySearch()) {
            return complaintTypeRepository.findActiveComplaintTypesByCategoryAndTenantId(searchCriteria.getCategoryId(),
                    searchCriteria.getTenantId());
        } else if (searchCriteria.isFrequencySearch()) {
            return complaintTypeRepository.getFrequentlyFiledComplaints(searchCriteria.getCount(),
                    searchCriteria.getTenantId());
        } else if (searchCriteria.isReturnAll()) {
            return complaintTypeRepository.getAllComplaintTypes();
        }
        return null;
    }
}
